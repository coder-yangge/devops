package com.devops.web.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author yangge
 * @version 1.0.0
 * @title: XssFilter
 * @description: TODO
 * @date 2020/4/21 17:51
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface XssFilter {

    String value() default "";
}
