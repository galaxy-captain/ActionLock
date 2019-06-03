package me.galaxy.lock.redis;

import me.galaxy.lock.GenericLock;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DigestUtils;
import org.springframework.data.redis.core.script.RedisScript;

import java.util.Collections;
import java.util.UUID;

public class RedisLock implements GenericLock {

    private RedisTemplate<String, String> redisTemplate;

    private String name;

    private long expireTime;

    private String randomKey;

    private static final RedisScript<Boolean> SETNX_AND_EXPIRE_SCRIPT;

    static {
        StringBuilder sb = new StringBuilder();
        sb.append("if (redis.call('setnx', KEYS[1], ARGV[1]) == 1) then\n");
        sb.append("\tredis.call('expire', KEYS[1], tonumber(ARGV[2]))\n");
        sb.append("\treturn true\n");
        sb.append("else\n");
        sb.append("\treturn false\n");
        sb.append("end");
        SETNX_AND_EXPIRE_SCRIPT = new RedisScriptImpl<Boolean>(sb.toString(), Boolean.class);
    }

    private static final RedisScript<Boolean> DEL_IF_GET_EQUALS;

    static {
        StringBuilder sb = new StringBuilder();
        sb.append("if (redis.call('get', KEYS[1]) == ARGV[1]) then\n");
        sb.append("\tredis.call('del', KEYS[1])\n");
        sb.append("\treturn true\n");
        sb.append("else\n");
        sb.append("\treturn false\n");
        sb.append("end");
        DEL_IF_GET_EQUALS = new RedisScriptImpl<Boolean>(sb.toString(), Boolean.class);
    }

    public RedisLock(RedisTemplate<String, String> redisTemplate, String name) {
        this.redisTemplate = redisTemplate;
        this.name = name;
        this.randomKey = Thread.currentThread().getId() + "." + UUID.randomUUID().toString();
    }

    @Override
    public boolean tryLock() {
        return redisTemplate.execute(SETNX_AND_EXPIRE_SCRIPT, Collections.singletonList(name), randomKey, String.valueOf(expireTime));
    }

    @Override
    public boolean freeLock() {
        return redisTemplate.execute(DEL_IF_GET_EQUALS, Collections.singletonList(name), randomKey);
    }

    @Override
    public boolean lock(long expireTime, long waitTime) {

        this.expireTime = expireTime;

        int retryCount = 10;
        int retryIntervalMillis = (int) (waitTime * 0.95 / retryCount);

        for (int hasRetryCount = 0; hasRetryCount < retryCount; hasRetryCount++) {

            // 成功获取到锁
            if (tryLock()) return true;

            try {
                // 等待重试
                Thread.sleep(retryIntervalMillis);
            } catch (InterruptedException e) {
                return false;
            }

        }

        return false;
    }

    @Override
    public boolean unlock() {
        return freeLock();
    }

    private static class RedisScriptImpl<T> implements RedisScript<T> {

        private final String script;

        private final String sha1;

        private final Class<T> resultType;

        public RedisScriptImpl(String script, Class<T> resultType) {
            this.script = script;
            this.sha1 = DigestUtils.sha1DigestAsHex(script);
            this.resultType = resultType;
        }

        @Override
        public String getSha1() {
            return sha1;
        }

        @Override
        public Class<T> getResultType() {
            return resultType;
        }

        @Override
        public String getScriptAsString() {
            return script;
        }

    }

}
