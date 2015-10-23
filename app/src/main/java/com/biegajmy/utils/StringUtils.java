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

    public static Double stringToDouble(String str) {
        return str != null && !str.isEmpty() ? Double.valueOf(str) : null;
    }

    public static String doubleToString(Double dbl) {
        return dbl == null || dbl == 0.0 ? null : String.valueOf(dbl);
    }
}
