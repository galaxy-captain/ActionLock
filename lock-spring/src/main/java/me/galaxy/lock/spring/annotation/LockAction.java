package me.galaxy.lock.spring.annotation;

import me.galaxy.lock.LockFactory;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @description: TODO
 * @author: Galaxy
 * @date: 2019-06-03 21:14
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface LockAction {

    String value() default "";

    String name() default "";

    String actionLockTemplate() default "";

    long expireTime() default 5000L;

    long waitTime() default 500L;

}
