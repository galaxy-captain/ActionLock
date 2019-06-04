package me.galaxy.lock;

/**
 * @description: 锁的门面类
 * @author: Galaxy
 * @date: 2019-06-02 22:50
 **/
public class ActionLock {

    private FactoryType type;

    private LockFactory factory;

    public ActionLock(FactoryType type) {
        this.type = type;
        this.factory = FactoryBuilder.build(type);
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
