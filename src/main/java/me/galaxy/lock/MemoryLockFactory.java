package me.galaxy.lock;

import me.galaxy.lock.cache.MemoryCache;

/**
 * @description: TODO
 * @author: Galaxy
 * @date: 2019-06-02 23:01
 **/
public class MemoryLockFactory implements LockFactory {

    /**
     *
     */
    private FactoryType type;

    /**
     *
     */
    private final Cache cache;

    public MemoryLockFactory() {
        this.type = FactoryType.MEMORY;
        this.cache = new MemoryCache();

    }

    public GenericLock create(String name) {
        return null;
    }

}
