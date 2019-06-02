package me.galaxy.lock.cache;

import me.galaxy.lock.Cache;

import java.util.HashMap;
import java.util.Map;

/**
 * @description: TODO
 * @author: Galaxy
 * @date: 2019-06-02 23:06
 **/
public class MemoryCache implements Cache {

    private Map<String, Long> cache = new HashMap<String, Long>();

}
