package com.troy.keeper.core.config;

import com.troy.keeper.core.base.service.RedisService;
import com.troy.keeper.core.base.service.impl.RedisServiceImpl;
import com.troy.keeper.core.client.OAuth2InterceptedFeignConfiguration;
import com.troy.keeper.core.filter.UserDetailDecideFilter;
import com.troy.keeper.core.security.AgentUserDetailServiceImpl;
import com.troy.keeper.core.security.SpringSecurityAuditorAware;
import com.troy.keeper.core.utils.RedisUtils;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.*;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Created with IntelliJ IDEA.
 * User: xiongzhan
 * Date: 17-3-29
 * Time: 上午11:04
 * To change this template use File | Settings | File Templates.
 */
@Configuration
@ComponentScan(
        basePackages = "com.troy",
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = OAuth2InterceptedFeignConfiguration.class)
)
@Import({AsyncConfiguration.class, CacheConfiguration.class,RestTemplateConfiguration.class, CloudDatabaseConfiguration.class, DatabaseConfiguration.class,
        DateTimeFormatConfiguration.class, FeignConfiguration.class, LocaleConfiguration.class , LoggingAspectConfiguration.class,
        MetricsConfiguration.class, SessionConfiguration.class,WebConfigurer.class})
@EnableConfigurationProperties({LiquibaseProperties.class, CloudProperties.class,JwtProperties.class})
public class BeanConfiguration {

    @Bean
    public SpringSecurityAuditorAware springSecurityAuditorAware() {
        return new SpringSecurityAuditorAware();
    }

    @Bean
    public RedisUtils redisUtils(){
        return new RedisUtils();
    }

    @Bean
    public RedisService redisService(){
        return new RedisServiceImpl();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailDecideFilter userDetailDecideFilter(){
        return new UserDetailDecideFilter();
    }

    @Bean("userDetailsService")
    public UserDetailsService agentUserDetailService(){
        return new AgentUserDetailServiceImpl();
    }
}