package com.troy.keeper.core.config;

import com.troy.keeper.core.filter.UserDetailDecideFilter;
import com.troy.keeper.core.security.AuthoritiesConstants;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.data.repository.query.SecurityEvaluationContextExtension;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.web.filter.CorsFilter;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;


@Configuration
@ConditionalOnMissingClass("org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter")
public class MonomerConfiguration {

    @Configuration
    @EnableWebSecurity
    @Order(101)
    @EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
    public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

        @Autowired(required = false)
        private HttpSecurityConfigure securityConfigure;

        @Autowired(required = false)
        private MonomerSecurityConfiguration monomerSecurityConfiguration;

        private final AuthenticationManagerBuilder authenticationManagerBuilder;

        @Autowired
        @Qualifier("userDetailsService")
        private UserDetailsService userDetailsService;

        @Autowired
        private PasswordEncoder passwordEncoder;

        private final CorsFilter corsFilter;

        public SecurityConfiguration(AuthenticationManagerBuilder authenticationManagerBuilder,
                                     CorsFilter corsFilter) {
            this.authenticationManagerBuilder = authenticationManagerBuilder;
//            this.userDetailsService = userDetailsService;
            this.corsFilter = corsFilter;
        }

        @PostConstruct
        public void init() {
            try {
                authenticationManagerBuilder
                        .userDetailsService(userDetailsService)
                        .passwordEncoder(passwordEncoder);
            } catch (Exception e) {
                throw new BeanInitializationException("Security configuration failed", e);
            }
        }

        @Autowired
        @Qualifier("http401UnauthorizedEntryPoint")
        AuthenticationEntryPoint authenticationEntryPoint;

        @Autowired
        @Qualifier("ajaxAuthenticationSuccessHandler")
        AuthenticationSuccessHandler authenticationSuccessHandler;

        @Autowired
        @Qualifier("ajaxAuthenticationFailureHandler")
        AuthenticationFailureHandler authenticationFailureHandler;

        @Autowired
        @Qualifier("ajaxLogoutSuccessHandler")
        LogoutSuccessHandler logoutSuccessHandler;

        @Autowired
        UserDetailDecideFilter userDetailDecideFilter;





        @Override
        public void configure(WebSecurity web) throws Exception {
            web.ignoring()
                    .antMatchers(HttpMethod.OPTIONS, "/**")
                    .antMatchers("/app/**/*.{js,html}")
                    .antMatchers("/bower_components/**")
                    .antMatchers("/i18n/**")
                    .antMatchers("/content/**")
                    .antMatchers("/swagger-ui/index.html")
                    .antMatchers("/test/**")
                    .antMatchers("/h2-console/**");
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http
                    .csrf()
                    .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                    .and()
                    .addFilterBefore(corsFilter, UsernamePasswordAuthenticationFilter.class)
                    .addFilterBefore(userDetailDecideFilter,UsernamePasswordAuthenticationFilter.class)
                    .exceptionHandling()
                    .authenticationEntryPoint(authenticationEntryPoint)
                    .and()
                    .formLogin()
                    .loginProcessingUrl("/api/system/login/doLogin")
                    .successHandler(authenticationSuccessHandler)
                    .failureHandler(authenticationFailureHandler)
                    .usernameParameter("loginName")
                    .passwordParameter("password")
                    .permitAll()
                    .and()
                    .logout()
                    .logoutUrl("/api/system/login/loginOut")
                    .logoutSuccessHandler(logoutSuccessHandler)
                    .permitAll()
                    .and()
                    .headers()
                    .frameOptions()
                    .disable()
                    .and()
                    .authorizeRequests()
                    .antMatchers("/api/register").permitAll()
                    .antMatchers("/api/activate").permitAll()
                    .antMatchers("/api/authenticate").permitAll()
                    .antMatchers("/api/account/reset_password/init").permitAll()
                    .antMatchers("/api/account/reset_password/finish").permitAll()
                    .antMatchers("/api/profile-info").permitAll()
                    .antMatchers("/api/system/login/getCode").permitAll()
                    .antMatchers("/api/system/login/checkCode").permitAll()
                    .antMatchers("/management/health").permitAll()
                    .antMatchers("/management/**").hasAuthority(AuthoritiesConstants.ADMIN)
                    .antMatchers("/v2/api-docs/**").permitAll()
                    .antMatchers("/swagger-resources/configuration/ui").permitAll()
                    .antMatchers("/swagger-ui/index.html").hasAuthority(AuthoritiesConstants.ADMIN);

            if (null!=securityConfigure) {
                securityConfigure.configure(http);
            }

            if (null != monomerSecurityConfiguration){
                monomerSecurityConfiguration.configure(http);
            }
            http.authorizeRequests().antMatchers("/api/**").authenticated();
        }

        @Bean
        public SecurityEvaluationContextExtension securityEvaluationContextExtension() {
            return new SecurityEvaluationContextExtension();
        }
    }
}
