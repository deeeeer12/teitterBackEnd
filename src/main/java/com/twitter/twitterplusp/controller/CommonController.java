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
@RequestMapping("/teitter/api")
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

//    @GetMapping("/download")
//    public void download(String name, HttpServletResponse response) {
//        //输入流，通过输入流读取文件内容
//        try {
//            FileInputStream fis = new FileInputStream(new File(avatarPath + name));
//
//            //输出流，通过输出流将文件写回浏览器，在浏览器展示图片
//            ServletOutputStream os = response.getOutputStream();
//
//            response.setContentType("image/jpeg");
//
//            int len = 0;
//            byte[] buffer = new byte[1024];
//            while ((len = fis.read(buffer)) != -1) {
//                os.write(buffer, 0, len);
//                os.flush();
//            }
//
//            //关闭资源
//            os.close();
//            fis.close();
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//
//    }

}
