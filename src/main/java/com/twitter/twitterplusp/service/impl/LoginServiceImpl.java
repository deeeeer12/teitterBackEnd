package com.twitter.twitterplusp.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.twitter.twitterplusp.common.R;
import com.twitter.twitterplusp.entity.LoginUser;
import com.twitter.twitterplusp.entity.User;
import com.twitter.twitterplusp.mapper.LoginMapper;
import com.twitter.twitterplusp.service.LoginService;
import com.twitter.twitterplusp.utils.GetLoginUserInfo;
import com.twitter.twitterplusp.utils.JwtUtil;
import com.twitter.twitterplusp.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
public class LoginServiceImpl extends ServiceImpl<LoginMapper, User> implements LoginService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RedisCache redisCache;

    /**
     * 获取AuthenticationManager进行用户认证
     *
     * 如果认证未通过，给出对应的提示
     *
     * 如果认证通过了，使用userid生成一个JWT  JWT存入ResponseResult返回
     *
     * 把完整的用户信息存入redis userid作为key
     * @param user
     * @return
     */
    @Override
    public R login(User user) {
        //这一步相当于把用户登录时的用户名和密码封装成一个Authentication对象
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(user.getUserName(),user.getUserPassword());
        //把封装好的Authentication对象传入AuthenticationManager，让他帮我们进行认证操作，他会调用我们定义的UserDetails方法进行校验。
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);

        //若authenticate为null则表示认证未通过
        if (Objects.isNull(authenticate)){
            throw new RuntimeException("登陆失败");
        }

        //如果认证通过了，使用userId生成一个JWT，JWT存入R返回
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        String userId = loginUser.getUser().getUid().toString();
        //使用JWTUtil生成jwt
        String jwt = JwtUtil.createJWT(userId);

        //把完整的用户信息存入redis userId作为key
        redisCache.setCacheObject("login:"+userId,loginUser);
        Map<String,String> map = new HashMap<>();
        map.put("token",jwt);
        //打包信息发送给前端
        return new R<>(200,"登录成功",loginUser.getUser(),map);

    }

    /**
     * 注销当前登录的用户
     * @return
     */
    @Override
    public R logout() {
        //获取SecurityContextHolder中的用户id
        LoginUser loginUser = GetLoginUserInfo.getLoginUser();
        Long userId = loginUser.getUser().getUid();

        //删除redis中缓存的用户信息
        redisCache.deleteObject("login:"+userId);
        return new R(200,"注销成功",null,null);
    }
}
