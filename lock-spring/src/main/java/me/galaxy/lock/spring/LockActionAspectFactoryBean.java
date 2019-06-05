package me.galaxy.lock.spring;

import me.galaxy.lock.spring.aspect.LockActionAspect;
import org.springframework.beans.factory.FactoryBean;

public class LockActionAspectFactoryBean implements FactoryBean<LockActionAspect> {

    @Override
    public LockActionAspect getObject() throws Exception {
        return new LockActionAspect();
    }

    @Override
    public Class<?> getObjectType() {
        return LockActionAspect.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

}
