package com.troy.uaa.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Properties specific to JHipster.
 *
 * <p>
 *     Properties are configured in the application.yml file.
 * </p>
 */
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
public class ApplicationProperties {

    private final Account account = new Account();

    public Account getAccount() {
        return account;
    }

    //账户
    public static class Account{
        private String initPassword;

        public String getInitPassword() {
            return initPassword;
        }

        public void setInitPassword(String initPassword) {
            this.initPassword = initPassword;
        }
    }



}
