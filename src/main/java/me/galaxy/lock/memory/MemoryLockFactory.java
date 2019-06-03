package me.galaxy.lock.memory;


import me.galaxy.lock.Cache;
import me.galaxy.lock.FactoryType;
import me.galaxy.lock.GenericLock;
import me.galaxy.lock.LockFactory;

/**
 * @description: TODO
 * @author: Galaxy
 * @date: 2019-06-02 23:01
 **/
public class MemoryLockFactory implements LockFactory {

    /**
     *
     */
    private static final FactoryType TYPE = FactoryType.MEMORY;

    /**
     *
     */
    private final Cache<String, Long> cache;

    /**
     *
     */
    private final ThreadLocal<GenericLock> locks = new ThreadLocal<>();

    public MemoryLockFactory() {
        this.cache = new MemoryCache();
    }

    public GenericLock create(String name) {

        GenericLock lock = locks.get();

        if (lock != null) return lock;

        lock = new MemoryLock(this.cache, name);

        locks.set(lock);

        return lock;
    }

}
