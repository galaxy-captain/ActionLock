package me.test;

import me.galaxy.lock.ActionLock;
import me.galaxy.lock.LockFactory;
import me.galaxy.lock.SimpleLock;
import me.galaxy.lock.redisson.RedissonConfig;
import me.galaxy.lock.redisson.RedissonLockFactory;
import org.junit.Test;

public class RedissonTest {

    @Test
    public void testLock() {

        RedissonConfig configuration = new RedissonConfig();
        configuration.setAddress("redis://127.0.0.1:6379");
        configuration.setDatabase("0");

        LockFactory lockFactory = new RedissonLockFactory(configuration);

        ActionLock actionLock = new ActionLock(lockFactory);

        SimpleLock lock = actionLock.create("testLock");

        lock.lock(5000, 500);

        lock.unlock();

    }

}
