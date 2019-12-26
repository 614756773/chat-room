package cn.hotpot.chartroom.controller;

import cn.hotpot.chartroom.common.Constants;
import cn.hotpot.chartroom.common.utils.AesEncryptUtils;
import cn.hotpot.chartroom.common.utils.AvatarUrls;
import cn.hotpot.chartroom.common.utils.PattrenUtil;
import cn.hotpot.chartroom.dao.entity.ChartInvitationCode;
import cn.hotpot.chartroom.dao.entity.ChartUser;
import cn.hotpot.chartroom.dao.repository.ChartInvitationCodeRepository;
import cn.hotpot.chartroom.dao.repository.ChartUserRepository;
import cn.hotpot.chartroom.model.param.LoginParam;
import cn.hotpot.chartroom.model.param.RegisterParam;
import cn.hotpot.chartroom.model.vo.UserVO;
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
    private ChartUserRepository userRepository;

    @Autowired
    private ChartInvitationCodeRepository invitationCodeRepository;

    /**
     * 获取用户信息
     */
    @GetMapping("/getUserInfo")
    public UserVO getUserInfo() {
        UserVO.GroupVo groupVo = new UserVO.GroupVo()
                .setGroupAvatarUrl("/cr/img/avatar/Group01.jpg")
                .setGroupId("01")
                .setGroupName("摸鱼聊天室");
        return new UserVO()
                .setAvatarUrl("https://img.hacpai.com/avatar/1551616223515_1574851307648.png")
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
        ChartUser chartUser = userRepository.findByUserId(loginParam.getUserId());
        if (chartUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("账号不存在");
        }
        if (!AesEncryptUtils.decrypt(chartUser.getPwd()).equals(loginParam.getPwd())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("密码错误");
        }
        saveSession(request, chartUser);

        return ResponseEntity.ok(chartUser);
    }

    /**
     * 注册
     */
    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody RegisterParam param) {
        ChartInvitationCode chartInvitation = invitationCodeRepository
                .findById(param.getInvitationCode())
                .orElseGet(() -> new ChartInvitationCode(-1));
        if (!param.getInvitationCode().equals(chartInvitation.getId())) {
            throw new RuntimeException("没有该邀请码");
        }
        if (!StringUtils.isEmpty(chartInvitation.getWhoUse())) {
            throw new RuntimeException("邀请码已被使用");
        }
        if (!PattrenUtil.checkEmailFormat(param.getUserId())) {
            throw new RuntimeException("账号必须是邮箱");
        }

        // 保存新用户
        ChartUser chartUser = new ChartUser()
                .setUserId(param.getUserId())
                .setPwd(AesEncryptUtils.encrypt(param.getPwd()))
                .setUsername(param.getUsername())
                .setAvatarUrl(AvatarUrls.getAny())
                .setReferrer(chartInvitation.getUserId());
        // 修改邀请码实体类
        chartInvitation.setWhoUse(param.getUserId());

        userRepository.save(chartUser);
        invitationCodeRepository.save(chartInvitation);
        return ResponseEntity.ok("注册成功");
    }

    /**
     * 生成邀请码
     */
    @GetMapping("/invitationCode/{userId}")
    @SuppressWarnings("unchecked")
    public ResponseEntity<Integer> createInvitationCode(@PathVariable String userId, HttpServletRequest request) {
        ChartUser chartUser;
        try {
            Map<String, ChartUser> userMap = (Map<String, ChartUser>) request.getSession().getAttribute(Constants.USER_SESSION);
            chartUser = userMap.get(userId);
        } catch (Exception e) {
            throw new RuntimeException("请先登录");
        }
        Integer invitationCode = produceInvitationCode(chartUser.getId());
        return ResponseEntity.ok(invitationCode);
    }

    @SuppressWarnings("unchecked")
    private void saveSession(HttpServletRequest request, ChartUser chartUser) {
        HttpSession session = request.getSession();
        Map<String, ChartUser> users = (Map<String, ChartUser>) session.getAttribute(Constants.USER_SESSION);
        if (users == null) {
            users = new ConcurrentHashMap<>();
            users.put(chartUser.getUserId(), chartUser);
            session.setAttribute(Constants.USER_SESSION, users);
        } else {
            users.put(chartUser.getUserId(), chartUser);
        }
    }

    /**
     * @param id user表的主键
     */
    private Integer produceInvitationCode(Integer id) {
        Optional<ChartUser> optional = userRepository.findById(id);
        if (!optional.isPresent()) {
            throw new RuntimeException("用户不存在");
        }

        ChartUser user = optional.get();
        Integer invitationId = user.getInvitationId();
        if (invitationId == null) {
            ChartInvitationCode chartInvitationCode = invitationCodeRepository.save(new ChartInvitationCode().setUserId(user.getUserId()));
            return chartInvitationCode.getId();
        }

        ChartInvitationCode chartInvitationCode = invitationCodeRepository.findById(invitationId)
                .orElseThrow(() -> new RuntimeException(invitationId + ":邀请码实体不存在"));
        LocalDate localDate = chartInvitationCode.getCreateTime().toLocalDate();
        if (localDate.isEqual(LocalDate.now())) {
            throw new RuntimeException("每天只能生产一个邀请码");
        }

        return invitationCodeRepository.save(new ChartInvitationCode().setUserId(user.getUserId())).getId();
    }
}
