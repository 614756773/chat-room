package cn.hotpot.chatroom.model.param;

import lombok.Data;

/**
 * @author qinzhu
 * @since 2020/3/25
 */
@Data
public class UserParamForUpdate {
    private String oldPwd;
    private String pwd;
    private String userName;
}
