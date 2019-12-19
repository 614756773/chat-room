package cn.hotpot.chartroom.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author qinzhu
 * @since 2019/12/19
 */
@AllArgsConstructor
@Getter
public enum WebsocketMeassageType {

    SINGLE_SENDING("SINGLE_SENDING", "私聊消息"),
    GROUP_SENDING("GROUP_SENDING", "群发消息"),
    FILE_MSG_SINGLE_SENDING("FILE_MSG_SINGLE_SENDING", "私发文件"),
    FILE_MSG_GROUP_SENDING("FILE_MSG_GROUP_SENDING", "群发文件");

    private String code;
    private String value;

}
