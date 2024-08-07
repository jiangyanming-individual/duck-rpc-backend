package com.jiang.duck.rpc.core.serializer.impl;

import com.caucho.hessian.io.HessianInput;
import com.caucho.hessian.io.HessianOutput;
import com.jiang.duck.rpc.core.serializer.Serializer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;


/**
 * HessianSerializer
 */
public class HessianSerializer implements Serializer {

    /**
     * 序列化：
     * @param obj
     * @return
     * @param <T>
     * @throws IOException
     */
    @Override
    public <T> byte[] serialize(T obj) throws IOException {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        HessianOutput hessianOutput = new HessianOutput(byteArrayOutputStream);
        hessianOutput.writeObject(obj);
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
        HessianInput hessianInput = new HessianInput(byteArrayInputStream);
        Object object = hessianInput.readObject(type);
        return (T) object;
    }
}
