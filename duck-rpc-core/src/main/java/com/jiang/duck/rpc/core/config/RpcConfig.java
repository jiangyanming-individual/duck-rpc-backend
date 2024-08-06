package com.jiang.duck.rpc.core.config;

import lombok.Data;

/**
 * rpc的配置类
 */
@Data
public class RpcConfig {

    /**
     * 是否开启mock服务
     */
    private Boolean mock = false;

    /**
     * rpc框架名
     */
    private String name = "rpc";

    /**
     * 版本
     */
    private String version = "1.0";

    /**
     * 域名
     */
    private String serverHost = "localhost";

    /**
     * 端口号
     */
    private Integer serverPort = 8080;

}
