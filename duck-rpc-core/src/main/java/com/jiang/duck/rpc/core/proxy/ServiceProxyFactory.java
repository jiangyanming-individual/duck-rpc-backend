package com.jiang.duck.rpc.core.proxy;

import com.jiang.duck.rpc.core.RpcApplication;

import java.lang.reflect.Proxy;

/**
 * 静态代理工厂模式
 */
public class ServiceProxyFactory {

    /**
     * 生成代理
     *
     * @param serviceClass
     * @param <T>          ClassLoader: 用于定义代理类的Classloader。
     *                     Class[] interfaces: 表示需要代理的接口，即serviceClass。
     *                     InvocationHandler: 代理对象，
     *                     这里使用的是new ServiceProxy()来处理代理方法的调用。
     * @return
     */
    public static <T> T getProxy(Class<?> serviceClass) {

        //如果开启模拟服务对象：
        if (RpcApplication.getRpcConfig().getMock()) {
            return getMockProxy(serviceClass);
        }

        //反之直接返回代理对象：
        return (T) Proxy.newProxyInstance(
                serviceClass.getClassLoader(),
                new Class[]{serviceClass},
                new ServiceProxy());
    }

    /**
     * 得到mock 代理对象：
     *
     * @param serviceClass
     * @param <T>
     * @return
     */
    public static <T> T getMockProxy(Class<?> serviceClass) {
        return (T) Proxy.newProxyInstance(
                serviceClass.getClassLoader(),
                new Class[]{serviceClass},
                new MockProxy()); //模拟服务代理类
    }

}
