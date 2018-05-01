package com.troy.keeper.core.client;

import feign.RequestInterceptor;
import io.github.jhipster.security.uaa.LoadBalancedResourceDetails;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.cloud.security.oauth2.client.feign.OAuth2FeignRequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;

import java.io.IOException;

@Configuration
@ConditionalOnBean(LoadBalancedResourceDetails.class)
//@Profile("!"+ Constants.SPRING_PROFILE_PERFORMANCE)
public class OAuth2InterceptedFeignConfiguration {

    private final LoadBalancedResourceDetails loadBalancedResourceDetails;

    public OAuth2InterceptedFeignConfiguration(LoadBalancedResourceDetails loadBalancedResourceDetails) {
        this.loadBalancedResourceDetails = loadBalancedResourceDetails;
    }

    @Bean(name = "oauth2RequestInterceptor")
//    @Profile("!"+ Constants.SPRING_PROFILE_PERFORMANCE)
    public RequestInterceptor getOAuth2RequestInterceptor() throws IOException {
        return new OAuth2FeignRequestInterceptor(new DefaultOAuth2ClientContext(), loadBalancedResourceDetails);
    }
}
