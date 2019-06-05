package me.galaxy.lock.redisson;

import me.galaxy.lock.LockFactoryType;
import me.galaxy.lock.SimpleLock;
import me.galaxy.lock.LockFactory;
import org.redisson.api.RedissonClient;

public class RedissonLockFactory implements LockFactory {

    private final LockFactoryType lockFactoryType = LockFactoryType.REDISSON;

    private RedissonClient redissonClient;

    public RedissonLockFactory(RedissonConfig redissonConfig) {
        this.redissonClient = redissonConfig.redissonClient();
    }

    public RedissonClient getRedissonClient() {
        return redissonClient;
    }

    public SimpleLock create(String name) {
        return new RedissonLock(redissonClient, name);
    }

    @Override
    public LockFactoryType getLockFactoryType() {
        return this.lockFactoryType;
    }

}
