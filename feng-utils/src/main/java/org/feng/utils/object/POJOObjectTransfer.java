package org.feng.utils.object;

import java.io.*;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * 简单java bean对象之间的互相转换
 * 通过字段名字匹配，只复制字段名相同的字段的值。
 */
public class POJOObjectTransfer<S, D> {

    private final Class<D> destClass;

    public POJOObjectTransfer(Class<D> destClass) {
        this.destClass = destClass;
    }

    /**
     * shallow copy
     *
     * @param src source
     * @return destiny object
     */
    public D shallow(S src) throws IllegalAccessException, InstantiationException {
        Field[] srcFields = src.getClass().getDeclaredFields();
        D dest = destClass.newInstance();
        Field[] destFields = dest.getClass().getDeclaredFields();

        Map<String, Object> srcMap = new HashMap<>();

        for (Field s : srcFields) {
            s.setAccessible(true);
            if (!"serialVersionUID".equals(s.getName())) {
                srcMap.put(s.getName(), s.get(src));
            }
        }
        for (Field d : destFields) {
            d.setAccessible(true);
            if (srcMap.containsKey(d.getName())) {
                d.set(dest, srcMap.get(d.getName()));
            }
        }

        return dest;
    }

    /**
     * deep copy
     *
     * @param src source
     * @return destiny object
     */
    public D deep(S src) throws IllegalAccessException, IOException, ClassNotFoundException, InstantiationException {
        D destObj = shallow(src);

        ByteArrayOutputStream tmp = new ByteArrayOutputStream();
        ObjectOutputStream writer = new ObjectOutputStream(tmp);
        writer.writeObject(destObj);

        ObjectInputStream reader = new ObjectInputStream(new ByteArrayInputStream(tmp.toByteArray()));
        D resObj = (D) reader.readObject();

        writer.close();
        reader.close();

        return resObj;
    }

}