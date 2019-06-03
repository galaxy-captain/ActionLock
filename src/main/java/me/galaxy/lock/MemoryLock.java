package me.galaxy.lock;

import me.galaxy.lock.helper.DateHelper;

/**
 * @description: TODO
 * @author: Galaxy
 * @date: 2019-06-02 23:02
 **/
public class MemoryLock implements GenericLock {

    /**
     *
     */
    private final Cache<String, Long> cache;

    /**
     *
     */
    private final static Long EXPIRE = 5_000L;

    /**
     *
     */
    private final static Long TOLERANT = 5L;

    private String name;

    private boolean isLocked = false;

    public MemoryLock(Cache<String, Long> cache, String name) {
        this.cache = cache;
        this.name = name;
    }

    @Override
    public boolean tryLock() {
        try {
            return doTryLock(EXPIRE);
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean lock() {

        int maxTryCount = 10;
        int tryIntervalMillis = (int) (EXPIRE * 0.8 / maxTryCount);
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

    private boolean doTryLock(long lockSeconds) {

        if (isLocked) return false;

        isLocked = doTryLock(this.cache, this.name, lockSeconds, TOLERANT);

        return isLocked;
    }

    private boolean doTryLock(Cache<String, Long> cache, String name, long lockSeconds, long tolerant) {

        long nowTime = DateHelper.currentTimeMillis();
        long expireTime = nowTime + lockSeconds + tolerant;

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
    public boolean freeLock() {

        if (!isLocked) return false;

        isLocked = false;

        cache.remove(this.name);

        System.out.println(cache.toString());

        return true;

    }

}
