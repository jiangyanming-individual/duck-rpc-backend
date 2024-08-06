package com.jiang.duck.rpc.easy.serve;

import com.jiang.duck.rpc.easy.model.RpcRequest;
import com.jiang.duck.rpc.easy.model.RpcResponse;
import com.jiang.duck.rpc.easy.registry.LocalRegistry;
import com.jiang.duck.rpc.easy.serializer.Serializer;
import com.jiang.duck.rpc.easy.serializer.impl.JdkSerializer;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;

import java.io.IOException;
import java.lang.reflect.Method;

/**
 * 请求处理器：
 */
public class HttpServerHandler implements Handler<HttpServerRequest> {
    @Override
    public void handle(HttpServerRequest httpServerRequest) {

        /**
         *
         * 主要步骤：
         *         1. 反序列化请求为对象，并从请求对象中获取参数。
         *         2. 根据服务名称从本地注册器中获取到对应的服务实现类。
         *         3. 通过反射机制调用方法，得到返回结果。
         *         4. 对返回结果进行封装和序列化，并写入到响应中。
         */

        //指定序列化器
        final Serializer jdkSerializer=new JdkSerializer();
        System.out.println("收到请求方法" + httpServerRequest.method() + "请求地址为：" + httpServerRequest.uri());
        httpServerRequest.bodyHandler(body -> {
            byte[] bytes = body.getBytes();
            RpcRequest rpcRequest=null;
            RpcResponse rpcResponse = new RpcResponse();
            try {
                //先是反序列化得到rpcRequest对象，因为在消费者端请求的时候就已经被序列化过了
                rpcRequest = jdkSerializer.deserialize(bytes, RpcRequest.class);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            //如果请求对象为空的情况下：
            if (rpcRequest == null){
                rpcResponse.setMessage("rpcRequest is null");
                doResponse(httpServerRequest,rpcResponse,jdkSerializer);
                return;
            }
            try {
                //2. 根据服务名称从本地注册器中获取到对应的服务实现类。
                Class<?> implClass = LocalRegistry.get(rpcRequest.getServiceName());
                Method method = implClass.getMethod(rpcRequest.getMethodName(), rpcRequest.getParameterTypes());
                //实例化服务类，然后传递参数，调用方法：==> 反射机制的使用
                Object result = method.invoke(implClass.newInstance(), rpcRequest.getArgs());
                //3. 通过反射机制调用方法，得到返回结果。
                rpcResponse.setData(result);
                rpcResponse.setDataType(method.getReturnType());
                rpcResponse.setMessage("ok");
            } catch (Exception e) {
                e.printStackTrace();
                //失败返回失败信息，其他信息为空：
                rpcResponse.setMessage(e.getMessage());
                rpcResponse.setException(e);
            }
            //4. 对返回结果进行封装和序列化，并写入到响应中。
            doResponse(httpServerRequest,rpcResponse,jdkSerializer);
        });

    }


    /**
     * 响应对象，对象相应对象进行序列化
     * @param request
     * @param rpcResponse
     * @param jdkSerializer
     */
    void doResponse(HttpServerRequest request, RpcResponse rpcResponse, Serializer jdkSerializer){
        HttpServerResponse httpServerResponse = request.response();
        httpServerResponse.putHeader("content-type", "application/json"); //设置请求头，为json格式
        try {
            //序列化：将对象转化为字节数组
            byte[] serializeData = jdkSerializer.serialize(rpcResponse);
            //传输数据:
            httpServerResponse.end(Buffer.buffer(serializeData));
        } catch (IOException e) {
            e.printStackTrace();
            httpServerResponse.end(Buffer.buffer());
        }
    }
}
