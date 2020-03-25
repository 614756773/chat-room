package cn.hotpot.chatroom.controller;

import cn.hotpot.chatroom.dao.entity.ChatGroup;
import cn.hotpot.chatroom.dao.repository.ChatGroupRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author qinzhu
 * @since 2020/3/25
 */
@RestController
@RequestMapping("/group")
@Api(tags = "组")
public class GroupController {
    @Autowired
    private ChatGroupRepository groupRepository;

    @ApiOperation("获取公共聊天室")
    @GetMapping("/public")
    public ResponseEntity<List<ChatGroup>> listPublicGroup() {
        return null;// TODO 使用缓存
    }
}
