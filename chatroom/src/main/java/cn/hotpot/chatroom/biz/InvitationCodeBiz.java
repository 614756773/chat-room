package cn.hotpot.chatroom.biz;

import cn.hotpot.chatroom.dao.entity.ChatInvitationCode;
import cn.hotpot.chatroom.dao.entity.ChatUser;
import cn.hotpot.chatroom.dao.repository.ChatInvitationCodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author qinzhu
 * @since 2020/3/25
 */
@Service
public class InvitationCodeBiz {
    private static final int NEW_USER_INVITATION_CODE_NUM = 2;

    @Autowired
    private ChatInvitationCodeRepository invitationCodeRepository;

    /**
     * 新用户生成邀请码
     */
    public void produce(String userId) {
        List<ChatInvitationCode> list = new ArrayList<>(NEW_USER_INVITATION_CODE_NUM);
        for (int i = 0; i < NEW_USER_INVITATION_CODE_NUM; i++) {
            list.add(new ChatInvitationCode(userId));
        }
        invitationCodeRepository.saveAll(list);
    }
}
