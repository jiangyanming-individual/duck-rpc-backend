package com.jiang.duck.rpc.core.config;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.checkerframework.checker.units.qual.A;

/**
 * 注册中心配置类
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterConfig {


    /**
     * 注册中心类型
     */
    private String registryKey="etcd";


    /**
     * 注册中心的地址
     */
    private String address="http://localhost:2379";


    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 超时时间 10s
     */
    private Long timeout=10000L;

}
