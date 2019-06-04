package me.galaxy.lock.redisson;

import me.galaxy.lock.SimpleLock;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import java.util.concurrent.TimeUnit;

public class RedissonLock implements SimpleLock {

    private RedissonClient redissonClient;

    private String name;

    private RLock lock;

    public RedissonLock(RedissonClient redissonClient, String name) {
        this.redissonClient = redissonClient;
        this.name = name;
        this.lock = redissonClient.getLock(name);
    }

    @Override
    public boolean tryLock() {

        // 此方法在此实现中无需使用

        try {

            if (lock == null)
                return false;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    @Override
    public boolean freeLock() {

        try {

            if (lock == null)
                return false;

            lock.unlock();

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    @Override
    public boolean lock(long expireTime, long waitTime) {
        lock.lock(expireTime, TimeUnit.MILLISECONDS);
        return true;
    }

    @Override
    public boolean unlock() {
        return freeLock();
    }

}
