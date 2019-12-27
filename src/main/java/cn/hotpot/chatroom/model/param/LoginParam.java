package cn.hotpot.chatroom.model.param;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author qinzhu
 * @since 2019/12/20
 */
@Data
public class LoginParam {
    @NotBlank
    private String userId;
    @NotBlank
    private String pwd;
}
