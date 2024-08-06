package com.jiang.duck.rpc.easy.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


/**
 * 请求封装类
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RpcRequest implements Serializable {

    private static final long serialVersionUID = -8926894517385434699L;

    //请求服务方法名、请求方法、请求参数类型、请求参数列表

    /**
     * 请求服务的类名
     */
    private String serviceName;

    /**
     * 请求方法名
     */
    private String methodName;

    /**
     * 请求参数类型列表
     */
    private Class<?>[] parameterTypes;

    /**
     * 请求参数列表
     */
    private Object[] args;
}
