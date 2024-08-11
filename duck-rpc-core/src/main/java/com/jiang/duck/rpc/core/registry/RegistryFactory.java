package com.jiang.duck.rpc.core.registry;


import com.jiang.duck.rpc.core.registry.impl.EtcdRegister;
import com.jiang.duck.rpc.core.spi.SpiLoader;
import javafx.scene.control.Spinner;

/**
 * 注册中心工厂, 跟序列化工厂一样
 */
public class RegistryFactory {


    /**
     * 静态代码块，随着类的加载而被加载：
     */
    static {
        SpiLoader.load(Register.class);
    }


    /**
     * 默认的注册中心
     */
    private static final Register DEFAULT_REGISTRY = new EtcdRegister();


    /**
     * 获取某一个注册中心的实例，根据key来实现
     * @param key
     * @return
     */
    public static Register getInstance(String key) {
        return SpiLoader.getInstance(Register.class, key);
    }
}
