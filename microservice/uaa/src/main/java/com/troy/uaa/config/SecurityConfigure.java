package com.troy.uaa.config;

import com.troy.keeper.core.config.HttpSecurityConfigure;
import com.troy.uaa.web.rest.filter.AuthorizationLoginFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.header.HeaderWriterFilter;
import org.springframework.stereotype.Component;

/**
 * Created with IntelliJ IDEA.
 * User: xiongzhan
 * Date: 17-4-18
 * Time: 下午2:27
 * To change this template use File | Settings | File Templates.
 */
@Component
public class SecurityConfigure implements HttpSecurityConfigure {

    @Autowired
    AuthorizationLoginFilter authorizationLoginFilter;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
            .addFilterBefore(authorizationLoginFilter, HeaderWriterFilter.class)
            .authorizeRequests()
            .antMatchers("/demo/hi").authenticated();
//            .antMatchers("/**").permitAll();
    }
}
