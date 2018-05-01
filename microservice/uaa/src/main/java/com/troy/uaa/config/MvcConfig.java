package com.troy.uaa.config;

import com.troy.uaa.web.rest.filter.AuthorizationLoginFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

/**
 * Created with IntelliJ IDEA.
 * User: xiongzhan
 * Date: 17-4-21
 * Time: 上午10:50
 * To change this template use File | Settings | File Templates.
 */
@Configuration
public class MvcConfig extends WebMvcConfigurerAdapter {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/login").setViewName("login");
    }

    @Bean
    public AuthorizationLoginFilter authorizationLoginFilter() {
        return new AuthorizationLoginFilter();
    }

}
