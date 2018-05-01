package com.troy.keeper.core.config;

import com.troy.keeper.core.filter.MutableHttpServletFilter;
import com.troy.keeper.core.filter.UserDetailDecideFilter;
import com.troy.keeper.core.security.AuthoritiesConstants;
import io.github.jhipster.config.JHipsterProperties;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.header.HeaderWriterFilter;
import org.springframework.web.filter.CorsFilter;

import java.security.KeyPair;

/**
 * Created with IntelliJ IDEA.
 * User: xiongzhan
 * Date: 17-4-24
 * Time: 下午3:31
 * To change this template use File | Settings | File Templates.
 */
@Configuration
@ConditionalOnBean(AuthenticationManager.class)
@ConditionalOnClass(AuthorizationServerConfigurerAdapter.class)
public class UaaConfiguration {

    @EnableAuthorizationServer
    public class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {

        @EnableResourceServer
        public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

            private final TokenStore tokenStore;

            private final JHipsterProperties jHipsterProperties;

            private final CloudProperties cloudProperties;

            private final CorsFilter corsFilter;

            private final UserDetailDecideFilter userDetailDecideFilter;

            public ResourceServerConfiguration(TokenStore tokenStore, CorsFilter corsFilter, JHipsterProperties jHipsterProperties, CloudProperties cloudProperties,UserDetailDecideFilter userDetailDecideFilter) {
                this.tokenStore = tokenStore;
                this.jHipsterProperties = jHipsterProperties;
                this.cloudProperties = cloudProperties;
                this.corsFilter = corsFilter;
                this.userDetailDecideFilter = userDetailDecideFilter;
            }

            @Override
            public void configure(HttpSecurity http) throws Exception {
                http
                        .exceptionHandling()
//                        .authenticationEntryPoint(authenticationEntryPoint())
                        .authenticationEntryPoint(authenticationEntryPoint)
                        .and()
                        .formLogin().failureUrl(cloudProperties.getLogin().getFailureUrl())
                        .loginPage(cloudProperties.getLogin().getLoginFormUrl())
                        .successHandler(authenticationSuccessHandler)
                        .permitAll()
                        .and().logout().logoutSuccessHandler(logoutSuccessHandler)
                        .and()
                        .csrf()
                        .disable()
                        .addFilterBefore(corsFilter, UsernamePasswordAuthenticationFilter.class)
                        .addFilterBefore(userDetailDecideFilter, UsernamePasswordAuthenticationFilter.class)
                        .addFilterBefore(mutableHttpServletFilter, HeaderWriterFilter.class)
                        .headers()
                        .frameOptions()
                        .disable()
                        .and()
                        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                        .and()
                        .authorizeRequests()
                        .anyRequest().authenticated()
                        .antMatchers("/api/register").permitAll()
                        .antMatchers("/api/activate").permitAll()
                        .antMatchers("/api/authenticate").permitAll()
                        .antMatchers("/api/account/reset_password/init").permitAll()
                        .antMatchers("/api/account/reset_password/finish").permitAll()
//                        .antMatchers("/login").permitAll()
                        .antMatchers("/v2/api-docs/**").permitAll()
                        .antMatchers("/swagger-ui/index.html").hasAuthority(AuthoritiesConstants.ADMIN);
            }

            @Override
            public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
                resources.resourceId("troy-uaa").tokenStore(tokenStore);
            }

            @Autowired
            AuthenticationEntryPoint authenticationEntryPoint;

            @Autowired
            AuthenticationSuccessHandler authenticationSuccessHandler;

            @Autowired
            LogoutSuccessHandler logoutSuccessHandler;

            @Autowired
            MutableHttpServletFilter mutableHttpServletFilter;

        }

        private final JHipsterProperties jHipsterProperties;

        private final JwtProperties jwtProperties;

        public AuthorizationServerConfiguration(JHipsterProperties jHipsterProperties,JwtProperties jwtProperties) {
            this.jHipsterProperties = jHipsterProperties;
            this.jwtProperties = jwtProperties;
        }

        @Override
        public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        /*
        For a better client design, this should be done by a ClientDetailsService (similar to UserDetailsService).
         */
            clients.inMemory()
                    .withClient("web_app")
                    .scopes("openid")
                    .autoApprove(true)
                    .authorizedGrantTypes("implicit","refresh_token", "password", "authorization_code")
                    .and()
                    .withClient(jHipsterProperties.getSecurity().getClientAuthorization().getClientId())
                    .secret(jHipsterProperties.getSecurity().getClientAuthorization().getClientSecret())
                    .scopes("web-app")
                    .autoApprove(true)
                    .authorizedGrantTypes("client_credentials");
        }

        @Override
        public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
            endpoints.authenticationManager(authenticationManager)
                    .accessTokenConverter(jwtAccessTokenConverter())
                    .tokenStore(tokenStore);
        }

        @Autowired
        @Qualifier("authenticationManagerBean")
        private AuthenticationManager authenticationManager;

        @Autowired
        private TokenStore tokenStore;



        /**
         * This bean generates an token enhancer, which manages the exchange between JWT acces tokens and Authentication
         * in both directions.
         *
         * @return an access token converter configured with the authorization server's public/private keys
         */
        @Bean
        public JwtAccessTokenConverter jwtAccessTokenConverter() {
            JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
            if (jwtProperties.getSecurity() != null &&
                    StringUtils.isNotBlank(jwtProperties.getSecurity().getKey()) //存在加密字符串
                    && StringUtils.isEmpty(jwtProperties.getSecurity().getFile())){//不存在加密文件
                converter.setSigningKey(jwtProperties.getSecurity().getKey());
            }else{
                String fileName = jwtProperties.getSecurity() != null && StringUtils.isNotBlank(jwtProperties.getSecurity().getFile())?
                        jwtProperties.getSecurity().getFile():"keystore.jks";
                KeyPair keyPair = new KeyStoreKeyFactory(
                        new ClassPathResource(fileName), "password".toCharArray())
                        .getKeyPair("selfsigned");
                converter.setKeyPair(keyPair);
            }

//            KeyPair keyPair = new KeyStoreKeyFactory(
//                    new ClassPathResource("keystore.jks"), "password".toCharArray())
//                    .getKeyPair("selfsigned");
//            converter.setKeyPair(keyPair);
            return converter;
        }

        @Override
        public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
            oauthServer.tokenKeyAccess("permitAll()").checkTokenAccess(
                    "isAuthenticated()");
        }
    }

}
