package cn.hotpot.chartroom.model.dto;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author qinzhu
 * @since 2019/12/19
 */
@Data
@Accessors(chain = true)
public class Message {

    /**
     * 消息类型 {@link cn.hotpot.chartroom.enums.WebsocketMeassageType}
     */
    private String type;

    /**
     * 文字内容
     */
    private String data;

    /**
     * 表情包
     */
    private byte[] bqb;
}
