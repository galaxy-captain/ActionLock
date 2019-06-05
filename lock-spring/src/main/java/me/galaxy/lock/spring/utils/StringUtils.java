package me.galaxy.lock.spring.utils;

public class StringUtils {

    public static String priority(String value, String priorValue) {
        return (priorValue == null || "".equals(priorValue)) ? value : priorValue;
    }

}
