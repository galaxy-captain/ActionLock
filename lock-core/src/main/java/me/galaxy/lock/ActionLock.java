package me.galaxy.lock;

/**
 * @description: 锁的门面类
 * @author: Galaxy
 * @date: 2019-06-02 22:50
 **/
public class ActionLock {

    private LockFactoryType type;

    private LockFactory factory;

    public ActionLock(LockFactory factory) {
        this.factory = factory;
        this.type = factory.getLockFactoryType();
    }

    public ActionLock(LockFactoryType type, LockFactory factory) {
        this.type = type;
        this.factory = factory;
    }

    public LockFactoryType getType() {
        return type;
    }

    public LockFactory getFactory() {
        return factory;
    }

    public SimpleLock create(String name) {
        return factory.create(name);
    }

}
