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

    private final PatternReflect patternReflect = new PatternReflect();

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

    /**
     * 核心解析方法
     *
     * @param joinPoint
     * @param lockAction
     * @return
     * @throws Exception
     */
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

    /**
     * 获取锁名的中段名称
     *
     * @param method
     * @return
     */
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

    /**
     * 获取锁名的后段名称
     *
     * @param parameters
     * @param args
     * @return
     */
    private String getLastNameInLockNameAnnotation(Parameter[] parameters, Object[] args) {

        StringBuilder builder = new StringBuilder();

        boolean hasLockName = false;

        for (int i = 0; i < parameters.length; i++) {

            LockName lockName = parameters[i].getAnnotation(LockName.class);

            if (lockName == null) continue;

            String valueName = patternReflect.getNameWithPatterInLockName(StringUtils.priority(lockName.value(), lockName.pattern()), args[i]);

            builder.append(valueName);

            hasLockName = true;
        }

        if (!hasLockName) builder.append("None");

        return builder.toString();
    }

}
