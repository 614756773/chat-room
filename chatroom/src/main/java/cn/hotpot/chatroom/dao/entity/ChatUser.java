package cn.hotpot.chatroom.dao.entity;

import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.UniqueElements;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author qinzhu
 * @since 2019/12/20
 */
@Data
@Entity
@Table(indexes = {@Index(name = "userId", columnList = "userId", unique = true)})
@Accessors(chain = true)
@EntityListeners(AuditingEntityListener.class)
public class ChatUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    /**
     * id即账号，强制使用邮箱作为账号
     */
    @UniqueElements
    @Column(length = 30)
    private String userId;
    /**
     * 用户昵称
     */
    @Column
    private String username;
    /**
     * 头像
     */
    @Column
    private String avatarUrl;
    /**
     * 密码
     */
    @Column
    private String pwd;
    /**
     * 引荐人
     */
    @Column
    private String referrer;
    /**
     * 邀请码，每个用户天只能生产3个
     */
    @Column
    private Integer invitationId;

    @Column
    @CreatedDate
    private LocalDateTime createTime;
}
