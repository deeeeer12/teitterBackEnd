package com.twitter.twitterplusp.controller;

import com.twitter.twitterplusp.common.R;
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
@RequestMapping("/teitter")
public class CommonController {

    @Value("${user.avatar.path}")
    private String avatarPath;

    /**
     * 文件上传
     *
     * @param file
     * @return
     */
    @PostMapping("/upload")
    public R<String> upload(MultipartFile file) {
        //file是一个临时文件，需要抓存到指定位置，否则本次请求完成后临时文件将消失
        log.info(file.toString());
        //原始文件名
        String originalFilename = file.getOriginalFilename();
        //获取文件后缀
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));

        String fileName = UUID.randomUUID().toString() + suffix;

        File dir = new File(avatarPath);
        if (!dir.exists()) {
            //不存在，创建这个文件夹
            dir.mkdirs();
        }

        try {
            //将临时文件转存到指定位置
            file.transferTo(new File(avatarPath + fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return R.success(null,fileName);
    }

    @GetMapping("/download")
    public void download(String name, HttpServletResponse response) {
        //输入流，通过输入流读取文件内容
        try {
            FileInputStream fis = new FileInputStream(new File(avatarPath + name));

            //输出流，通过输出流将文件写回浏览器，在浏览器展示图片
            ServletOutputStream os = response.getOutputStream();

            response.setContentType("image/jpeg");

            int len = 0;
            byte[] buffer = new byte[1024];
            while ((len = fis.read(buffer)) != -1) {
                os.write(buffer, 0, len);
                os.flush();
            }

            //关闭资源
            os.close();
            fis.close();

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

}
