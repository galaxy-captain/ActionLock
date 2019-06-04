package me.galaxy.lock;

import me.galaxy.lock.memory.MemoryLockFactory;
import me.galaxy.lock.redis.RedisLockFactory;
import me.galaxy.lock.redisson.RedissonLockFactory;

class FactoryBuilder {

    static LockFactory build(FactoryType type) {

        switch (type) {
            case REDISSON:
                return new RedissonLockFactory();
            case REDIS:
                return new RedisLockFactory();
            default:
                return new MemoryLockFactory();
        }

    }

}
