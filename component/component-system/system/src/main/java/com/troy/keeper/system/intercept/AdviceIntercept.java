package com.troy.keeper.system.intercept;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Harry on 2017/9/6.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AdviceIntercept {
    /**
     * 要进行拦截的接口
     * @return
     */
    Class value();

    String  prefix() default "pre";

    String  postfix() default "post";
}
