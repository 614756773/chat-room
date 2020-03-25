package cn.hotpot.chatroom.dao.repository;

import cn.hotpot.chatroom.dao.entity.ChatInvitationCode;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

/**
 * @author qinzhu
 * @since 2019/12/20
 */
public interface ChatInvitationCodeRepository extends CrudRepository<ChatInvitationCode, Integer> {

    @Query(value = "SELECT count(ci.id) FROM ChatInvitationCode ci WHERE ci.userId = ?1 AND ci.whoUse IS NULL ")
    int remaining(String userId);
}
