package me.galaxy.lock.spring.aspect;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

public class PatternReflect {

    private static final Logger logger = LoggerFactory.getLogger(LockActionAspect.class);

    public String getNameWithPatterInLockName(String patterns, Object argument) {

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
        } catch (NullPointerException e) {
            throw new NullPointerException("No such pattern matched: " + pattern);
        }

    }

    private Object getObjectWithPattern(String pattern, Object obj) throws NoSuchFieldException, IllegalAccessException {

        if (obj == null)
            throw new NullPointerException();

        NameAndKey nk = getNameAndKeyFromPattern(pattern);

        if ("this".equals(nk.name)) {

        } else {
            Field field = obj.getClass().getDeclaredField(nk.name);
            field.setAccessible(true);
            obj = field.get(obj);
        }

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
        } else {

            int start = pattern.lastIndexOf("[");

            nk.name = pattern.substring(0, start);
            nk.key = pattern.substring(start + 1, end);
        }
        return nk;
    }

    class NameAndKey {
        String name;
        String key;
    }

}
