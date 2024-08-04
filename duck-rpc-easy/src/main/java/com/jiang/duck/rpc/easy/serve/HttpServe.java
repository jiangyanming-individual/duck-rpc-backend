package com.jiang.duck.rpc.easy.serve;

/**
 * web 服务器：
 */
public interface HttpServe {
    /**
     * 启动服务器
     * @param port
     */
    void doStart(int port);
}
