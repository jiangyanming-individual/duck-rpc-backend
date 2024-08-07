package com.jiang.duck.rpc.core.proxy;


import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * 模拟数据代理类
 */
@Slf4j
public class MockProxy implements InvocationHandler {

    /**
     * @param proxy  代理类
     * @param method 方法
     * @param args   方的参数
     * @return
     * @throws Throwable
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        //方法返回类型
        Class<?> returnType = method.getReturnType();
        log.info("请求的方法名：" + method.getName());
        return getDefaultObject(returnType);
    }

    /**
     * 获取默认的对象
     * @param type
     * @return
     */
    public Object getDefaultObject(Class<?> type){
        //基本数据类型
        if (type.isPrimitive()){ //判断是不是基本数据类型
            if (type == boolean.class){
                return false;
            }else if (type == int.class){
                return 0;
            }else if (type == long.class){
                return 0L;
            }else if (type == float.class){
                return 0.0;
            }else if (type == double.class){
                return 0.0;
            }else if (type == byte.class){
                return (byte) 0;
            }else if (type == short.class){
                return (short) 0;
            }else if (type == char.class){
                return (char) 0;
            }
        }
        //对象类型
        return null;
    }
}
