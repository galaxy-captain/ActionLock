package me.galaxy.lock.spring;

import me.galaxy.lock.ActionLock;
import me.galaxy.lock.LockFactory;
import me.galaxy.lock.SimpleLock;
import me.galaxy.lock.spring.aspect.LockActionAspect;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.*;

@Configuration
public class ActionLockTemplate implements InitializingBean {

    private ActionLock actionLock;

    private LockFactory lockFactory;

    public ActionLockTemplate() {

    }

    public ActionLockTemplate(LockFactory lockFactory) {
        this.lockFactory = lockFactory;
        this.actionLock = buildActionLock(lockFactory);
    }

    public void setLockFactory(LockFactory lockFactory) {
        this.lockFactory = lockFactory;
    }

    public SimpleLock create(String name) {
        return this.actionLock.create(name);
    }

    @Override
    public void afterPropertiesSet() throws Exception {

        if (this.actionLock == null && this.lockFactory != null) {
            this.actionLock = buildActionLock(this.lockFactory);
        }

    }

    private ActionLock buildActionLock(LockFactory lockFactory) {
        return new ActionLock(lockFactory);
    }

//    @Bean
//    public LockActionAspectFactoryBean lockActionAspectFactoryBean() {
//        return new LockActionAspectFactoryBean();
//    }

}
