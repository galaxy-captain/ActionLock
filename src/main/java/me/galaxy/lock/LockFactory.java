package me.galaxy.lock;

/**
 * @description: TODO
 * @author: Galaxy
 * @date: 2019-06-02 23:03
 **/
public interface LockFactory {

    /**
     * @param name
     * @return
     */
    GenericLock create(String name);

}
