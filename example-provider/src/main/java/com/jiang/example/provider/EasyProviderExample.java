package com.jiang.example.provider;


import com.jiang.duck.rpc.easy.serve.VertxHttpServer;

/**
 * 服务提供者
 */
public class EasyProviderExample {
    public static void main(String[] args) {
        //提供服务：web服务
        VertxHttpServer vertxHttpServer = new VertxHttpServer();
        vertxHttpServer.doStart(8020);
    }
}
