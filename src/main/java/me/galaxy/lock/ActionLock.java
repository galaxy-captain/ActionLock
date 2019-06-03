package me.galaxy.lock;

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
        factory = configurationHolder.createFactory();
    }

    private LockFactory getFactory() {
        return this.factory;
    }

    public static boolean lock(String name) {
        return getInstance().getFactory().create(name).lock();
    }

    public static boolean unlock(String name) {
        return getInstance().getFactory().create(name).freeLock();
    }

}
