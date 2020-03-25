package cn.hotpot.chatroom.model.param;

import io.swagger.annotations.ApiModelProperty;
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
    @ApiModelProperty("账号")
    private String userId;
    @NotBlank
    @ApiModelProperty("密码")
    private String pwd;
    @NotBlank
    @ApiModelProperty("名称")
    private String username;
    @NotNull
    @ApiModelProperty("邀请码")
    private Integer invitationCode;
}
