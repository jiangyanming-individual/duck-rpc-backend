package com.jiang.duck.rpc.core.serve;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;

public class VertxHttpServer implements HttpServe {
    @Override
    public void doStart(int port) {

        // vertx 实例化：
        Vertx vertx = Vertx.vertx();
        //创建http 服务器
        HttpServer server = vertx.createHttpServer();

//        server.requestHandler(request -> {
//            //http 请求
//            System.out.println("request method:" + request.method() + "request uri:" + request.uri());
//            //http 响应：
//            HttpServerResponse response = request.response();
//            response.putHeader("content-type", "text/plain");
//            // Write to the response and end it
//            response.end("Hello World!");
//        });

        //监听端口并处理请求：
        server.requestHandler(new HttpServerHandler());
        //future 类, 监听http端口
        server.listen(port, result -> {
            //异步返回
            if (result.succeeded()) {
                System.out.println("server is listening on port " + port);
            } else {
                System.out.println("Failed to start server " + result.cause());
            }
        });

    }
}
