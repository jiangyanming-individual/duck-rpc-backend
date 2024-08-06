package com.jiang.duck.rpc.core;

import com.jiang.duck.rpc.core.config.RpcConfig;
import com.jiang.duck.rpc.core.constants.RpcConstant;
import com.jiang.duck.rpc.core.utils.ConfigUtils;
import lombok.extern.slf4j.Slf4j;


/**
 * 使用单例双重检测锁模式加载配置类
 */
@Slf4j
public class RpcApplication {


    //volatile 关键字
    private static volatile RpcConfig rpcConfig;

    /**
     * 使用双重检测锁模式
     *
     * @return
     */
    public static RpcConfig getRpcConfig() {

        if (rpcConfig == null) {
            //加锁
            synchronized (RpcApplication.class) {
                if (rpcConfig == null) {
                    //初始化
                    init();
                }
            }
        }
        return rpcConfig;
    }

    /**
     * 初始化
     */
    public static void init() {
        RpcConfig newRpcConfig;
        try {
            //读取配置文件，返回一个配置类
            newRpcConfig = ConfigUtils.loadConfig(RpcConfig.class, RpcConstant.DEFAULT_CONFIG_PREFIX);
        } catch (Exception e) {
            //加载失败,使用默认值
            newRpcConfig = new RpcConfig();
        }
        init(newRpcConfig); //调用下面的init，打印日志：
    }

    /**
     * 赋值rpcConfig
     *
     * @param newRpcConfig
     */
    public static void init(RpcConfig newRpcConfig) {
        //打印日志：
        log.info("rpc init, config={}", newRpcConfig.toString());
        //赋值给rpcConfig;
        rpcConfig = newRpcConfig;
    }

}
