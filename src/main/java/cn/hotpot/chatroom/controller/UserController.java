package cn.hotpot.chatroom.controller;

import cn.hotpot.chatroom.common.Constants;
import cn.hotpot.chatroom.common.utils.AesEncryptUtils;
import cn.hotpot.chatroom.common.utils.AvatarUrls;
import cn.hotpot.chatroom.common.utils.PattrenUtil;
import cn.hotpot.chatroom.dao.entity.ChatInvitationCode;
import cn.hotpot.chatroom.dao.entity.ChatUser;
import cn.hotpot.chatroom.dao.repository.ChatInvitationCodeRepository;
import cn.hotpot.chatroom.dao.repository.ChatUserRepository;
import cn.hotpot.chatroom.model.param.LoginParam;
import cn.hotpot.chatroom.model.param.RegisterParam;
import cn.hotpot.chatroom.model.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author qinzhu
 * @since 2019/12/17
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private ChatUserRepository userRepository;

    @Autowired
    private ChatInvitationCodeRepository invitationCodeRepository;

    /**
     * 获取用户信息
     */
    @GetMapping("/getUserInfo")
    public UserVO getUserInfo() {
        UserVO.GroupVo groupVo = new UserVO.GroupVo()
                .setGroupAvatarUrl("/cr/img/avatar/Group01.jpg")
                .setGroupId("01")
                .setGroupName("Chat Room");
        return new UserVO()
                .setAvatarUrl(AvatarUrls.getAny())
                .setGroupList(Collections.singletonList(groupVo))
                .setUserId("614756773")
                .setUsername("混合动力火锅")  // todo 邀请码
                .setFriendList(new ArrayList<>());
    }

    /**
     * 登录
     */
    @PostMapping("/login")
    public ResponseEntity<Object> login(@Valid @RequestBody LoginParam loginParam, HttpServletRequest request) {
        ChatUser chatUser = userRepository.findByUserId(loginParam.getUserId());
        if (chatUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("账号不存在");
        }
        if (!AesEncryptUtils.decrypt(chatUser.getPwd()).equals(loginParam.getPwd())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("密码错误");
        }
        saveSession(request, chatUser);

        return ResponseEntity.ok(chatUser);
    }

    /**
     * 注册
     */
    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody RegisterParam param) {
        ChatInvitationCode chatInvitation = invitationCodeRepository
                .findById(param.getInvitationCode())
                .orElseGet(() -> new ChatInvitationCode(-1));
        if (!param.getInvitationCode().equals(chatInvitation.getId())) {
            throw new RuntimeException("没有该邀请码");
        }
        if (!StringUtils.isEmpty(chatInvitation.getWhoUse())) {
            throw new RuntimeException("邀请码已被使用");
        }
        if (!PattrenUtil.checkEmailFormat(param.getUserId())) {
            throw new RuntimeException("账号必须是邮箱");
        }

        // 保存新用户
        ChatUser chatUser = new ChatUser()
                .setUserId(param.getUserId())
                .setPwd(AesEncryptUtils.encrypt(param.getPwd()))
                .setUsername(param.getUsername())
                .setAvatarUrl(AvatarUrls.getAny())
                .setReferrer(chatInvitation.getUserId());
        // 修改邀请码实体类
        chatInvitation.setWhoUse(param.getUserId());

        userRepository.save(chatUser);
        invitationCodeRepository.save(chatInvitation);
        return ResponseEntity.ok("注册成功");
    }

    /**
     * 生成邀请码
     */
    @GetMapping("/invitationCode/{userId}")
    @SuppressWarnings("unchecked")
    public ResponseEntity<Integer> createInvitationCode(@PathVariable String userId, HttpServletRequest request) {
        ChatUser chatUser;
        try {
            Map<String, ChatUser> userMap = (Map<String, ChatUser>) request.getSession().getAttribute(Constants.USER_SESSION);
            chatUser = userMap.get(userId);
        } catch (Exception e) {
            throw new RuntimeException("请先登录");
        }
        Integer invitationCode = produceInvitationCode(chatUser.getId());
        return ResponseEntity.ok(invitationCode);
    }

    @SuppressWarnings("unchecked")
    private void saveSession(HttpServletRequest request, ChatUser chatUser) {
        HttpSession session = request.getSession();
        Map<String, ChatUser> users = (Map<String, ChatUser>) session.getAttribute(Constants.USER_SESSION);
        if (users == null) {
            users = new ConcurrentHashMap<>();
            users.put(chatUser.getUserId(), chatUser);
            session.setAttribute(Constants.USER_SESSION, users);
        } else {
            users.put(chatUser.getUserId(), chatUser);
        }
    }

    /**
     * @param id user表的主键
     */
    private Integer produceInvitationCode(Integer id) {
        Optional<ChatUser> optional = userRepository.findById(id);
        if (!optional.isPresent()) {
            throw new RuntimeException("用户不存在");
        }

        ChatUser user = optional.get();
        Integer invitationId = user.getInvitationId();
        if (invitationId == null) {
            ChatInvitationCode chatInvitationCode = invitationCodeRepository.save(new ChatInvitationCode().setUserId(user.getUserId()));
            return chatInvitationCode.getId();
        }

        ChatInvitationCode chatInvitationCode = invitationCodeRepository.findById(invitationId)
                .orElseThrow(() -> new RuntimeException(invitationId + ":邀请码实体不存在"));
        LocalDate localDate = chatInvitationCode.getCreateTime().toLocalDate();
        if (localDate.isEqual(LocalDate.now())) {
            throw new RuntimeException("每天只能生产一个邀请码");
        }

        return invitationCodeRepository.save(new ChatInvitationCode().setUserId(user.getUserId())).getId();
    }
}
