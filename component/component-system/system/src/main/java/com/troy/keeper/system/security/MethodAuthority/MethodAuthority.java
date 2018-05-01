package com.troy.keeper.system.security.MethodAuthority;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * * * * * * * * * * * * *
 * 接口权限注解类
 * * * * * * * * * * * * *
 * Class Name:InterfaceAuthorityAnnotation
 *
 * @author SimonChu By Troy
 * @create 2017-10-24 14:45
 **/
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface MethodAuthority {

    boolean annotationSwitch() default true;

    ControlType CONTROL_TYPE();

    String menuCode();

}
