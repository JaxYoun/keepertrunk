package com.troy.keeper.activiti.config;

import com.troy.keeper.activiti.filter.PermitFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * description:请求地址过滤 <br/>
 * Date:     2017/6/30 15:03<br/>
 *
 * @author work_tl
 * @see
 * @since JDK 1.8
 */
@Configuration
public class PermitWebConfig extends WebMvcConfigurerAdapter {
    @Bean
    public FilterRegistrationBean getPermitFilter(){
        PermitFilter permitFilter = new PermitFilter();
        FilterRegistrationBean registrationBean=new FilterRegistrationBean();
        registrationBean.setFilter(permitFilter);
        List<String> urlPatterns=new ArrayList<String>();
        urlPatterns.add("/*");//拦截路径，可以添加多个
        registrationBean.setUrlPatterns(urlPatterns);
        registrationBean.setOrder(1133);
        return registrationBean;
    }
}
