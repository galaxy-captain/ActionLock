package me.galaxy.test.memory;

import me.galaxy.test.Cache;
import me.galaxy.test.SimpleLock;
import me.galaxy.test.helper.DateHelper;

/**
 * @description: 基于内存的锁的实现
 * @author: Galaxy
 * @date: 2019-06-02 23:02
 **/
public class MemoryLock implements SimpleLock {

    /**
     * 内存缓存
     */
    private final Cache<String, Long> cache;

    private String name;

    private long expireTime;

    private boolean isLocked = false;

    public MemoryLock(Cache<String, Long> cache, String name) {
        this.cache = cache;
        this.name = name;
    }

    private boolean doTryLock(long lockSeconds) {

        if (isLocked) return false;

        isLocked = doTryLock(this.cache, this.name, lockSeconds);

        return isLocked;
    }

    private boolean doTryLock(Cache<String, Long> cache, String name, long lockSeconds) {

        long nowTime = DateHelper.currentTimeMillis();
        long expireTime = nowTime + lockSeconds + 100;

        if (cache.setIfNotExist(name, expireTime)) {
            cache.expire(name, lockSeconds);
            return true;
        } else {
            Long oldValue = cache.get(name);
            if (oldValue != null && oldValue < nowTime) {
                Long oldValue2 = cache.getAndSet(name, expireTime);
                if (oldValue.equals(oldValue2)) {
                    cache.expire(name, lockSeconds);
                    return true;
                } else {
                    return false;
                }
            }
        }

        return false;
    }

    @Override
    public boolean tryLock() {
        try {
            return doTryLock(this.expireTime);
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean freeLock() {

        if (!isLocked) return false;

        isLocked = false;

        cache.remove(this.name);

        return true;

    }

    @Override
    public boolean lock(long expireTime, long waitTime) {

        this.expireTime = expireTime;

        int maxTryCount = 10;
        int tryIntervalMillis = (int) (waitTime * 0.95 / maxTryCount);
        int tryCount = 0;

        while (true) {

            if (++tryCount >= maxTryCount) {
                return false;
            }

            try {
                if (tryLock()) return true;
            } catch (Exception e) {
                return false;
            }

            try {
                Thread.sleep(tryIntervalMillis);
            } catch (InterruptedException e) {
                return false;
            }

        }

    }

    @Override
    public boolean unlock() {
        return this.freeLock();
    }

}
