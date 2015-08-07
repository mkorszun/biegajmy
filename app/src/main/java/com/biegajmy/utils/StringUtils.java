package com.biegajmy.utils;

import java.util.List;

public class StringUtils {

    public static String join(List<String> list, String sep) {
        StringBuilder builder = new StringBuilder();
        for (String s : list) {
            builder.append(s);
            builder.append(sep);
        }
        return builder.toString().trim();
    }
}
