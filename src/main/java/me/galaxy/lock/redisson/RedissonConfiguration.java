package me.galaxy.lock.redisson;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


public class RedissonConfiguration {

    private String address;

    private String database;

    public RedissonConfiguration() {

    }

    public RedissonClient redissonClient() {

        Config config = new Config();
        config.useSingleServer().setAddress(address).setDatabase(Integer.parseInt(database));

        return Redisson.create(config);
    }

}
