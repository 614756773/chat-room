package cn.hotpot.chatroom.controller;

import cn.hotpot.chatroom.dao.entity.ChatFile;
import cn.hotpot.chatroom.dao.repository.ChatFileRepository;
import cn.hotpot.chatroom.model.vo.FileUploadResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author qinzhu
 * @since 2019/12/25
 */
@RestController
@RequestMapping("/file")
@Api(tags = "文件")
public class FileController {

    private static Set<String> imgSuffix = new HashSet<>(Arrays.asList("png", "jpg", "jpge"));

    @Autowired
    private ChatFileRepository chatFileRepository;

    @PostMapping("/upload")
    @ApiOperation("上传文件")
    public ResponseEntity<FileUploadResultVO> upload(MultipartFile file) throws IOException {
        String suffix = checkSize(file);
        ChatFile chatFile = new ChatFile()
                .setName(file.getOriginalFilename())
                .setType(suffix)
                .setData(file.getBytes())
                .setSize(file.getSize());
        ChatFile entity = chatFileRepository.save(chatFile);
        return ResponseEntity.ok(new FileUploadResultVO(entity.getId(), entity.getName(), entity.getSize()));
    }

    /**
     * 下载文件
     */
    @GetMapping("/{id}")
    @ApiOperation("下载文件")
    public void download(HttpServletResponse response, @PathVariable Integer id) throws IOException {
        try (ServletOutputStream os = response.getOutputStream()) {
            ChatFile chatFile = chatFileRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("没有此图片"));
            String fileName = URLEncoder.encode(chatFile.getName(), "UTF-8");
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment;filename=\"" + fileName + "\"");
            os.write(chatFile.getData());
            os.flush();
        }
    }

    /**
     * 展示图片
     */
    @GetMapping("/{id}/preview")
    @ApiOperation("展示图片")
    public void showImage(HttpServletResponse response, @PathVariable Integer id) throws IOException {
        try (ServletOutputStream os = response.getOutputStream()) {
            ChatFile chatFile = chatFileRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("没有此图片"));
            response.setContentType("image/" + chatFile.getType());
            os.write(chatFile.getData());
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
