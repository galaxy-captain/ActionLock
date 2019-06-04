package me.galaxy.test.memory;


import me.galaxy.test.Cache;
import me.galaxy.test.FactoryType;
import me.galaxy.test.SimpleLock;
import me.galaxy.test.LockFactory;

/**
 * @description: 基于内存的锁的工厂类
 * @author: Galaxy
 * @date: 2019-06-02 23:01
 **/
public class MemoryLockFactory implements LockFactory {

    /**
     * 基于内存的模式
     */
    private final FactoryType factoryType = FactoryType.MEMORY;

    /**
     * 内存缓存
     */
    private final Cache<String, Long> cache;

    /**
     * 基于ThreadLocal在每个线程存储锁
     */
    private final ThreadLocal<SimpleLock> locks = new ThreadLocal<>();

    public MemoryLockFactory() {
        this.cache = new MemoryCache();
    }

    public SimpleLock create(String name) {

        SimpleLock lock = locks.get();

        if (lock != null) return lock;

        lock = new MemoryLock(this.cache, name);

        locks.set(lock);

        return lock;
    }

    @Override
    public FactoryType getFactoryType() {
        return this.factoryType;
    }

}
