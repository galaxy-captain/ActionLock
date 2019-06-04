package me.galaxy.lock.redisson;

import me.galaxy.lock.FactoryType;
import me.galaxy.lock.SimpleLock;
import me.galaxy.lock.LockFactory;
import org.redisson.api.RedissonClient;

public class RedissonLockFactory implements LockFactory {

    public static final FactoryType TYPE = FactoryType.REDISSON;

    private RedissonClient redissonClient;

    public RedissonClient getRedissonClient() {
        return redissonClient;
    }

    public void setRedissonClient(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    public SimpleLock create(String name) {
        return new RedissonLock(redissonClient, name);
    }

}
