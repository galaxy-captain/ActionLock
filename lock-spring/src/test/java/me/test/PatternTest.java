package me.test;

import me.galaxy.lock.LockFactory;
import me.galaxy.lock.LockFactoryType;
import me.galaxy.lock.redisson.RedissonConfig;
import me.galaxy.lock.redisson.RedissonLockFactory;
import me.galaxy.lock.spring.ActionLockTemplate;
import me.galaxy.lock.spring.LockFactoryBean;
import me.galaxy.lock.spring.aspect.PatternReflect;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PatternTest {

    @Test
    public void testActionLockFactoryBean() {

        LockFactoryBean lockFactoryBean = new LockFactoryBean();
        lockFactoryBean.setLockFactoryType(LockFactoryType.REDISSON);
        lockFactoryBean.setHost("localhost");
        lockFactoryBean.setPort("6379");
        lockFactoryBean.setDatabase("0");

        ActionLockTemplate actionLockTemplate = new ActionLockTemplate(lockFactoryBean.getObject());

        Assert.assertNotNull(actionLockTemplate.create("testLock"));

    }


    @Test
    public void testActionLock() {

        RedissonConfig config = new RedissonConfig();
        config.setAddress("redis://127.0.0.1:6379");
        config.setDatabase("0");

        LockFactory lockFactory = new RedissonLockFactory(config);

        ActionLockTemplate actionLockTemplate = new ActionLockTemplate(lockFactory);

        Assert.assertNotNull(actionLockTemplate.create("testLock"));

    }

    @Test
    public void testPattern() {

        PatternReflect pattern = new PatternReflect();
        String pattern1 = "this.map[map].this[list].this[1]";
        String pattern2 = "this.list[0].this[list].this[1]";
        String pattern3 = "this.map[map].this[map].this[map].this[map].this[map].this[obj].map[obj].integer";
        String pattern4 = "this.list[0].this[list].this[0].this[obj].list[1]";
        Map map = new HashMap<>();
        List list = new ArrayList();

        PatternObject obj = new PatternObject();
        obj.setList(list);
        obj.setMap(map);
        obj.setInteger(9999);

        map.put("list", list);

        list.add(map);

        map.put("obj", obj);
        map.put("map", map);

        list.add("hello world");

        map.put("list2", list);

        Assert.assertEquals(pattern.getNameWithPatterInLockName(pattern1, obj), "hello world");
        Assert.assertEquals(pattern.getNameWithPatterInLockName(pattern2, obj), "hello world");
        Assert.assertEquals(pattern.getNameWithPatterInLockName(pattern3, obj), "9999");
        Assert.assertEquals(pattern.getNameWithPatterInLockName(pattern4, obj), "hello world");

    }

}
