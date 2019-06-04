package me.galaxy.test.redisson;

import me.galaxy.test.FactoryType;
import me.galaxy.test.SimpleLock;
import me.galaxy.test.LockFactory;
import org.redisson.api.RedissonClient;

public class RedissonLockFactory implements LockFactory {

    private final FactoryType factoryType = FactoryType.REDISSON;

    private RedissonClient redissonClient;

    public RedissonLockFactory(RedissonConfiguration redissonConfiguration) {
        this.redissonClient = redissonConfiguration.redissonClient();
    }

    public RedissonClient getRedissonClient() {
        return redissonClient;
    }

    public SimpleLock create(String name) {
        return new RedissonLock(redissonClient, name);
    }

    @Override
    public FactoryType getFactoryType() {
        return this.factoryType;
    }

}
