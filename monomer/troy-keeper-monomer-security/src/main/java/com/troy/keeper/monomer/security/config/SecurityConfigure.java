package com.troy.keeper.monomer.security.config;

import com.troy.keeper.core.config.MonomerSecurityConfiguration;
import com.troy.keeper.monomer.security.authority.DomainUserDetailsService;
import com.troy.keeper.monomer.security.authority.PersistentTokenRememberMeServices;
import com.troy.keeper.monomer.security.repository.PersistentTokenRepository;
import com.troy.keeper.monomer.security.repository.UserRepository;
import io.github.jhipster.config.JHipsterProperties;
import io.github.jhipster.security.AjaxAuthenticationFailureHandler;
import io.github.jhipster.security.AjaxAuthenticationSuccessHandler;
import io.github.jhipster.security.AjaxLogoutSuccessHandler;
import io.github.jhipster.security.Http401UnauthorizedEntryPoint;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * Created with IntelliJ IDEA.
 * User: xiongzhan
 * Date: 17-5-22
 * Time: 下午3:14
 * To change this template use File | Settings | File Templates.
 */
@Configuration
public class SecurityConfigure extends MonomerSecurityConfiguration {

    private JHipsterProperties jHipsterProperties;

    private PersistentTokenRepository persistentTokenRepository;

    private UserRepository userRepository;


    public SecurityConfigure(JHipsterProperties jHipsterProperties, PersistentTokenRepository persistentTokenRepository, UserRepository userRepository) {
        this.jHipsterProperties = jHipsterProperties;
        this.persistentTokenRepository = persistentTokenRepository;
        this.userRepository =userRepository;
    }



    @Bean
    @ConditionalOnMissingBean(UserDetailsService.class)
    public UserDetailsService userDetailsService(UserRepository userRepository) {
        return new DomainUserDetailsService(userRepository);
    }

    @Bean
    public AjaxAuthenticationSuccessHandler ajaxAuthenticationSuccessHandler() {
        return new AjaxAuthenticationSuccessHandler();
    }


    @Bean
    public AjaxAuthenticationFailureHandler ajaxAuthenticationFailureHandler() {
        return new AjaxAuthenticationFailureHandler();
    }

    @Bean
    public AjaxLogoutSuccessHandler ajaxLogoutSuccessHandler() {
        return new AjaxLogoutSuccessHandler();
    }

    @Bean
    public Http401UnauthorizedEntryPoint http401UnauthorizedEntryPoint() {
        return new Http401UnauthorizedEntryPoint();
    }


    @Override
    public void configure(HttpSecurity http) throws Exception {
        PersistentTokenRememberMeServices persistentTokenRememberMeServices = new PersistentTokenRememberMeServices(jHipsterProperties, userDetailsService(userRepository), persistentTokenRepository, userRepository);
        persistentTokenRememberMeServices.setParameter("remember-me");
        http.csrf().disable()
                .rememberMe()
                    .rememberMeServices(persistentTokenRememberMeServices)
                    .rememberMeParameter("remember-me")
                    .key(jHipsterProperties.getSecurity().getRememberMe().getKey());
        System.out.println("monomer-security configure");
    }
}
