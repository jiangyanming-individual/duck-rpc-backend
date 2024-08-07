package com.jiang.duck.rpc.core.utils;

import cn.hutool.core.util.StrUtil;
import cn.hutool.setting.dialect.Props;

/**
 * 通过读取配置文件获取对应配置类的信息：
 */
public class ConfigUtils {

    /**
     * 加载配置对象：
     * @param tClass 配置类
     * @param prefix 前缀
     * @return
     * @param <T>
     */
    public static <T> T loadConfig(Class<T> tClass, String prefix) {
        return loadConfig(tClass, prefix, "");
    }

    /**
     * 加载配置文件转为配置类：
     * @param tClass
     * @param prefix
     * @param environment
     * @return
     * @param <T>
     */
    public static <T> T loadConfig(Class<T> tClass,String prefix,String environment) {
        StringBuilder configFileBuilder = new StringBuilder("application");
        //environment 不为空的情况下：
        if (StrUtil.isNotBlank(environment)){
            configFileBuilder.append("-").append(environment);
        }
        //不传入environment
        configFileBuilder.append(".properties");
        //hutools properties 读取配置文件转配置类：
        Props props = new Props(configFileBuilder.toString());
        return props.toBean(tClass,prefix);
    }

}
