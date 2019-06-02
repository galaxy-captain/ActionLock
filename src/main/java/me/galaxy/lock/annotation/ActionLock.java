package me.galaxy.lock.annotation;

/**
 * @description: TODO
 * @author: Galaxy
 * @date: 2019-06-02 22:51
 **/
public @interface ActionLock {

    String value();

    boolean fair() default false;

}
