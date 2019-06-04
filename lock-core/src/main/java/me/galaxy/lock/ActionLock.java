package me.galaxy.lock;

/**
 * @description: 锁的门面类
 * @author: Galaxy
 * @date: 2019-06-02 22:50
 **/
public class ActionLock {

    private FactoryType type;

    private LockFactory factory;

    public ActionLock(LockFactory factory) {
        this.factory = factory;
        this.type = factory.getFactoryType();
    }

    public ActionLock(FactoryType type, LockFactory factory) {
        this.type = type;
        this.factory = factory;
    }

    public FactoryType getType() {
        return type;
    }

    public LockFactory getFactory() {
        return factory;
    }

    public SimpleLock create(String name) {
        return factory.create(name);
    }

}
