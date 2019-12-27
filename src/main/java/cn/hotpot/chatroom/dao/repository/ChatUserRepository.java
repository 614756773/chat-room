package cn.hotpot.chatroom.dao.repository;

import cn.hotpot.chatroom.dao.entity.ChatUser;
import org.springframework.data.repository.CrudRepository;

/**
 * @author qinzhu
 * @since 2019/12/20
 */
public interface ChatUserRepository extends CrudRepository<ChatUser, Integer>{
    ChatUser findByUserId(String userId);
}
