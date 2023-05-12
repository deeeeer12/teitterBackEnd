package com.twitter.twitterplusp.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Component
@Slf4j
public class Upload {

    public static final String videoPath = "/teitterfile/videos/";

    public static final String imagePath = "/teitterfile/images/";

    public static final String MY_URL = "https://www.heron.love:8888";

    /**
     * 上传文件返回URL
     */
    public static String uplods(MultipartFile file) throws IOException {
        //file是一个临时文件，需要抓存到指定位置，否则本次请求完成后临时文件将消失
        //原始文件名
        String originalFilename = file.getOriginalFilename();
        //获取文件后缀
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));

        String fileName = UUID.randomUUID().toString()+suffix;

        if (".webp".equals(suffix)){
            File dir = new File(imagePath);
//            File dir = new File(IMAGE_PATH);
            if (!dir.exists()) {
                //不存在，创建这个文件夹
                dir.mkdirs();
            }

            try {
                //将临时文件转存到指定位置
                file.transferTo(new File(imagePath + fileName));
            } catch (IOException e) {
                e.printStackTrace();
            }

            log.info(fileName);
            return MY_URL+imagePath+fileName;
        }

        File dir = new File(videoPath);
//        File dir = new File(VIDEO_PATH);
        if (!dir.exists()) {
            //不存在，创建这个文件夹
            dir.mkdirs();
        }

        try {
            //将临时文件转存到指定位置
            file.transferTo(new File(videoPath + fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }

        log.info(fileName);
        return MY_URL+videoPath+fileName;
    }
}
