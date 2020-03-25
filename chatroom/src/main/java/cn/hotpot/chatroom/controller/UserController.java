package cn.hotpot.chatroom.controller;

import cn.hotpot.chatroom.biz.InvitationCodeBiz;
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
import cn.hotpot.chatroom.model.param.UserParamForUpdate;
import cn.hotpot.chatroom.model.vo.UserVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author qinzhu
 * @since 2019/12/17
 */
@RestController
@RequestMapping("/user")
@Api(tags = "用户")
public class UserController {

    @Autowired
    private ChatUserRepository userRepository;

    @Autowired
    private InvitationCodeBiz invitationCodeBiz;

    @Autowired
    private ChatInvitationCodeRepository invitationCodeRepository;

    /**
     * 获取用户信息
     */
    @GetMapping("/getUserInfo")
    @ApiOperation("获取用户信息")
    public UserVO getUserInfo() {
        UserVO.GroupVo groupVo = new UserVO.GroupVo()
                .setGroupAvatarUrl("./img/avatar/Group01.jpg")
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
    @ApiOperation("登录")
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
    @ApiOperation("注册")
    public ResponseEntity<String> register(@Valid @RequestBody RegisterParam param) {
        ChatInvitationCode chatInvitation = invitationCodeRepository
                .findById(param.getInvitationCode())
                .orElseGet(() -> new ChatInvitationCode(-1));
        if (!param.getInvitationCode().equals(chatInvitation.getId())) {
            throw new RuntimeException("不存在的邀请码");
        }
        if (!StringUtils.isEmpty(chatInvitation.getWhoUse())) {
            throw new RuntimeException("邀请码已被使用");
        }
        if (!PattrenUtil.checkEmailFormat(param.getUserId())) {
            throw new RuntimeException("账号必须是邮箱");
        }

        // 保存新用户
        ChatUser newUser = new ChatUser()
                .setUserId(param.getUserId())
                .setPwd(AesEncryptUtils.encrypt(param.getPwd()))
                .setUsername(param.getUsername())
                .setAvatarUrl(AvatarUrls.getAny())
                .setReferrer(chatInvitation.getUserId());
        // 修改邀请码实体类
        chatInvitation.setWhoUse(param.getUserId());

        invitationCodeRepository.save(chatInvitation);
        userRepository.save(newUser);
        invitationCodeBiz.produce(newUser.getUserId());
        if (usedUp(newUser.getReferrer())) {
            invitationCodeBiz.produce(newUser.getReferrer());
        }
        return ResponseEntity.ok("注册成功");
    }

    @PutMapping("/{userId}")
    @ApiOperation("修改用户信息")
    public ResponseEntity<String> updateUser(@PathVariable String userId, @RequestBody UserParamForUpdate param) {
        ChatUser user = userRepository.findByUserId(userId);
        if (!AesEncryptUtils.decrypt(user.getPwd()).equals(param.getOldPwd())) {
            throw new RuntimeException("原密码输入错误");
        }
        if (!StringUtils.isEmpty(param.getPwd())) {
            user.setPwd(AesEncryptUtils.encrypt(param.getPwd()));
        }
        if (!StringUtils.isEmpty(param.getUserName())) {
            user.setUsername(param.getUserName());
        }
        userRepository.save(user);
        return ResponseEntity.ok("修改成功");
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
     * 用户的邀请码是否被使用完了
     */
    private boolean usedUp(String userId) {
        return invitationCodeRepository.remaining(userId) == 0;
    }
}
