package me.galaxy.lock;

import me.galaxy.lock.redis.RedisLockFactory;

/**
 * @description: TODO
 * @author: Galaxy
 * @date: 2019-06-02 22:50
 **/
public class ActionLock {

    private static volatile ActionLock INSTANCE = null;

    public static ActionLock getInstance() {

        if (INSTANCE == null) {
            synchronized (ActionLock.class) {
                if (INSTANCE == null) {
                    INSTANCE = new ActionLock();
                }
            }
        }

        return INSTANCE;
    }

    private ConfigurationHolder configurationHolder;

    private LockFactory factory;

    private ActionLock() {
        configurationHolder = new ConfigurationHolder();
        configurationHolder.setFactoryType(FactoryType.REDIS);

        factory = configurationHolder.createFactory();
    }

    private LockFactory getFactory() {
        return this.factory;
    }

    public static GenericLock create(String name) {
        return getInstance().getFactory().create(name);
    }

    public static boolean lock(String name) {
        return false;
    }

    public static boolean unlock(String name) {
        return false;
    }

}
