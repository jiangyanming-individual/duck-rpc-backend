package com.jiang.duck.rpc.core.serializer.impl;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jiang.duck.rpc.core.model.RpcRequest;
import com.jiang.duck.rpc.core.model.RpcResponse;
import com.jiang.duck.rpc.core.serializer.Serializer;

import java.io.IOException;

/**
 * json序列化器
 */
public class JsonSerializer implements Serializer {

    //JackSon 序列化Mapper
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    /**
     * 序列化： 对象转字节数组：
     * @param obj
     * @return
     * @param <T>
     * @throws IOException
     */
    @Override
    public <T> byte[] serialize(T obj) throws IOException {
        return OBJECT_MAPPER.writeValueAsBytes(obj);
    }

    /**
     * 反序列化：字节数组转对象：
     * 由于Object对象 会在序列化时将Object原始对象擦除，编程LinkedHashMap
     * @param bytes
     * @param type  类型
     * @return
     * @param <T>
     * @throws IOException
     */
    @Override
    public <T> T deserialize(byte[] bytes, Class<T> type) throws IOException {

        T obj = OBJECT_MAPPER.readValue(bytes, type);
        //请求对象
        if (obj instanceof RpcRequest){
            return handleRequest((RpcRequest) obj,type);
        }
        //响应对象
        if (obj instanceof RpcResponse){
            return handleResponse((RpcResponse) obj,type);
        }
        return obj;
    }


    /**
     * 单独处理RpcRequest 和RpcResponse:
     * @param rpcRequest
     * @param type
     * @return
     * @param <T>
     */
    public <T> T handleRequest(RpcRequest rpcRequest,Class<T> type){

        Class<?>[] parameterTypes = rpcRequest.getParameterTypes();
        Object[] args = rpcRequest.getArgs();

        for (int i = 0; i < parameterTypes.length; i++) {
            Class<?> clazz = parameterTypes[i];
            //如果类型不同，就重新处理一下
            if (!clazz.isAssignableFrom(args[i].getClass())){
                try {
                    byte[] bytes = OBJECT_MAPPER.writeValueAsBytes(args[i]);
                    args[i] = OBJECT_MAPPER.readValue(bytes, clazz);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
        //类型转换：
        return type.cast(rpcRequest);
    }

    /**
     * 单独处理response对象
     * @param rpcResponse
     * @param type
     * @return
     * @param <T>
     */
    public <T> T handleResponse(RpcResponse rpcResponse,Class<T> type){

        try {
            byte[] bytes = OBJECT_MAPPER.writeValueAsBytes(rpcResponse.getData());
            Object object = OBJECT_MAPPER.readValue(bytes, rpcResponse.getDataType());
            rpcResponse.setData(object);
            //转换类型：
            return type.cast(rpcResponse);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
