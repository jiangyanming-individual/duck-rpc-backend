package com.jiang.duck.rpc.core.registry;

import com.jiang.duck.rpc.core.config.RegisterConfig;
import com.jiang.duck.rpc.core.model.ServiceMetaInfo;

import java.util.List;

/**
 * 注册中心：注册中心初始化、服务注册， 服务注销，服务发现，服务销毁，
 * */
public interface Register {

    /**
     * 注册中心初始化：
     * @param registerConfig
     */
    void init(RegisterConfig registerConfig);


    /**
     * 服务注册
     * @param serviceMetaInfo
     * @throws Exception
     */
    void registry(ServiceMetaInfo serviceMetaInfo) throws Exception;


    /**
     * 服务注销
     * @param serviceMetaInfo
     */
    void unRegistry(ServiceMetaInfo serviceMetaInfo);


    /**
     * 发现服务
     * @param serviceKey 服务键名
     * @return
     */
    List<ServiceMetaInfo> serviceDiscovery(String serviceKey);


    /**
     * 服务销毁
     */
    void destroy();

}
