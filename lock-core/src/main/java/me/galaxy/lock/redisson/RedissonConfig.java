package me.galaxy.lock.redisson;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;


public class RedissonConfig {

    private String address;

    private String database;

    private String password;

    public RedissonConfig() {

    }

    public RedissonConfig(String address, String database, String password) {
        this.address = address;
        this.database = database;
        this.password = password;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    RedissonClient redissonClient() {

        Config config = new Config();
        config.useSingleServer().setAddress(address).setDatabase(Integer.parseInt(database)).setPassword(password);

        return Redisson.create(config);
    }

}
