package com.jiang.duck.rpc.easy.proxy;

import java.lang.reflect.Proxy;

/**
 * 静态代理工厂模式
 */
public class ServiceProxyFactory {


    /**
     * 生成代理
     * @param serviceClass
     * @return
     * @param <T>
     *
     *
     * ClassLoader: 用于定义代理类的Classloader。
     * Class[] interfaces: 表示需要代理的接口，即serviceClass。
     * InvocationHandler: 代理对象，
     * 这里使用的是new ServiceProxy()来处理代理方法的调用。
     */
    public static <T> T getProxy(Class<?> serviceClass) {
        return (T) Proxy.newProxyInstance(
                serviceClass.getClassLoader(),
                new Class[]{serviceClass},
                new ServiceProxy());
    }
}
