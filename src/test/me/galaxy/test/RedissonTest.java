package me.galaxy.test;

import me.galaxy.test.redisson.RedissonConfiguration;
import me.galaxy.test.redisson.RedissonLockFactory;
import org.junit.Test;

public class RedissonTest {

    @Test
    public void testLock() {

        RedissonConfiguration configuration = new RedissonConfiguration();
        configuration.setAddress("redis://127.0.0.1:6379");
        configuration.setDatabase("0");

        LockFactory lockFactory = new RedissonLockFactory(configuration);

        ActionLock actionLock = new ActionLock(lockFactory);

        SimpleLock lock = actionLock.create("testLock");

        lock.lock(5000, 500);

        lock.unlock();

    }

}
