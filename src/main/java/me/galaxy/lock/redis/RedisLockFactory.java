package me.galaxy.lock.redis;

import me.galaxy.lock.GenericLock;
import me.galaxy.lock.LockFactory;
import org.springframework.data.redis.core.RedisTemplate;

public class RedisLockFactory implements LockFactory {

    private RedisTemplate<String, String> redisTemplate;

    public RedisLockFactory(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public GenericLock create(String name) {
        return new RedisLock(redisTemplate, name);
    }

}
