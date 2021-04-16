package org.feng.serialize.serializer;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.List;

public class EntitySerializer implements ObjectDeserializer {

    @Override
    public Object deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
        Object result = new Object();
//        Class targetClass = Person.class;
//        //拿到键值对
//        List<Pair> infoList = parser.parseArray(Pair.class);
//        for (Pair pair : infoList) {
//            //反射赋值
//            Field field;
//            try {
//                field = targetClass.getDeclaredField(pair.getKey());
//                field.setAccessible(true);
//                field.set(result, pair.getValue());
//            } catch (NoSuchFieldException | IllegalAccessException ignored) {
//
//            }
//        }
        return result;
    }

    @Override
    public int getFastMatchToken() {
        return 0;
    }

    @Setter
    @Getter
    private static class Pair {
        private String key;
        private Object value;
    }
}
