package cn.hotpot.chatroom.scheduler;

import cn.hotpot.chatroom.dao.repository.ChatFileRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author qinzhu
 * @since 2019/12/27
 */
@Component
@Slf4j
public class DataCleaner {

    @Autowired
    private ChatFileRepository chatFileRepository;

    /**
     * 每周日清除chat_file表
     */
    @Scheduled(cron = "0 0 0 ? * 1")
    public void clearDbFile() {
        long count = chatFileRepository.count();
        chatFileRepository.truncateTable();
        log.info("清空chat_file表数据，共{}条", count);
    }
}
