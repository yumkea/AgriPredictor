package com.agripredictor.ossservice.controller;


import com.agripredictor.ossservice.config.DirPathConfig;
import com.agripredictor.ossservice.po.Result;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;


/**
 * 对象存储类
 */
@RestController
@RequestMapping("/oss")
@Slf4j
@RequiredArgsConstructor
public class OSSController {

    private final DirPathConfig dirPathConfig;

    /**
     * 文件上传
     * 可上传任意文件  大小不超过100mb
     * 我们服务器资源有限尽量不要上传视频
     * @param file
     * @return
     */
    @PostMapping("/upload")
    public Result upload(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            log.error("文件为空");
            return Result.error("file is empty");
        }
        String fileName = UUID.randomUUID() + "__" + file.getOriginalFilename();
        String path = dirPathConfig.getDirPath() + fileName;
        try {
            log.info("文件上传；{}", path);
            file.transferTo(new File(path));
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("upload failed");
        }
        return Result.success(fileName);
    }


    /**
     * 文件展示
     * 支持 jpg jpeg png gif mp4 mp3
     * 我们服务器资源有限尽量不要上传视频
     * @param filename
     * @param response
     * @throws IOException
     */
    @GetMapping("/download")
    public void download(@RequestParam String filename, HttpServletResponse response) throws IOException {
        String Dir = dirPathConfig.getDirPath();
        File file = new File(Dir + filename);
        FileInputStream fis;
        fis = new FileInputStream(file);
        long size = file.length();
        byte[] data = new byte[(int) size];
        fis.read(data, 0, (int) size);
        fis.close();
        String[] split = filename.split("\\.");

        switch (split[split.length - 1]) {
            case "jpg":
                response.setContentType("image/jpeg");
                break;
            case "jpeg":
                response.setContentType("image/jpeg");
                break;
            case "png":
                response.setContentType("image/png");
                break;
            case "gif":
                response.setContentType("image/gif");
                break;
            case "mp4":
                response.setContentType("video/mp4");
            case "mp3":
                response.setContentType("audio/mpeg");
        }
        log.info("文件获取 {}", file.getName());
        OutputStream out = response.getOutputStream();
        out.write(data);
        out.flush();
        out.close();
    }


}
