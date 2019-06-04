package me.galaxy.lock;

/**
 * @description: TODO
 * @author: Galaxy
 * @date: 2019-06-02 23:07
 **/
public interface Cache<K, V> {

    V get(K key);

    V getAndSet(K key, V value);

    boolean setIfNotExist(K key, V value);

    void expire(K key, long time);

    void remove(K key);

}
