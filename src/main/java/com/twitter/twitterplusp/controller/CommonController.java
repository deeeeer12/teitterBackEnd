package com.twitter.twitterplusp.controller;

import com.twitter.twitterplusp.common.R;
import com.twitter.twitterplusp.utils.Upload;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/teitter/v2/api")
public class CommonController {

    public static final String BASE_URL = "https://www.heron.love:8888";

    /**
     * 文件上传
     *
     * @param file
     * @return
     */
    @PostMapping("/upload")
    public R upload(MultipartFile file) {

        String fileUrl = null;
        try {
            fileUrl = Upload.uplods(file);
        } catch (IOException e) {
            R result = new R(400,"上传失败",null,null);
            return result;
        }

        R result = new R(200,"上传成功",fileUrl,null);

        return result;
    }


}
