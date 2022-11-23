package com.twitter.twitterplus.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.twitter.twitterplus.bean.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserService extends IService<User> {

    User login(String username,String password);



}
