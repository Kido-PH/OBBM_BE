package com.springboot.obbm.util;

import java.lang.reflect.Field;

public class StringFieldTrimmer {
    public static void trimAndNormalizeStringFields(Object obj) {
        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (field.getType().equals(String.class)) {
                try {
                    field.setAccessible(true);
                    String value = (String) field.get(obj);
                    if (value != null) {
                        field.set(obj, value.trim().replaceAll("\\s+", " "));
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
