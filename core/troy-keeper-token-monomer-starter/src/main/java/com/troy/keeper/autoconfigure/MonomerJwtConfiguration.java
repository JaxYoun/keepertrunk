package com.troy.keeper.autoconfigure;

import com.troy.keeper.core.config.JwtProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

/**
 * Created by yjm on 2017/8/3.
 */
@Configuration
public class MonomerJwtConfiguration {


    private final JwtProperties jwtProperties;

    public MonomerJwtConfiguration(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    }


    @Bean
    public TokenStore tokenStore(final RedisConnectionFactory connectionFactory) {
        return new RedisTokenStore(connectionFactory);
    }


    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setSigningKey(jwtProperties.getSecurity().getKey());
//        converter.setSigningKey("123");
        return converter;
    }

}
