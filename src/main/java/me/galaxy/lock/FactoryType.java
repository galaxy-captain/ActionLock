package me.galaxy.lock;

/**
 * @description: 产生锁的工厂的类型
 * @author: Galaxy
 * @date: 2019-06-02 23:19
 **/
public enum FactoryType {

    /**
     *
     */
    MEMORY("memory"),
    /**
     *
     */
    REDIS("redis"),
    /**
     *
     */
    REDISSON("redisson");

    private String type;

    FactoryType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
