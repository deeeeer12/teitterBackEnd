package com.twitter.twitterplusp.filter;

import com.twitter.twitterplusp.entity.LoginUser;
import com.twitter.twitterplusp.utils.JwtUtil;
import com.twitter.twitterplusp.utils.RedisCache;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {
    @Autowired
    private RedisCache redisCache;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //获取TOKEN
        String token1 = request.getHeader("Authorization");
        String token2 = request.getHeader("Sec-Websocket-Protocol");

        if (token1==null && token2==null){
            //没有token，给他放行
            filterChain.doFilter(request,response);
            return;
        }

        if (StringUtils.hasText(token1)){
            //解析token
            String userId;
            try {
                Claims claims = JwtUtil.parseJWT(token1);
                userId = claims.getSubject();
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("token非法");
            }
            //从redis中获取用户信息
            String redisKey = "login:"+userId;
            LoginUser loginUser = redisCache.getCacheObject(redisKey);
            if(Objects.isNull(loginUser)){
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
            }
            //存入SecurityContextHolder
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(loginUser,null,loginUser.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);

            //放行
            filterChain.doFilter(request,response);

            //请求头中没有Authorization，那么就用Sec-Websocket-Protocol再查一遍（私信模块使用）
        }else {
            if (StringUtils.hasText(token2)){

                System.out.println("这里到了验证token2");
                System.out.println("这里到了验证token2");
                System.out.println("这里到了验证token2");
                System.out.println("这里到了验证token2");
                System.out.println("这里到了验证token2");


                System.out.println(token2);

                //解析token
                String userId;
                try {
                    Claims claims = JwtUtil.parseJWT(token2);
                    userId = claims.getSubject();
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new RuntimeException("token非法");
                }
                //从redis中获取用户信息
                String redisKey = "login:"+userId;
                LoginUser loginUser = redisCache.getCacheObject(redisKey);
                if(Objects.isNull(loginUser)){
                    response.setStatus(HttpStatus.UNAUTHORIZED.value());
                }
                //存入SecurityContextHolder
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(loginUser,null,loginUser.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);

                //放行
                filterChain.doFilter(request,response);
            }
        }
    }
}
