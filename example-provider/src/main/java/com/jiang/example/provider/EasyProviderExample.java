package com.jiang.example.provider;


import com.jiang.duck.rpc.core.RpcApplication;
import com.jiang.duck.rpc.core.config.RegisterConfig;
import com.jiang.duck.rpc.core.config.RpcConfig;
import com.jiang.duck.rpc.core.model.ServiceMetaInfo;
import com.jiang.duck.rpc.core.registry.LocalRegistry;
import com.jiang.duck.rpc.core.registry.Register;
import com.jiang.duck.rpc.core.registry.RegistryFactory;
import com.jiang.duck.rpc.core.serve.VertxHttpServer;
import com.jiang.example.common.service.UserService;

/**
 * 服务提供者
 */
public class EasyProviderExample {
    public static void main(String[] args) {

        //框架初始化：
        RpcApplication.init();
        //注册服务：
        String serviceName = UserService.class.getName();
        //本地注册，后期忽略
        LocalRegistry.register(serviceName,UserServiceImpl.class);

        //服务注册到注册中心:
        RpcConfig rpcConfig = RpcApplication.getRpcConfig();
        RegisterConfig registerConfig = rpcConfig.getRegisterConfig();
        //获取注册中心实例：
        Register register = RegistryFactory.getInstance(registerConfig.getRegistryKey());
        //设置服务信息：
        ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
        serviceMetaInfo.setServiceName(serviceName);
        serviceMetaInfo.setServiceHost(rpcConfig.getServerHost());
        serviceMetaInfo.setServicePort(rpcConfig.getServerPort());
        //服务提供者注册服务
        try {
            register.registry(serviceMetaInfo);
        }catch (Exception e){
            throw new RuntimeException("服务提供者注册服务失败",e);
        }
        //提供服务：web服务
        VertxHttpServer vertxHttpServer = new VertxHttpServer();
        //获取配置服务的端口号：
        vertxHttpServer.doStart(RpcApplication.getRpcConfig().getServerPort());
    }
}
