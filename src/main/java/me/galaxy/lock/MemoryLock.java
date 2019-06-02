package me.galaxy.lock;

/**
 * @description: TODO
 * @author: Galaxy
 * @date: 2019-06-02 23:02
 **/
public class MemoryLock implements GenericLock {

    public GenericLock tryLock() {
        return null;
    }

    public boolean freeLock() {
        return false;
    }

}
