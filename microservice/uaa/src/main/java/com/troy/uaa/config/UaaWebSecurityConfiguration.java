package com.troy.uaa.config;


import com.troy.keeper.core.config.CloudProperties;
import com.troy.uaa.security.AuthenticationSuccessHandler;
import com.troy.uaa.security.AwareAuthenticationEntryPoint;
import com.troy.uaa.security.LogoutSuccessHandler;
import com.troy.uaa.security.oauth2.Oauth2TokenService;
import com.troy.uaa.service.LogsearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.data.repository.query.SecurityEvaluationContextExtension;

import javax.annotation.PostConstruct;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class UaaWebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;

    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    private final CloudProperties cloudProperties;

    public UaaWebSecurityConfiguration(UserDetailsService userDetailsService, AuthenticationManagerBuilder authenticationManagerBuilder, CloudProperties cloudProperties) {
        this.userDetailsService = userDetailsService;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.cloudProperties = cloudProperties;
    }

    @PostConstruct
    public void init() throws Exception {
        authenticationManagerBuilder
            .userDetailsService(userDetailsService)
            .passwordEncoder(passwordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
            .antMatchers(HttpMethod.OPTIONS, "/**")
//            .antMatchers("/**")
            .antMatchers("/app/**/*.{js,html}")
            .antMatchers("/bower_components/**")
            .antMatchers("/i18n/**")
            .antMatchers("/content/**")
            .antMatchers("/swagger-ui/index.html")
            .antMatchers("/test/**")
            .antMatchers("/h2-console/**");
    }

    @Bean
    public SecurityEvaluationContextExtension securityEvaluationContextExtension() {
        return new SecurityEvaluationContextExtension();
    }


    @Bean
    public LogoutSuccessHandler logoutSuccessHandler() {
        return new LogoutSuccessHandler(logsearchService);
    }

    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return new AuthenticationSuccessHandler(oauth2TokenService, cloudProperties,logsearchService);
    }

    @Bean
    public AwareAuthenticationEntryPoint authenticationEntryPoint() {
        return new AwareAuthenticationEntryPoint(cloudProperties);
    }

    @Autowired
    Oauth2TokenService oauth2TokenService;

    @Autowired
    LogsearchService logsearchService;

}
