package cn.hotpot.chatroom.model.dto;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author qinzhu
 * @since 2019/12/19
 */
@Data
@Accessors(chain = true)
public class MessageFrame {

    /**
     * 消息类型 {@link cn.hotpot.chatroom.common.enums.WebsocketMeassageType}
     */
    private String type;

    /**
     * 消息内容，格式为{@link Message}转换后的json字符串
     */
    private String data;

}
