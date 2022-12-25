package com.twitter.twitterplusp.handler;

import com.alibaba.fastjson.JSON;
import com.twitter.twitterplusp.common.R;
import com.twitter.twitterplusp.utils.WebUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AccessDeniedHandlerImpl implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException, ServletException {
        R result = new R(HttpStatus.FORBIDDEN.value(), "您的权限不足",null,null);
        String json = JSON.toJSONString(result);
        WebUtils.renderString(httpServletResponse,json);
    }
}
