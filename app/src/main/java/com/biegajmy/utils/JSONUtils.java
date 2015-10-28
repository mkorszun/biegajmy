package com.biegajmy.utils;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;

public class JSONUtils {

    public static String[] toArray(String jsonString) {
        String[] array = new String[] {};

        try {
            JSONArray arr = new JSONArray(jsonString);
            List<String> list = new ArrayList();
            for (int i = 0; i < arr.length(); i++) {
                list.add(arr.getString(i));
            }
            return list.toArray(array);
        } catch (JSONException e) {
            return array;
        }
    }
}
