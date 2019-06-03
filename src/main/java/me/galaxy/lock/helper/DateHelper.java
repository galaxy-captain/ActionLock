package me.galaxy.lock.helper;

public class DateHelper {

    public static long currentTimeNanos() {
        return System.nanoTime();
    }

    public static long currentTimeMillis() {
        return System.currentTimeMillis();
    }

}
