package com.troy.keeper.core.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * Created with IntelliJ IDEA.
 * User: xiongzhan
 * Date: 17-3-29
 * Time: 上午11:04
 * To change this template use File | Settings | File Templates.
 */
@Configuration
public class RestTemplateConfiguration {

    @Configuration
    @ConditionalOnClass(RibbonClient.class)
    protected static class LoadBalancedRestTemplate {

        @Bean
        public RestTemplate loadBalancedRestTemplate(org.springframework.cloud.client.loadbalancer.RestTemplateCustomizer customizer) {
            RestTemplate restTemplate = new RestTemplate();
            customizer.customize(restTemplate);
            return restTemplate;
        }
    }

    @Autowired
    private RestTemplateBuilder builder;

    @Bean
    public RestTemplate restTemplate() {
        return builder.build();
    }

}
