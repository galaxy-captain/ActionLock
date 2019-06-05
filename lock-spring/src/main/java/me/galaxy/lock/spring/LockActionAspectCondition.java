package me.galaxy.lock.spring;

import me.galaxy.lock.spring.aspect.LockActionAspect;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

public class LockActionAspectCondition implements Condition {

    @Override
    public synchronized boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        return false;
    }

}
