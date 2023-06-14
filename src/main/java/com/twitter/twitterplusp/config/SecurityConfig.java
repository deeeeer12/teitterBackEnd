package com.twitter.twitterplusp.config;

import com.twitter.twitterplusp.filter.JwtAuthenticationTokenFilter;
import com.twitter.twitterplusp.handler.AccessDeniedHandlerImpl;
import com.twitter.twitterplusp.handler.AuthenticationEntryPointImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;

    @Autowired
    private AccessDeniedHandlerImpl accessDeniedHandler;

    @Autowired
    private AuthenticationEntryPointImpl authenticationEntryPoint;

    /**
     * 暴露Authentication
     *
     * @return
     * @throws Exception
     */
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

//    @Override
//    public void configure(WebSecurity web) throws Exception {
//        web.ignoring().antMatchers("/teitter/api/tweet/getAllTweet/{pageNum}");
//    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
//                //登陆时进入的url-->相当于进入登陆页面
//                .formLogin().loginPage("/teitter/api/user/login")
//                // 告诉spring-security点击登陆时访问的url为/teitter/api/user/login ---->当spring-security接收到此url的请求后,会自动调用
//                // com.nrsc.security.security.action.NRSCDetailsService中的loadUserByUsername
//                // 进行登陆校验
//                .loginProcessingUrl("/teitter/api/tweet/getAllTweet/")
////                //指定使用NRSCAuthenticationSuccessHandler处理登陆成功后的行为
////                .successHandler(NRSCAuthenticationSuccessHandler)
//                //指定使用NNRSCAuthenticationFailureHandler处理登陆失败后的行为
//                .failureHandler(NRSCAuthenticationFailureHandler)
//                .and()
                //关闭csrf
                .csrf().disable()
                //不通过Session获取SecurityContext，因为前后端分离项目用不了session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                //对于登录接口 允许匿名访问
                .antMatchers("/teitter/v2/api/user/login").anonymous()
//                .antMatchers("/teitter/api/intoChat").anonymous()
                //无论登录没登录，都可以访问
                .antMatchers("/teitter/v2/api/user/isLogin").permitAll()
                .antMatchers("/teitter/v2/api/user/regist").permitAll()
                .antMatchers("/teitter/v2/api/tweet/getAllTweet").permitAll()
                .antMatchers("/teitter/v2/api/tweet/getUserTweet/{uid}").permitAll()
                .antMatchers("/teitter/v2/api/comment/getComment/{tweetId}").permitAll()
                .antMatchers("/teitter/v2/api/user/getUserInfo/{uid}").permitAll()
                .antMatchers("/teitter/v2/api//comment/getComment/**").permitAll()
                .antMatchers("/teitter/v2/app/api/**").permitAll()
                .antMatchers("/teitter/v2/api/admin/**").permitAll()
//                .antMatchers("/teitter/api/topic/getTweetsByTopicId").permitAll()
        // 除上面外的所有请求全部需要鉴权认证
                .anyRequest().authenticated();
        //添加过滤器
        http.addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);
        //配置自定义异常处理器
        http.exceptionHandling().authenticationEntryPoint(authenticationEntryPoint)
                .accessDeniedHandler(accessDeniedHandler);
        //允许跨域
        http.cors();
    }

    /**
     * 定义一个方法，能返回BCryptPawwordEncoder就可以
     *
     * @return
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}



