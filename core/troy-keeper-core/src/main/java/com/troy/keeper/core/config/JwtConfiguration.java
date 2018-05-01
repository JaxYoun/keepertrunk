package com.troy.keeper.core.config;


import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: xiongzhan
 * Date: 17-3-29
 * Time: 上午11:11
 * To change this template use File | Settings | File Templates.
 */
@Configuration
//@Profile("!"+ Constants.SPRING_PROFILE_PERFORMANCE)
@ConditionalOnProperty("jhipster.security.client-authorization.client-id")
public class JwtConfiguration {

    private final DiscoveryClient discoveryClient;

    public JwtConfiguration(DiscoveryClient discoveryClient) {
        this.discoveryClient = discoveryClient;
    }

    @Bean
    public TokenStore tokenStore(final RedisConnectionFactory connectionFactory) {
        return new RedisTokenStore(connectionFactory);
    }


    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter(
            @Qualifier("loadBalancedRestTemplate") RestTemplate keyUriRestTemplate) {

        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setVerifierKey(getKeyFromAuthorizationServer(keyUriRestTemplate));
        return converter;
    }

    private String getKeyFromAuthorizationServer(RestTemplate keyUriRestTemplate) {
        // Load available UAA servers
        discoveryClient.getServices();
        HttpEntity<Void> request = new HttpEntity<Void>(new HttpHeaders());
        return (String) keyUriRestTemplate
                .exchange("http://uaa/oauth/token_key", HttpMethod.GET, request, Map.class).getBody()
                .get("value");

    }

}
