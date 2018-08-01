package com.atdtl.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.Base64Utils;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 文件上传的几种方式： 单文件上传、多文件上传、BASE64编码
 *
 * @author Administrator
 * @since 2018/7/26 15:54
 */
@RequestMapping("/uploads")
@Controller
public class FileUploadController {
    public static final Logger log = LoggerFactory.getLogger(FileUploadController.class);

    @GetMapping("/fileupoload")
    public String fileupload(){
        return "fileupload";
    }

    /**
     * 单文件上传
     * @param file
     * @return
     * @throws IOException
     */
    @PostMapping("/simpleUpload")
    @ResponseBody
    public Map<String, Object> simpleUpload(@RequestParam("simpleFile")MultipartFile file) throws IOException {
        log.info("[文件名称1] - [{}]", file.getName());
        log.info("[文件名称2] - [{}]", file.getOriginalFilename());
        log.info("[文件类型] - [{}]", file.getContentType());
        log.info("[文件大小] - [{}]", file.getSize());

        // TODO: 2018/7/26 将文件写到指定目录（实际开发中，有可能是将文件写到云存储/或者指定目录通过Nginx进行gzip压缩和反向代理）
        file.transferTo(new File("F:\\springboot\\" + file.getOriginalFilename()));
        Map<String, Object> result = new HashMap<>();
        result.put("", file.getContentType());
        result.put("", file.getOriginalFilename());
        result.put("", file.getSize() + "");
        return result;
    }

    @PostMapping("/multiUpload")
    @ResponseBody
    public List<Map<String, String>> multiUpload(@RequestParam("file")MultipartFile[] files) throws IOException {
        if (files == null || files.length == 0){
            return null;
        }
        List<Map<String, String >> result = new ArrayList<>();
        for (MultipartFile file : files) {
            // TODO: 2018/7/26 MVC提供的写入方式
            file.transferTo(new File("F:\\springboot\\" + file.getOriginalFilename()));
            Map<String, String> map = new HashMap<>();
            map.put("", file.getContentType());
            map.put("", file.getOriginalFilename());
            map.put("", file.getSize() + "");
            result.add(map);
        }

        return result;
    }

    @PostMapping("/base64Upload")
    @ResponseBody
    public void base64Upload(String base64) throws IOException {
        // TODO: 2018/7/26 BASE64 方式的格式和名字需要自己控制（如：png图片编码后的前缀就会是 data:image/png;base64） 
        final File tempFile = new File("F:\\springboot\\test.jpg");
        // TODO: 2018/7/26 防止有的传了 data:image/png;base64，有的没传的情况
        String[] d = base64.split("base64,");
        final byte[] bytes = Base64Utils.decodeFromString(d.length > 1 ? d[1] : d[0]);
        FileCopyUtils.copy(bytes, tempFile);
    }
}
