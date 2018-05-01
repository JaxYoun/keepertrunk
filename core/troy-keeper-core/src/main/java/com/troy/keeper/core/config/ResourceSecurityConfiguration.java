package com.troy.keeper.core.config;

import com.troy.keeper.core.filter.MutableHttpServletFilter;
import com.troy.keeper.core.security.AuthoritiesConstants;
import io.github.jhipster.config.JHipsterProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;
import org.springframework.security.web.header.HeaderWriterFilter;

/**
 * Created with IntelliJ IDEA.
 * User: xiongzhan
 * Date: 17-4-18
 * Time: 下午1:23
 * To change this template use File | Settings | File Templates.
 */
@Configuration
@ConditionalOnClass(ResourceServerConfigurer.class)
//@Profile("!"+ Constants.SPRING_PROFILE_PERFORMANCE)
public class ResourceSecurityConfiguration {

    @Autowired(required = false)
    private HttpSecurityConfigure securityConfigure;

    @Configuration
    @EnableResourceServer
    @EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
    public class MicroserviceSecurityConfiguration extends ResourceServerConfigurerAdapter {

        private final JHipsterProperties jHipsterProperties;

        private final DiscoveryClient discoveryClient;

        private final Environment env;



        public MicroserviceSecurityConfiguration(Environment env, JHipsterProperties jHipsterProperties,
                                                 DiscoveryClient discoveryClient) {
            this.env = env;
            this.jHipsterProperties = jHipsterProperties;
            this.discoveryClient = discoveryClient;
        }

        @Override
        public void configure(HttpSecurity http) throws Exception {

            http
                    .csrf()
                    .disable()
                    .addFilterBefore(mutableHttpServletFilter(), HeaderWriterFilter.class)
                    .headers()
                    .frameOptions()
                    .disable()
                    .and()
                    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                    .and()
                    .authorizeRequests()
                    .antMatchers("/api/profile-info").permitAll()
//                    .antMatchers("/api/**").authenticated()
                    .antMatchers("/management/health").permitAll()
                    .antMatchers("/management/**").hasAuthority(AuthoritiesConstants.ADMIN)
                    .antMatchers("/swagger-resources/configuration/ui").permitAll();
            if (null!=securityConfigure) {
                securityConfigure.configure(http);
            }
        }

    }


    /**
     * Apply the token converter (and enhander) for token store.
     */
    @Bean
    public TokenStore tokenStore(final RedisConnectionFactory connectionFactory) {
        return new RedisTokenStore(connectionFactory);
    }

    @Autowired
    private RedisTemplate redisTemplate;

    @Bean
    public MutableHttpServletFilter mutableHttpServletFilter() {
//         return new MutableHttpServletFilter(redisTemplate);
         return new MutableHttpServletFilter();
    }

}
