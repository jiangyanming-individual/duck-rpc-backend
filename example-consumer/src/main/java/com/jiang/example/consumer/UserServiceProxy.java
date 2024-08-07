package com.jiang.example.consumer;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.jiang.duck.rpc.core.model.RpcRequest;
import com.jiang.duck.rpc.core.model.RpcResponse;
import com.jiang.duck.rpc.core.serializer.Serializer;
import com.jiang.duck.rpc.core.serializer.impl.JdkSerializer;
import com.jiang.example.common.model.User;
import com.jiang.example.common.service.UserService;

import java.io.IOException;

/**
 * 静态代理的模式：
 */
public class UserServiceProxy implements UserService {
    @Override
    public User getUser(User user) {

        //序列化器：
        final Serializer serializer=new JdkSerializer();

        //请求对象：
        RpcRequest rpcRequest=RpcRequest.builder()
                .serviceName(UserService.class.getName())
                .methodName("getUser")
                .parameterTypes(new Class[]{User.class}) //参数类型
                .args(new Object[]{user}) //传入参数
                .build();

        //序列化
        try {
            byte[] requestBody = serializer.serialize(rpcRequest);
            //结果：
            byte[] result;
            //发送请求
            try (HttpResponse httpResponse = HttpRequest.
                    post("http://localhost:8020")
                    .body(requestBody)
                    .execute()){
               result= httpResponse.bodyBytes();
            }
            //反序列化：将字节数组转化为对象：
            RpcResponse rpcResponse = serializer.deserialize(result, RpcResponse.class);
            return (User) rpcResponse.getData();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
