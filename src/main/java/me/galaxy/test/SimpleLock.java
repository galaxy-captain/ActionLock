package me.galaxy.test;

/**
 * @description: 通用锁定义
 * @author: Galaxy
 * @date: 2019-06-02 23:02
 **/
public interface SimpleLock {

    /**
     * 从缓存中获取锁资源
     * <p>
     * 保证原子性操作
     *
     * @return {@code true} 获取到锁 {@code false} 其他情况
     */
    boolean tryLock();

    /**
     * 释放锁
     * <p>
     * 保证原子性操作
     *
     * @return {@code true} 成功释放锁 {@code false} 其他情况
     */
    boolean freeLock();

    /**
     * 根据名字获取全局唯一锁
     *
     * @param expireTime 锁的有效存在时间
     * @param waitTime   等待获取锁的时间
     * @return {@code true} 获取到锁 {@code false} 其他情况
     */
    boolean lock(long expireTime, long waitTime);

    /**
     * 根据名字释放全局唯一锁
     *
     * @return {@code true} 成功释放锁 {@code false} 其他情况
     */
    boolean unlock();

}
