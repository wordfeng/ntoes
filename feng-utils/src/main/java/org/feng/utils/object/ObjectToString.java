package org.feng.utils.object;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

public class ObjectToString {

    /**
     * 循环调用任意一个对象的所有属性的toString
     */
    public static String from(Object obj) {
        ArrayList<Object> visited = new ArrayList<>();

        if (obj == null) {
            return "null";
        }

        visited.add(obj);
        Class<?> clz = obj.getClass();
        if (clz == String.class)
            return (String) obj;
        if (clz.isArray()) {
            StringBuilder l = new StringBuilder(clz.getComponentType() + "[]{");
            for (int i = 0; i < Array.getLength(obj); i++) {
                if (i > 0) {
                    l.append(", \n");
                }
                Object value = Array.get(obj, i);
                if (clz.getComponentType().isPrimitive()) {
                    l.append(value);
                } else {
                    l.append(from(value));
                }
            }

            return l + "}";
        }

        StringBuilder res = new StringBuilder();
        res.append(clz.getName());
        do {
            res.append("{");
            Field[] fields = clz.getDeclaredFields();
            AccessibleObject.setAccessible(fields, true);

            for (Field field : fields) {
                if (!Modifier.isStatic(field.getModifiers())) {
                    if (!res.toString().endsWith("[") && !res.toString().endsWith("{")) {
                        res.append(", ");
                    }
                    res.append(field.getName()).append("=");

                    try {
                        Class<?> type = field.getType();
                        Object value = field.get(obj);
                        if (type.isPrimitive()) {
                            res.append(value);
                        } else {
                            res.append(from(value));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            res.append("}");
            clz = clz.getSuperclass();
        } while (clz != null);

        return res.toString();
    }

}