package com.troy.keeper.system.security.MethodAuthority;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * * * * * * * * * * * * *
 * 方法权限拦截器配置类
 * * * * * * * * * * * * *
 * Class Name:MethodInterceptorConfig
 *
 * @author SimonChu By Troy
 * @create 2017-10-26 16:58
 **/
@Configuration
public class MethodInterceptorConfig extends WebMvcConfigurerAdapter {

    @Bean
    public MethodAuthorityInterceptor getMethodAuthorityInterceptor(){
        return new MethodAuthorityInterceptor();
    }

    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(getMethodAuthorityInterceptor()).addPathPatterns("/api/**");
    }

}
