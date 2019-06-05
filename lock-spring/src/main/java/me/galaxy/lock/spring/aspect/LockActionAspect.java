package me.galaxy.lock.spring.aspect;

import me.galaxy.lock.SimpleLock;
import me.galaxy.lock.spring.ActionLockTemplate;
import me.galaxy.lock.spring.LockActionApplicationContextHolder;
import me.galaxy.lock.spring.utils.StringUtils;
import me.galaxy.lock.spring.annotation.LockAction;
import me.galaxy.lock.spring.annotation.LockName;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.List;
import java.util.Map;

import static org.springframework.util.ObjectUtils.isEmpty;

@Configuration
@Aspect
public class LockActionAspect {

    public static final Logger logger = LoggerFactory.getLogger(LockActionAspect.class);

    /**
     * 缺省的ActionLockTemplate
     */
    private ActionLockTemplate defaultActionLockTemplate;

    @Autowired
    private LockActionApplicationContextHolder lockActionApplicationContextHolder;

    public LockActionAspect() {

    }

    @PostConstruct
    public void afterAllBeanInitialized() {

        if (this.defaultActionLockTemplate == null) {
            this.defaultActionLockTemplate = lockActionApplicationContextHolder.getBeanFromMany(ActionLockTemplate.class);
        }

    }

    @Bean(name = "lockActionApplicationContextHolder")
    public LockActionApplicationContextHolder lockActionApplicationContextHolder() {
        return new LockActionApplicationContextHolder();
    }

    @Pointcut("execution(public * *(..)) && @annotation(lockAction)")
    public void cutLockAction(LockAction lockAction) {
        // None
    }

    @Around(value = "cutLockAction(lockAction)", argNames = "joinPoint,lockAction")
    public Object around(ProceedingJoinPoint joinPoint, LockAction lockAction) throws Exception {

        String actionLockFactoryName = lockAction.actionLockTemplate();

        if ("".equals(actionLockFactoryName) && this.defaultActionLockTemplate == null) {
            throw new NullPointerException("No actionLockTemplate was initialized.");
        }

        ActionLockTemplate actionLockTemplate;

        if (!isEmpty(actionLockFactoryName)) {
            actionLockTemplate = lockActionApplicationContextHolder.getBean(actionLockFactoryName);
        } else {
            actionLockTemplate = this.defaultActionLockTemplate;
        }

        logger.info(this.toString());
        logger.info(actionLockTemplate.toString());

        SimpleLock lock = null;

        try {

            long expireTime = lockAction.expireTime();
            long waitTime = lockAction.waitTime();

            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            Method method = signature.getMethod();
            Object[] methodArgs = joinPoint.getArgs();

            String lockName = getLockNameWithAnnotationInMethod(method, methodArgs);

            if (logger.isInfoEnabled()) {
                logger.info("lock name -> " + lockName);
            }

            lock = actionLockTemplate.create(lockName);

            lock.lock(expireTime, waitTime);

            return joinPoint.proceed();

        } catch (Throwable throwable) {
            throwable.printStackTrace();
            throw new Exception(throwable);
        } finally {
            if (lock != null) {
                lock.unlock();
            }
        }

    }

    /**
     * LockAction:方法名:参数值
     *
     * @param method
     * @param args
     * @return
     */
    private String getLockNameWithAnnotationInMethod(Method method, Object[] args) {

        StringBuilder lockName = new StringBuilder();

        String firstName = "LockAction";
        String middleName = getMiddleNameInLockActionAnnotation(method);
        String lastName = getLastNameInLockNameAnnotation(method.getParameters(), args);

        lockName.append(firstName).append(":").append(middleName).append(":").append(lastName);

        return lockName.toString();
    }

    private String getMiddleNameInLockActionAnnotation(Method method) {

        StringBuilder builder = new StringBuilder();

        LockAction lockAction = method.getAnnotation(LockAction.class);
        // 获取锁名
        String middleName = StringUtils.priority(lockAction.value(), lockAction.name());

        // 锁名为空则使用方法名作为锁名
        if ("".equals(middleName)) middleName = method.getName();

        builder.append(middleName);

        return builder.toString();
    }

    private String getLastNameInLockNameAnnotation(Parameter[] parameters, Object[] args) {

        StringBuilder builder = new StringBuilder();

        boolean hasLockName = false;

        for (int i = 0; i < parameters.length; i++) {

            LockName lockName = parameters[i].getAnnotation(LockName.class);

            if (lockName == null) continue;

            String valueName = getNameWithPatterInLockName(StringUtils.priority(lockName.value(), lockName.pattern()), args[i]);

            builder.append(valueName);

            hasLockName = true;
        }

        if (!hasLockName) builder.append("None");

        return builder.toString();
    }

    private String getNameWithPatterInLockName(String patterns, Object argument) {

        if ("".equals(patterns)) return argument.toString();

        String[] parts = patterns.split("\\|");

        StringBuilder builder = new StringBuilder();

        for (String part : parts) {

            if ("".equals(part)) continue;

            builder.append(getValueWithPattern(part, argument));
        }

        return builder.toString();
    }

    private String getValueWithPattern(String pattern, Object argument) {

        String[] parts = pattern.split("\\.");

        if (parts.length <= 1) return argument.toString();

        try {

            Object obj = argument;

            for (int i = 1; i < parts.length; i++) {
                obj = getObjectWithPattern(parts[i], obj);
            }

            return obj.toString();

        } catch (NoSuchFieldException e) {
            logger.warn("No such pattern matched: " + pattern);
            return null;
        } catch (IllegalAccessException e) {
            logger.warn("No accessible promised to " + pattern);
            return null;
        }

    }

    private Object getObjectWithPattern(String pattern, Object obj) throws NoSuchFieldException, IllegalAccessException {

        NameAndKey nk = getNameAndKeyFromPattern(pattern);

        Field field = obj.getClass().getDeclaredField(nk.name);
        field.setAccessible(true);
        obj = field.get(obj);

        if (obj instanceof List) {
            obj = ((List) obj).get(Integer.parseInt(nk.key));
        } else if (obj instanceof Map) {
            obj = ((Map) obj).get(nk.key);
        }

        return obj;
    }

    private NameAndKey getNameAndKeyFromPattern(String pattern) {

        NameAndKey nk = new NameAndKey();

        int end = pattern.lastIndexOf("]");

        if (end == -1) {
            nk.name = pattern;
            nk.key = null;
        }

        int start = pattern.lastIndexOf("[");

        nk.name = pattern.substring(0, start);
        nk.key = pattern.substring(start + 1, end);

        return nk;
    }

    class NameAndKey {
        String name;
        String key;
    }

}
