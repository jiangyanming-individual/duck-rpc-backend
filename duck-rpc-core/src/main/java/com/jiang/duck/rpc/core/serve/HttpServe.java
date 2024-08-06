package com.jiang.duck.rpc.core.serve;

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
