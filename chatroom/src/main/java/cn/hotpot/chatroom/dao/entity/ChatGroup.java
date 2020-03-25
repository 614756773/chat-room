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
 * @since 2020/3/25
 */
@Data
@Entity
@Table(indexes = {@Index(name = "userId", columnList = "userId")})
@Accessors(chain = true)
@EntityListeners(AuditingEntityListener.class)
public class ChatGroup {
    /**
     * id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 名称
     */
    @Column
    private String name;

    /**
     * 聊天组头像
     */
    @Column
    private String avatarUrl;

    /**
     * 聊天组是否为私有
     */
    @Column
    private Boolean isPrivate;

    /**
     * 聊天组的拥有人
     */
    @Column(length = 30)
    private String userId;

    @Column
    @CreatedDate
    private LocalDateTime createTime;

    @Column
    @LastModifiedDate
    private LocalDateTime modifyTime;
}
