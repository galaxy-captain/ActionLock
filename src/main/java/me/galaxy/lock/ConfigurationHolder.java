package me.galaxy.lock;

import me.galaxy.lock.memory.MemoryLockFactory;
import me.galaxy.lock.redis.RedisLockFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.context.ContextLoaderListener;

import java.util.Optional;
import java.util.function.Function;

/**
 * @description: TODO
 * @author: Galaxy
 * @date: 2019-06-02 23:18
 **/
@Configuration
public class ConfigurationHolder {

    private FactoryType factoryType = FactoryType.MEMORY;

    public void setFactoryType(FactoryType factoryType) {
        this.factoryType = factoryType;
    }

    public FactoryType getFactoryType() {
        return this.factoryType;
    }

    public LockFactory createFactory() {

        if (factoryType == FactoryType.REDIS) {
            return this.createRedisLockFactory();
        }

        return new MemoryLockFactory();
    }

    private LockFactory createRedisLockFactory() {
        RedisTemplate redisTemplate = ContextLoaderListener.getCurrentWebApplicationContext().getBean(RedisTemplate.class);
        RedisLockFactory redisLockFactory = new RedisLockFactory(redisTemplate);
        return redisLockFactory;
    }

}
