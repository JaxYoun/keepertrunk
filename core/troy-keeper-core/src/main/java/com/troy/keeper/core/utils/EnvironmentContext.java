package com.troy.keeper.core.utils;

import org.springframework.core.env.Environment;

/**
 * Created by Administrator on 2017/6/20.
 */
public class EnvironmentContext {
    private static Environment environment;

    public static void init(Environment environment) {
        EnvironmentContext.environment = environment;
    }

    public static Environment getEnvironment() {
        return environment;
    }

}
