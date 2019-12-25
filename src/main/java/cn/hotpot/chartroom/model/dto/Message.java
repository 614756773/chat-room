package cn.hotpot.chartroom.model.dto;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author qinzhu
 * @since 2019/12/25
 */
@Data
@Accessors(chain = true)
public class Message {
    private String fromUserId;

    private String avatarUrl;

    private String toGroupId;

    private String content;

    /**
     * {@link cn.hotpot.chartroom.common.enums.WebsocketMeassageType}
     */
    private String type;
}
