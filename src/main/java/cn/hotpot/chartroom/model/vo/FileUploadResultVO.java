package cn.hotpot.chartroom.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author qinzhu
 * @since 2019/12/26
 */
@Data
@AllArgsConstructor
public class FileUploadResultVO {
    private Integer id;
    private String name;
    private Long size;
}
