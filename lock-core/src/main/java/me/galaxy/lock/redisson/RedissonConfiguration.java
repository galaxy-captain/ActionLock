package me.galaxy.lock.redisson;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;


public class RedissonConfiguration {

    private String address;

    private String database;

    public RedissonConfiguration() {

    }

    public RedissonConfiguration(String address, String database) {
        this.address = address;
        this.database = database;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public RedissonClient redissonClient() {

        Config config = new Config();
        config.useSingleServer().setAddress(address).setDatabase(Integer.parseInt(database));

        return Redisson.create(config);
    }

}
