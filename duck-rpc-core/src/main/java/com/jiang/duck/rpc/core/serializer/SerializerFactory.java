package com.jiang.duck.rpc.core.serializer;
import com.jiang.duck.rpc.core.constants.SerializerKeys;
import com.jiang.duck.rpc.core.serializer.impl.HessianSerializer;
import com.jiang.duck.rpc.core.serializer.impl.JdkSerializer;
import com.jiang.duck.rpc.core.serializer.impl.JsonSerializer;
import com.jiang.duck.rpc.core.serializer.impl.KryoSerializer;
import com.jiang.duck.rpc.core.spi.SpiLoader;

import java.util.HashMap;

/**
 * 单例模式 + 工厂模式 动态生成序列化
 */
public class SerializerFactory {


    /**
     * 静态代码块
     */
    static {
        SpiLoader.load(Serializer.class);
    }

    /***
     * 使用HashMap存储
     */

//    private static final HashMap<String, Serializer> KEY_SERIALIZER_MAP = new HashMap<String, Serializer>(){{
//        put(SerializerKeys.JDK,new JdkSerializer());
//        put(SerializerKeys.JSON, new JsonSerializer());
//        put(SerializerKeys.KRYO,new KryoSerializer());
//        put(SerializerKeys.HESSIAN,new HessianSerializer());
//    }};


    /**
     * 默认序列化器：
      */
    private static final Serializer DEFAULT_SERIALIZER =new JdkSerializer();

    /**
     *根据key 返回具体的序列化器
     * @param key
     * @return
     */
    public static Serializer getInstance(String key){
//        return KEY_SERIALIZER_MAP.getOrDefault(key,DEFAULT_SERIALIZER);
        return SpiLoader.getInstance(Serializer.class,key);
    }

}
