package com.troy.keeper.activiti.security;

import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.stereotype.Component;

/**
* Created by yg on 2017/4/28.
* 1. please access /login to understand how to get the crsf token.
* 2. and also the crsf token will get on the cookie. go to see.
*/
@Component
@Order(112)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
        .antMatchers("/api/actTask/**")
        .antMatchers("/api/actProc/**")
        .antMatchers("/api/actDefine/**")
        .antMatchers("/api/actResource/**")
        .antMatchers("/api/actModel/**")
        .antMatchers("/api/service/**");
    }
}