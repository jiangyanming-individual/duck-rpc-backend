package com.jiang.duck.rpc.core.proxy;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.jiang.duck.rpc.core.RpcApplication;
import com.jiang.duck.rpc.core.config.RegisterConfig;
import com.jiang.duck.rpc.core.constants.RpcConstant;
import com.jiang.duck.rpc.core.model.RpcRequest;
import com.jiang.duck.rpc.core.model.RpcResponse;
import com.jiang.duck.rpc.core.model.ServiceMetaInfo;
import com.jiang.duck.rpc.core.registry.Register;
import com.jiang.duck.rpc.core.registry.RegistryFactory;
import com.jiang.duck.rpc.core.serializer.Serializer;
import com.jiang.duck.rpc.core.serializer.SerializerFactory;
import com.jiang.duck.rpc.core.serializer.impl.JdkSerializer;


import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;

/**
 * 使用jdk实现的消费者端的服务代理类：
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

        //序列化器: 硬编码的方式
//        Serializer serializer = new JdkSerializer();
        //使用序列工厂的模式：
        final Serializer serializer = SerializerFactory.getInstance(RpcApplication.getRpcConfig().getSerializerKey());

        //得到服务类名：
        String serviceName = method.getDeclaringClass().getName();
        //请求封装类
        RpcRequest rpcRequest = RpcRequest.builder()
                .serviceName(serviceName) //声明该方法的类
                .methodName(method.getName()) //方法名
                .parameterTypes(method.getParameterTypes()) //参数类型
                .args(args) //传入参数
                .build();

        try {
            //序列化：
            byte[] requestBody = serializer.serialize(rpcRequest);
            byte[] result;
            //从注册中心获取服务提供者提供的服务：
            RegisterConfig registerConfig = RpcApplication.getRpcConfig().getRegisterConfig();
            //得到注册中心实例
            Register register = RegistryFactory.getInstance(registerConfig.getRegistryKey());
            ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
            serviceMetaInfo.setServiceName(serviceName);
            serviceMetaInfo.setServiceVersion(RpcConstant.DEFAULT_SERVICE_VERSION);
            //消费者端发现服务：
            List<ServiceMetaInfo> serviceMetaInfoList = register.serviceDiscovery(serviceMetaInfo.getServiceKey());
            if (CollUtil.isEmpty(serviceMetaInfoList)){
                throw new RuntimeException("暂无服务地址");
            }
            //如果有的话，先取第一个 todo 后续优化
            ServiceMetaInfo selectedServiceMetaInfo = serviceMetaInfoList.get(0);
            //发送请求：获取地址：
            try (HttpResponse httpResponse = HttpRequest.post(selectedServiceMetaInfo.getServiceAddress()).body(requestBody).execute()) {
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
