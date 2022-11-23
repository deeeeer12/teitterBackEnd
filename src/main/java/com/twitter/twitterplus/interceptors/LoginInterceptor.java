//package com.twitter.twitterplus.interceptors;
//
//import org.springframework.web.servlet.HandlerInterceptor;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import javax.servlet.http.HttpSession;
//
//public class LoginInterceptor implements HandlerInterceptor {
//    @Override
//    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        //得到用户登录时的session
//        HttpSession session = request.getSession();
//        Object loginUser = session.getAttribute("loginUser");
//        if(loginUser != null){
//            //放行
//            return true;
//        }else {
//            //拦截，跳转到登录页面
//            session.setAttribute("message","请先登录");
//            response.sendRedirect("/teitter/login");
//            return false;
//        }
//    }
//}
