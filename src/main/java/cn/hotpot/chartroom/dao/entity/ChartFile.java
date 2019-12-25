package cn.hotpot.chartroom.dao.entity;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author qinzhu
 * @since 2019/12/25
 */
@Entity
@Data
@Accessors(chain = true)
@EntityListeners(AuditingEntityListener.class)
public class ChartFile {
    /**
     * id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 文件名称
     */
    @Column
    private String name;

    /**
     * 数据
     */
    @Column(columnDefinition = "blob")
    private byte[] data;

    /**
     * 文件大小
     */
    @Column
    private Long size;

    /**
     * 文件类型，其实就是文件后缀
     */
    @Column(length = 10)
    private String type;

    @Column
    @CreatedDate
    private LocalDateTime createTime;

    @Column
    @LastModifiedDate
    private LocalDateTime modifyTime;
}
