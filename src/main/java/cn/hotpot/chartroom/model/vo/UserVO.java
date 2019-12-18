package cn.hotpot.chartroom.model.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author qinzhu
 * @since 2019/12/17
 */
@Data
@Accessors(chain = true)
public class UserVO {
    private String userId;
    private String username;
    private String avatarUrl;
    private List<UserVO> friendList;
    private List<GroupVo> groupList;


    @Data
    @Accessors(chain = true)
    public static class GroupVo {
        private String groupAvatarUrl;
        private String groupId;
        private String groupName;
    }
}
