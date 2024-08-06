package com.jiang.duck.rpc.easy.model;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 响应返回类
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RpcResponse implements Serializable {
    private static final long serialVersionUID = 8180777593073435245L;

//    响应数据、响应数据类型、响应信息、异常

    /**
     * 响应的数据：
     */
    private Object data;

    /**
     * 响应数据的类型
     */
    private Class<?> dataType;


    /**
     * 响应信息
     */
    private String message;

    /**
     * 响应异常；
     */
    private Exception exception;
}
