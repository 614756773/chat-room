package cn.hotpot.chartroom.controller;

import cn.hotpot.chartroom.dao.entity.ChartFile;
import cn.hotpot.chartroom.dao.repository.ChartFileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author qinzhu
 * @since 2019/12/25
 */
@RestController
@RequestMapping("/file")
public class FileController {

    private static Set<String> imgSuffix = new HashSet<>(Arrays.asList("png", "jpg", "jpge"));

    @Autowired
    private ChartFileRepository chartFileRepository;

    @PostMapping("/upload")
    public ResponseEntity<Integer> upload(MultipartFile file) throws IOException {
        String suffix = checkSize(file);
        ChartFile chartFile = new ChartFile()
                .setName(file.getOriginalFilename())
                .setType(suffix)
                .setData(file.getBytes())
                .setSize(file.getSize());
        ChartFile entity = chartFileRepository.save(chartFile);
        return ResponseEntity.ok(entity.getId());
    }

    /**
     * 展示图片
     */
    @GetMapping("/{id}/preview")
    public void showImage(HttpServletResponse response, @PathVariable Integer id) throws IOException {
        try (ServletOutputStream os = response.getOutputStream()) {
            ChartFile chartFile = chartFileRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("没有此图片"));
            response.setContentType("image/" + chartFile.getType());
            os.write(chartFile.getData());
            os.flush();
        }
    }

    private String checkSize(MultipartFile file) {
        String filename = file.getOriginalFilename();
        int i = filename.lastIndexOf(".");
        String suffix = filename.substring(i + 1, filename.length()).toLowerCase();
        String s = imgSuffix.contains(suffix) ? "图片" : "文件";
        if (file.getSize() > 1024 * 1024 * 5) {
            throw new RuntimeException(s + "大小不能超过5MB");
        }
        return suffix;
    }
}
