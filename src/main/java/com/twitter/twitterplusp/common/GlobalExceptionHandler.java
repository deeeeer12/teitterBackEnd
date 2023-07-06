package com.twitter.twitterplusp.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import java.lang.ClassCastException;

import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.SQLSyntaxErrorException;

/**
 * 全局異常處理
 * 只要任何一个类上加了下面这两个注解，就会被我们的这个处理
 */
@ControllerAdvice(annotations = {RestController.class, Controller.class})
@ResponseBody
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 异常处理方法
     * @return
     */
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public R<String> exceptionHandler(SQLIntegrityConstraintViolationException ex){
        log.error(ex.getMessage());
        if(ex.getMessage().contains("Duplicate entry")){
            String[] split = ex.getMessage().split(" ");
            String msg = split[2]+"已存在";
            return R.error(msg);
        }
        return R.error("未知错误");
    }

    @ExceptionHandler(SQLSyntaxErrorException.class)
    public R<String> exceptionHandler(SQLSyntaxErrorException ex){
        log.error(ex.getMessage());
        if(ex.getMessage().contains("You have")){
            String msg = "SQL语法错误";
            return R.error(msg);
        }
        return R.error("未知错误");
    }

    @ExceptionHandler(ClassCastException.class)
    public R<String> exceptionHandler(ClassCastException ex){
        log.error(ex.getMessage());
        if(ex.getMessage().contains("org.springframework")){
            String msg = "登录解锁更多功能~";
            return R.error(msg);
        }
        return R.error("未知错误");
    }

    /**
     * 异常处理方法
     * @return
     */
    @ExceptionHandler(CustomException.class)
    public R<String> exceptionHandler(CustomException ex){
        log.error(ex.getMessage());
        return R.error(ex.getMessage());
    }

    @ExceptionHandler(NullPointerException.class)
    public R<String> exceptionHandler(NullPointerException ex){
        if (ex.getMessage().contains("Closing session")){
            String msg = "webSocket空指针异常";
            return R.error(msg);
        }
        return R.error("未知错误");
    }

}
