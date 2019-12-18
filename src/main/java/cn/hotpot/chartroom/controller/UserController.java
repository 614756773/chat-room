package cn.hotpot.chartroom.controller;

import cn.hotpot.chartroom.model.vo.UserVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collections;

/**
 * @author qinzhu
 * @since 2019/12/17
 */
@RestController
public class UserController {

    @GetMapping("/getUserInfo")
    public UserVO getUserInfo() {
        UserVO.GroupVo groupVo = new UserVO.GroupVo()
                .setGroupAvatarUrl("https://img.hacpai.com/avatar/1551616223515_1574851307648.png")
                .setGroupId("123")
                .setGroupName("1组");
        return new UserVO()
                .setAvatarUrl("https://img.hacpai.com/avatar/1551616223515_1574851307648.png")
                .setGroupList(Collections.singletonList(groupVo))
                .setUserId("614756773")
                .setUsername("混合动力火锅")
                .setFriendList(new ArrayList<>());
    }
}
