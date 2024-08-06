package com.jiang.duck.rpc.core.proxy;


import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.jiang.duck.rpc.core.model.RpcRequest;
import com.jiang.duck.rpc.core.model.RpcResponse;
import com.jiang.duck.rpc.core.serializer.Serializer;
import com.jiang.duck.rpc.core.serializer.impl.JdkSerializer;


import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * 使用jdk实现的服务代理类：
 */
public class ServiceProxy implements InvocationHandler {

    /**
     * @param proxy  代理类
     * @param method 方法
     * @param args   方的参数
     * @return
     * @throws Throwable
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        //序列化器
        Serializer serializer = new JdkSerializer();
        //请求封装类
        RpcRequest rpcRequest = RpcRequest.builder()
                .serviceName(method.getDeclaringClass().getName()) //声明该方法的类
                .methodName(method.getName()) //方法名
                .parameterTypes(method.getParameterTypes()) //参数类型
                .args(args) //传入参数
                .build();

        try {
            //序列化：
            byte[] requestBody = serializer.serialize(rpcRequest);
            byte[] result;
            //发送请求：
            try (HttpResponse httpResponse = HttpRequest.post("http://localhost:8020").body(requestBody).execute()) {
                result = httpResponse.bodyBytes();
                //反序列化：
                RpcResponse rpcResponse = serializer.deserialize(result, RpcResponse.class);
                //返回对象：
                return rpcResponse.getData();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
