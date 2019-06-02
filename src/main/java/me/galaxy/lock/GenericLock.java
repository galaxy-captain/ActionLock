package me.galaxy.lock;

/**
 * @description: TODO
 * @author: Galaxy
 * @date: 2019-06-02 23:02
 **/
public interface GenericLock {

    /**
     * 获取锁
     *
     * @return
     */
    GenericLock tryLock();

    /**
     * 释放锁
     *
     * @return
     */
    boolean freeLock();

}
