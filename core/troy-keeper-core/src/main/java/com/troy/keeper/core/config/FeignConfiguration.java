package com.troy.keeper.core.config;

import com.troy.keeper.core.client.OAuth2InterceptedFeignConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.netflix.feign.FeignContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ConditionalOnClass(FeignContext.class)
public class FeignConfiguration {


    @Configuration
    @Import(OAuth2InterceptedFeignConfiguration.class)
    @EnableFeignClients(basePackages = {"com.troy"})
    public class FeignClientsConfiguration {

    }
}

