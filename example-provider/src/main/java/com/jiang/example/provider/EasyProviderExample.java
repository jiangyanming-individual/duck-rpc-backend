package com.jiang.example.provider;


import com.jiang.duck.rpc.easy.registry.LocalRegistry;
import com.jiang.duck.rpc.easy.serve.VertxHttpServer;
import com.jiang.example.common.service.UserService;

/**
 * 服务提供者
 */
public class EasyProviderExample {
    public static void main(String[] args) {

        //注册服务：
        LocalRegistry.register(UserService.class.getName(),UserServiceImpl.class);

        //提供服务：web服务
        VertxHttpServer vertxHttpServer = new VertxHttpServer();
        vertxHttpServer.doStart(8020);
    }
}
