package com.troy.uaa.config;

/**
 * Application constants.
 */
public final class Constants {

    //Regex for acceptable logins
    public static final String LOGIN_REGEX = "^[_'.@A-Za-z0-9-]*$";

    public static final String SYSTEM_ACCOUNT = "system";
    public static final String OAUTH2_ACCESS_TOKEN = "OAUTH2_ACCESS_TOKEN";
    public static final String ANONYMOUS_USER = "anonymoususer";
    public static final String SELECT_ROLE = "selectRole";

    private Constants() {
    }
}
