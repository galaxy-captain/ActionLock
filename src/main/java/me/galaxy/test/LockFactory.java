package me.galaxy.test;

/**
 * @description: 生产锁的抽象工厂类
 * @author: Galaxy
 * @date: 2019-06-02 23:03
 **/
public interface LockFactory {

    /**
     * 产生锁
     *
     * @param name 锁名
     * @return 全局唯一锁
     */
    SimpleLock create(String name);

    /**
     * 获取工厂的类型
     *
     * @return FactoryType
     */
    FactoryType getFactoryType();

}
