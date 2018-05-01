package com.troy.keeper.core.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by yjm on 2017/8/3.
 */
@ConfigurationProperties(prefix = "jwt", ignoreUnknownFields = false)
public class JwtProperties {
    private final Security security = new Security();

    public Security getSecurity() {
        return security;
    }

    public static class Security{
        private String file;
        private String key;

        public String getFile() {
            return file;
        }

        public void setFile(String file) {
            this.file = file;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }
    }
}
