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

    public static void lock(String name) {
        getInstance().getFactory().create(name).tryLock();
    }

    public static void unlock(String name) {
        getInstance().getFactory().create(name).freeLock();
    }

}
