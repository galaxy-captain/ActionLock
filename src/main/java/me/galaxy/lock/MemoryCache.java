package me.galaxy.lock;


import me.galaxy.lock.Cache;
import me.galaxy.lock.helper.FormatHelper;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @description: TODO
 * @author: Galaxy
 * @date: 2019-06-02 23:06
 **/
public class MemoryCache implements Cache<String, Long> {

    private Map<String, Long> cache = new HashMap<String, Long>();

    private ReentrantLock getAndSetLock = new ReentrantLock(true);

    private ReentrantLock setIfNotExistLock = new ReentrantLock(true);

    /**
     * @param key
     * @return
     */
    @Override
    public Long get(String key) {
        return cache.get(key);
    }

    /**
     * 原子性操作
     * <p>
     * 获取旧值并赋新值，通过操作
     *
     * @param key   键
     * @param value 值
     * @return 旧值
     */
    @Override
    public Long getAndSet(String key, Long value) {

        if (value == null) throw new NullPointerException("Method parameter that named 'value[Long]' can not be null.");

        try {

            getAndSetLock.lock();

            Optional<Long> oldValue = Optional.ofNullable(cache.get(key));

            cache.put(key, value);

            return oldValue.orElse(-1L);

        } finally {
            getAndSetLock.unlock();
        }

    }

    /**
     * 原子性操作
     * <p>
     * 如果键不存在，则赋值
     *
     * @param key   键
     * @param value 值
     * @return {@code false} 键已经存在，赋值失败; {@code true} 键不存在，赋值成功
     */
    @Override
    public boolean setIfNotExist(String key, Long value) {

        try {
            setIfNotExistLock.lock();

            if (cache.containsKey(key)) return false;

            cache.put(key, value);

            return true;

        } finally {
            setIfNotExistLock.unlock();
        }
    }

    /**
     * @param key
     * @param time
     */
    @Override
    public void expire(String key, long time) {

    }

    /**
     * @param key
     */
    @Override
    public void remove(String key) {
        cache.remove(key);
    }

    @Override
    public String toString() {
        return FormatHelper.mapToString(cache);
    }

}
