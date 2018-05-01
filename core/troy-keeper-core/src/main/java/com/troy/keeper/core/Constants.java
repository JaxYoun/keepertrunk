package com.troy.keeper.core;

/**
 * Application constants.
 */
public final class Constants {

    //Regex for acceptable logins
    public static final String LOGIN_REGEX = "^[_'.@A-Za-z0-9-]*$";

    public static final String SPRING_PROFILE_PERFORMANCE = "perf";

    public static final String OAUTH2_ACCESS_TOKEN = "OAUTH2_ACCESS_TOKEN";
    public static final String COLON = ":";
    public static final String SESSION = "SESSION";

    public static final Long SYSTEM_ACCOUNT = 0l;//"system";
    public static final String ANONYMOUS_USER = "anonymoususer";

    private Constants() {
    }


}
