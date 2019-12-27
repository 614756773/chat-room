package cn.hotpot.chatroom.dao.entity;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author qinzhu
 * @since 2019/12/20
 */
@Entity
@Table(indexes = {@Index(name = "userId", columnList = "userId", unique = true)})
@Data
@Accessors(chain = true)
@EntityListeners(AuditingEntityListener.class)
public class ChatInvitationCode {
    /**
     * 即是id，也是邀请码
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 该邀请码的持有者
     */
    @Column(length = 30)
    private String userId;

    /**
     * 谁使用了该邀请码
     */
    @Column
    private String whoUse;

    @Column
    @CreatedDate
    private LocalDateTime createTime;

    @Column
    @LastModifiedDate
    private LocalDateTime modifyTime;

    public ChatInvitationCode() {
    }

    public ChatInvitationCode(int mockInvitationCode) {
        this.id = mockInvitationCode;
    }
}
