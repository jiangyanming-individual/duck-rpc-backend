package com.jiang.duck.rpc.easy.serializer.impl;

import com.jiang.duck.rpc.easy.serializer.Serializer;

import java.io.*;


/**
 *
 * ObjectOutputStream 和ObjectInputStream
 */
public class JdkSerializer implements Serializer {

    /**
     * 序列化：
     * @param obj
     * @return
     * @param <T>
     * @throws IOException
     */

    @Override
    public <T> byte[] serialize(T obj) throws IOException {
        //写入流
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        //序列化流：
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(obj);
        objectOutputStream.close();

        //转为字节数组
        return byteArrayOutputStream.toByteArray();
    }


    /**
     * 反序列化
     * @param bytes
     * @param type  类型
     * @return
     * @param <T>
     * @throws IOException
     */
    @Override
    public <T> T deserialize(byte[] bytes, Class<T> type) throws IOException {

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        try {
            //读取流文件：
            return (T) objectInputStream.readObject();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }finally {
            //关闭流
            objectInputStream.close();
        }
    }
}
