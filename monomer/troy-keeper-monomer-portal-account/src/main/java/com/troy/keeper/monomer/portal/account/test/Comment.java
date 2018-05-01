package com.troy.keeper.monomer.portal.account.test;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by yineng on 2015/7/31.
 */
@Target({TYPE, METHOD, FIELD})
@Retention(RUNTIME)
public @interface Comment {
//    String value() default "";
    Class saveFor() default void.class;
}
