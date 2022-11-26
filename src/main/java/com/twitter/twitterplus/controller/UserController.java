package com.twitter.twitterplus.controller;

import com.twitter.twitterplus.bean.User;
import com.twitter.twitterplus.mapper.UserMapper;
import com.twitter.twitterplus.service.UserService;
import com.twitter.twitterplus.utils.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
public class UserController {

    @Autowired
    UserMapper userMapper;

    @Autowired
    UserService userService;



    /**
     * 创建用户，创建成功后返回登录页面
     * @param user
     * @return
     */
    @PostMapping("/teitter/api/RegisterUser")
    public Map saveUser(User user, MultipartFile file,HttpServletRequest request) throws IOException {
        Map<String,Object> map = new HashMap<>();
        String OnUserName = user.getUserName();//将用户名转换为字符串
            String url = FileUtil.uplods(file,OnUserName);
            user.setAvatarUrl(url);
            user.setNickName(request.getParameter("nickName"));
        int result = userMapper.insert(user);
        if(result == 0){
            map.put("message","注册用户失败");
            map.put("status","400");
        }else {
            map.put("message","注册用户成功");
            map.put("status","200");
            map.put("avatarUrl",url);
        }
        return map;
    }


    /**
     * 用户登录验证
     *
     *
     * @param
     * @return
     */
    @GetMapping("/teitter/api/login")
    public Map login(String username, String password, HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        User user = userService.login(username, password);

        if (user == null) {
            map.put("message", "登录失败");
            map.put("status", "400");
        } else {
            //向session域中保存用户信息，以供发布推文的时候从session域中取出使用
            request.getSession().setAttribute("userName",user.getUserName());
            request.getSession().setAttribute("nickName",user.getNickName());
            request.getSession().setAttribute("avatarUrl",user.getAvatarUrl());
            request.getSession().setAttribute("userInfo", user);
            map.put("message", "登录成功");
            map.put("status", "200");
            //返回用户登录成功后，用户的信息
            map.put("userInfo", request.getSession().getAttribute("userInfo"));
        }
        return map;
    }

    @PostMapping("/teitter/api/logout")
    public Map logout(HttpServletRequest request){
        Map<String,Object> map = new HashMap<>();
        //false代表：不创建session对象，只是从request中获取。
        HttpSession session = request.getSession(false);
        if(session.getAttribute("userInfo") == null){
            map.put("status","400");
            map.put("message","登出失败！");
            return map;
        }else{
            session.removeAttribute("userInfo");
            map.put("status",200);
            map.put("message","登出成功！");
            return map;
        }

    }

}
