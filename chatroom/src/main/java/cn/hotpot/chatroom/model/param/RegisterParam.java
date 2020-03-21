package cn.hotpot.chatroom.model.param;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author qinzhu
 * @since 2019/12/20
 */
@Data
public class RegisterParam {
    @NotBlank
    private String userId;
    @NotBlank
    private String pwd;
    @NotBlank
    private String username;
    @NotNull
    private Integer invitationCode;
}
