package com.troy.keeper.core.utils.object;

import com.troy.keeper.core.error.KeeperException;

import java.io.*;

public class ObjectUtil {

    /**
     * 序列化克隆
     *
     * @param <T>
     * @param obj
     * @param clazz
     * @return
     * @throws Exception
     */
    public static <T> T clone(Object obj, Class<T> clazz)  {
        // 将对象序列化后写在流里,因为写在流里面的对象是一份拷贝,
        // 原对象仍然在JVM里
        try{
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(obj);
            ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bos.toByteArray()));
            return clazz.cast(ois.readObject());
        }catch (Exception e){
            throw new KeeperException(e);
        }

    }

    /**
     * 序列化對象
     *
     * @param object
     * @return
     * @throws IOException
     */
    public static byte[] serialize(Object object) throws IOException {
        ObjectOutputStream oos = null;
        ByteArrayOutputStream baos = null;
        // 序列化
        baos = new ByteArrayOutputStream();
        oos = new ObjectOutputStream(baos);
        oos.writeObject(object);
        byte[] bytes = baos.toByteArray();
        return bytes;

    }

    /**
     * 返序列化對象
     * @param bytes
     * @param clazz
     * @return
     * @throws ClassNotFoundException
     * @throws IOException
     */
    public static <T> T unserialize(byte[] bytes, Class<T> clazz) throws ClassNotFoundException, IOException {
        ByteArrayInputStream bais = null;
        // 反序列化
        bais = new ByteArrayInputStream(bytes);
        ObjectInputStream ois = new ObjectInputStream(bais);
        return clazz.cast(ois.readObject());

    }
}