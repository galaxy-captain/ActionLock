package me.galaxy.lock.spring;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.ResolvableType;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

public class LockActionApplicationContextHolder implements ApplicationContextAware, DisposableBean {

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void destroy() throws Exception {
        applicationContext = null;

    }

    public <T> T getBean(String name) {
        return (T) applicationContext.getBean(name);
    }

    public <T> T getBean(Class<T> requiredType) {
        return applicationContext.getBean(requiredType);
    }

    public <T> T getBeanFromMany(Class<T> requiredType) {
        Map<String, T> beans = applicationContext.getBeansOfType(requiredType);
        return Optional.of(beans).map(Map::keySet).map(keys -> keys.iterator().next()).map(beans::get).orElse(null);
    }

    public Object getBean(String name, Object... args) throws BeansException {
        return applicationContext.getBean(name, args);
    }


    public <T> T getBean(Class<T> requiredType, Object... args) throws BeansException {
        return applicationContext.getBean(requiredType, args);
    }


    public boolean containsBean(String name) {
        return applicationContext.containsBean(name);
    }


    public boolean isSingleton(String name) throws NoSuchBeanDefinitionException {
        return applicationContext.isSingleton(name);
    }


    public boolean isPrototype(String name) throws NoSuchBeanDefinitionException {
        return applicationContext.isPrototype(name);
    }


    public boolean isTypeMatch(String name, ResolvableType typeToMatch) throws NoSuchBeanDefinitionException {
        return applicationContext.isTypeMatch(name, typeToMatch);
    }


    public boolean isTypeMatch(String name, Class<?> typeToMatch) throws NoSuchBeanDefinitionException {
        return applicationContext.isTypeMatch(name, typeToMatch);
    }


    public Class<?> getType(String name) throws NoSuchBeanDefinitionException {
        return applicationContext.getType(name);
    }


    public String[] getAliases(String name) {
        return applicationContext.getAliases(name);
    }

}
