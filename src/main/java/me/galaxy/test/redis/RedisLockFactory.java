package me.galaxy.test.redis;

import me.galaxy.test.FactoryType;
import me.galaxy.test.SimpleLock;
import me.galaxy.test.LockFactory;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @description: 基于Redis中间件的锁的工厂类
 * @author: Galaxy
 * @date: 2019-06-02 23:07
 **/
public class RedisLockFactory implements LockFactory {

    private final FactoryType factoryType = FactoryType.REDIS;

    private RedisTemplate<String, String> redisTemplate;

    public RedisLockFactory() {

    }

    public RedisTemplate<String, String> getRedisTemplate() {
        return redisTemplate;
    }

    public void setRedisTemplate(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public SimpleLock create(String name) {
        return new RedisLock(redisTemplate, name);
    }

    @Override
    public FactoryType getFactoryType() {
        return this.factoryType;
    }


}
