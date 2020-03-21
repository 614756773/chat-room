package cn.hotpot.chatroom.dao.repository;

import cn.hotpot.chatroom.dao.entity.ChatFile;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author qinzhu
 * @since 2019/12/25
 */
public interface ChatFileRepository extends CrudRepository<ChatFile, Integer> {

    /**
     * 清空表数据
     */
    @Transactional(rollbackFor = Exception.class)
    @Modifying
    @Query(value = "truncate table chat_file", nativeQuery = true)
    void truncateTable();

}
