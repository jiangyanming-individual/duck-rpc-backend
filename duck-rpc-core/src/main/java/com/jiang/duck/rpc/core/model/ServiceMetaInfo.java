package com.jiang.duck.rpc.core.model;


import cn.hutool.core.util.StrUtil;
import lombok.Data;

/**
 * 服务节点信息
 */
@Data
public class ServiceMetaInfo {


    /**
     * 服务名
     */
    private String serviceName;


    /**
     * 服务版本
     */
    private String serviceVersion="1.0";

    /**
     * 服务域名
     */
    private String serviceHost;

    /**
     * 服务端口号, 类型为Integer
     */
    private Integer servicePort;

    /**
     * 服务默认分组：
     */
    private String serviceGroup="default";

    /**
     * 获取服务的键名
     * @return
     */
    public String getServiceKey() {
        return String.format("%s:%s",serviceName,serviceVersion);
    }

    /**
     * 获取服务节点的名
     * @return
     */
    public String getServiceNodeKey() {
        // serviceName:serviceVersion/serviceHost:servicePort
        return String.format("%s/%s:%s",getServiceKey(),getServiceHost(),getServicePort());
    }


    /**
     * 获取节点服务的地址
     * @return
     */
    public String getServiceAddress() {

        //拼接域名和端口号,如果serviceHost不包含http
        if (!StrUtil.contains(serviceHost,"http")){
            return String.format("http://%s:%s",serviceHost,servicePort);
        }

        //如果serviceHost直接包含了http,
        return String.format("%s:%s",serviceHost,servicePort);
    }
}
