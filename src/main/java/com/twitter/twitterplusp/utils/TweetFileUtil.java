package com.twitter.twitterplusp.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Component
public class TweetFileUtil {
    public static final String USER_AVATAR_UPLOAD_PATH = "https://www.heron.love:8888/images/avatarFile/";

    public static final String LOCAL_PATH = "D:\\a_images\\";


    public static final String TWEET_IMAGES_UPLOAD_PATH =  "/images/tweetFile/";

    /**
     * 上传文件返回URL
     */
    public static String uplods(MultipartFile file, String filePrefix) throws IOException {
        //判断文件大小
        //TODO

        //文件后缀
        final String fileSuffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf('.')+1);

        //文件名
        String fileName = filePrefix +"."+fileSuffix;

        //文件写入
        File descFile = new File(TWEET_IMAGES_UPLOAD_PATH, fileName);
        //转换一下，可以快速保存到本地
        file.transferTo(descFile);

        //文件URL
        String url =TWEET_IMAGES_UPLOAD_PATH+fileName;
        return url;
    }
}
