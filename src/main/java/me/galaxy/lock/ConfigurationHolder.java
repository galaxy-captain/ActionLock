package me.galaxy.lock;

/**
 * @description: TODO
 * @author: Galaxy
 * @date: 2019-06-02 23:18
 **/
public class ConfigurationHolder {

    private FactoryType factoryType = FactoryType.MEMORY;

    public FactoryType getFactoryType() {
        return factoryType == null ? FactoryType.MEMORY : factoryType;
    }

    public LockFactory createFactory() {

        if (factoryType == FactoryType.REDIS) {
            return null;
        }

        return new MemoryLockFactory();
    }

}
