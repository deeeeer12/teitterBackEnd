package com.twitter.twitterplus.utils;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class FileUtil {

    public static final String UPLOAD_PATH = "/images/";

    /**
     * 上传文件返回URL
     */
    public static String uplods(MultipartFile file,String filePrefix) throws IOException {
        //判断文件大小
        //TODO

        //文件后缀
        final String fileSuffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf('.')+1);

        //文件名
        String fileName = filePrefix +"."+fileSuffix;

        //文件写入
        File descFile = new File(UPLOAD_PATH, fileName);
        //转换一下，可以快速保存到本地
        file.transferTo(descFile);

        //文件URL
        String url =UPLOAD_PATH+fileName;
        return url;
    }

}
