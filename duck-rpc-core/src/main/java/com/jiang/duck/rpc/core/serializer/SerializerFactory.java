package com.jiang.duck.rpc.core.serializer;

import com.jiang.duck.rpc.core.serializer.impl.HessianSerializer;
import com.jiang.duck.rpc.core.serializer.impl.JdkSerializer;
import com.jiang.duck.rpc.core.serializer.impl.JsonSerializer;
import com.jiang.duck.rpc.core.serializer.impl.KryoSerializer;

import java.util.HashMap;
/**
 * 序列化器工厂模式
 */
public class SerializerFactory {
    /**
     * 初始化序列化器map：
     */
    private static final HashMap<String, Serializer> KEY_SERIALIZER_MAP = new HashMap<String, Serializer>() {
        {
            put("jdk", new JdkSerializer());
            put("json", new JsonSerializer());
            put("hessian", new HessianSerializer());
            put("kryo", new KryoSerializer());
        }
    };

    /**
     * 设置默认的序列化器：jdk
     */
    private static final Serializer DEFAULT_SERIALIZER=KEY_SERIALIZER_MAP.get("jdk");


    /**
     * 如果找不到就使用默认的序列化器：jdk
     * @param key
     * @return
     */
    public static Serializer getInstanceSerializer(String key){

        return KEY_SERIALIZER_MAP.getOrDefault(key,DEFAULT_SERIALIZER);
    }

}
