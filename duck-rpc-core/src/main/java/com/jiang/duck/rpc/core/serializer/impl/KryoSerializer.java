package com.jiang.duck.rpc.core.serializer.impl;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.jiang.duck.rpc.core.serializer.Serializer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;


/**
 * Kryo序列化器,存在线程不安全的问题
 */
public class KryoSerializer implements Serializer {


    //ThreadLocal做线程隔离
    private static final ThreadLocal<Kryo> KRYO_THREAD_LOCAL=ThreadLocal.withInitial(()->{
        Kryo kryo = new Kryo();
        //不提前注册所有类防止有安全问题
        kryo.setRegistrationRequired(false);
        return kryo;
    });


    @Override
    public <T> byte[] serialize(T obj) throws IOException {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Output output = new Output(byteArrayOutputStream);
        //获取kryo对象:
        KRYO_THREAD_LOCAL.get().writeObject(output,obj);
        output.close();
        return byteArrayOutputStream.toByteArray();
    }


    @Override
    public <T> T deserialize(byte[] bytes, Class<T> type) throws IOException {

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        Input input = new Input(byteArrayInputStream);
        //返回对象
        T t = KRYO_THREAD_LOCAL.get().readObject(input, type);
        //关闭流
        input.close();
        return t;
    }
}
