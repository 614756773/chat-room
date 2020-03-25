package cn.hotpot.chatroom.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author qinzhu
 * @since 2019/12/17
 */
@Data
@Accessors(chain = true)
@ApiModel("用户信息")
public class UserVO {
    @ApiModelProperty("账号")
    private String userId;
    @ApiModelProperty("名称")
    private String username;
    @ApiModelProperty("头像")
    private String avatarUrl;
    @ApiModelProperty("邀请码")
    private String invitationCode;
    @ApiModelProperty("好友列表")
    private List<UserVO> friendList;
    @ApiModelProperty("组列表")
    private List<GroupVo> groupList;


    @Data
    @Accessors(chain = true)
    public static class GroupVo {
        private String groupAvatarUrl;
        private String groupId;
        private String groupName;
    }
}
