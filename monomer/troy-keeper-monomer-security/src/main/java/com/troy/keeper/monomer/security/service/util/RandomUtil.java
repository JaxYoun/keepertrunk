package com.troy.keeper.monomer.security.service.util;

import org.apache.commons.lang3.RandomStringUtils;

/**
 * Utility class for generating random Strings.
 * 自身业务相关的Util，放在自己工程，
 * 公共的util，放在uitl工程(无依赖,无业务,只是工具)
 */
public final class RandomUtil {

    private static final int DEF_COUNT = 20;

    private RandomUtil() {
    }

    /**
     * Generate a password.
     *
     * @return the generated password
     */
    public static String generatePassword() {
        return RandomStringUtils.randomAlphanumeric(DEF_COUNT);
    }

    /**
     * Generate an activation key.
     *
     * @return the generated activation key
     */
    public static String generateActivationKey() {
        return RandomStringUtils.randomNumeric(DEF_COUNT);
    }

    /**
    * Generate a reset key.
    *
    * @return the generated reset key
    */
    public static String generateResetKey() {
        return RandomStringUtils.randomNumeric(DEF_COUNT);
    }

    /**
    * Generate a unique series to validate a persistent token, used in the
    * authentication remember-me mechanism.
    *
    * @return the generated series data
    */
    public static String generateSeriesData() {
        return RandomStringUtils.randomAlphanumeric(DEF_COUNT);
    }

    /**
    * Generate a persistent token, used in the authentication remember-me mechanism.
    *
    * @return the generated token data
    */
    public static String generateTokenData() {
        return RandomStringUtils.randomAlphanumeric(DEF_COUNT);
    }
}
