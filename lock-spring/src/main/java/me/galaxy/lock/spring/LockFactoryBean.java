package me.galaxy.lock.spring;

import me.galaxy.lock.LockFactoryType;
import me.galaxy.lock.LockFactory;
import me.galaxy.lock.memory.MemoryLockFactory;
import me.galaxy.lock.redis.RedisLockFactory;
import me.galaxy.lock.redisson.RedissonConfig;
import me.galaxy.lock.redisson.RedissonLockFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.springframework.util.Assert.notNull;
import static org.springframework.util.Assert.state;
import static org.springframework.util.ObjectUtils.isEmpty;
import static org.springframework.util.StringUtils.hasLength;

public class LockFactoryBean implements FactoryBean<LockFactory>, InitializingBean {

    private LockFactory lockFactory;

    private LockFactoryType lockFactoryType;

    private RedissonConfig redissonConfig;

    private String address;

    private String host;

    private String port;

    private String password;

    private String database;

    public LockFactory getLockFactory() {
        return lockFactory;
    }

    public void setLockFactory(LockFactory lockFactory) {
        this.lockFactory = lockFactory;
    }

    public LockFactoryType getLockFactoryType() {
        return lockFactoryType;
    }

    public void setLockFactoryType(LockFactoryType lockFactoryType) {
        this.lockFactoryType = lockFactoryType;
    }

    public void setRedissonConfig(RedissonConfig redissonConfig) {
        this.redissonConfig = redissonConfig;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    @Override
    public LockFactory getObject() {

        if (this.lockFactory == null) {
            afterPropertiesSet();
        }

        return this.lockFactory;
    }

    @Override
    public void afterPropertiesSet() {
        this.lockFactory = buildLockFactory();
    }

    private LockFactory buildLockFactory() {

        if (this.lockFactoryType == null) {
            this.lockFactoryType = LockFactoryType.MEMORY;
        }

        switch (lockFactoryType) {
            case REDISSON:
                return buildRedissonLockFactory();
            case REDIS:
                return buildRedisLockFactory();
            default:
                return buildMemoryLockFactory();
        }

    }

    private RedissonLockFactory buildRedissonLockFactory() {

        if (this.redissonConfig == null) {
            this.redissonConfig = new RedissonConfig();


            if (isEmpty(this.address))
                this.address = "redis://" + this.host + ":" + this.port;

            if (!this.address.startsWith("redis"))
                this.address = "redis://" + this.address;

            redissonConfig.setAddress(this.address);

            if (!isEmpty(this.password))
                redissonConfig.setPassword(this.password);

            redissonConfig.setDatabase(database);
        }

        return new RedissonLockFactory(this.redissonConfig);
    }

    private RedisLockFactory buildRedisLockFactory() {
        return null;
    }

    private MemoryLockFactory buildMemoryLockFactory() {
        return null;
    }

    @Override
    public Class<?> getObjectType() {
        return lockFactory == null ? LockFactory.class : lockFactory.getClass();
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

}
