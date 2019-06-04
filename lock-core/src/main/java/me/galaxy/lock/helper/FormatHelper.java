package me.galaxy.lock.helper;

import java.util.Map;

public class FormatHelper {

    public static String mapToString(Map map) {

        StringBuilder builder = new StringBuilder();
        builder.append(map.getClass().getName()).append("{\n");

        for (Object key : map.keySet()) {
            Object value = map.get(key);
            builder.append("\t").append(key).append(" : ").append(value).append(",\n");
        }

        builder.append("}");

        return builder.toString();

    }

}
