package com.troy.keeper.monomer.portal.account.test;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by yjm on 2017/6/8.
 */
@Target({TYPE, METHOD, FIELD})
@Retention(RUNTIME)
public @interface LogRecord {
    boolean isLog() default false;
}
