package com.troy.keeper.activiti.client.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Date:     2017/9/2 20:49<br/>
 *
 * @author work_tl
 * @see
 * @since JDK 1.8
 */
@Component
@ConfigurationProperties(prefix = "activiti", ignoreUnknownFields = false)
public class ActivitiConfig {
    private String baseUrl = "http://localhost:8081";

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }
}
