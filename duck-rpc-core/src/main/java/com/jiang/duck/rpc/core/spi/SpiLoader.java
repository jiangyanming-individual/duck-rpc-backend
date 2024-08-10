package com.jiang.duck.rpc.core.spi;


import cn.hutool.core.io.resource.ResourceUtil;
import com.jiang.duck.rpc.core.serializer.Serializer;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * spi加载器：
 */

@Slf4j //打上日志：
public class SpiLoader {

    /**
     * 存储已知加载的类： (接口名,(key,实现类))
     */
    private static final Map<String, Map<String, Class<?>>> loadMap = new ConcurrentHashMap<>();

    /**
     * 对象实例缓存：(具体的类路径，对象实例Object)
     */
    private static final Map<String, Object> instanceCache = new ConcurrentHashMap<>();


    /**
     * 系统加载路径
     */
    private static final String RPC_SYSTEM_SPI_DIR = "META-INF/rpc/system/";

    /**
     * 用户自定加载路径：
     */
    private static final String RPC_CUSTOM_SPI_DIR = "META-INF/rpc/custom/";

    /**
     * 动态扫描所有文件路径
     */
    private static final String[] SCAN_DIRS = new String[]{RPC_SYSTEM_SPI_DIR, RPC_CUSTOM_SPI_DIR,};

    /**
     * 加载所有的类为list
     */
    private static final List<Class<?>> LOAD_CLASS_LIST = Arrays.asList(Serializer.class);


    /**
     * 加载所有类
     */
    public static void loadAll() {
        log.info("SPI 加载所有类");
        //依次遍历：加载所有的类：
        for (Class<?> aClass : LOAD_CLASS_LIST) {
            load(aClass);
        }

    }

    /**
     * 加载某一类
     *
     * @param loadClass
     * @return
     */
    public static Map<String, Class<?>> load(Class<?> loadClass) {
        log.info("Spi加载的类为：{}", loadClass.getName());
        //用户自定义SPI，要高于系统定义的SPI

        //用于存储key和对用的class类
        HashMap<String, Class<?>> keyClassMap = new HashMap<>();
        for (String scanDir : SCAN_DIRS) {
            log.info("scanDir:" + scanDir);
            //读取Resource/META-INF/rpc/custom下面的资源：
            List<URL> resources = ResourceUtil.getResources(scanDir + loadClass.getName());
            //读取文件的每一个资源
            for (URL resource : resources) {
                log.info("resource:" + resource);
                try {
                    //字节流转换为字符流：
                    InputStreamReader inputStreamReader = new InputStreamReader(resource.openStream());
                    //字符缓冲流
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        String[] split = line.split("=");
                        if (split.length > 1) {
                            //key:
                            String key = split[0];
                            //类的全路径
                            String className = split[1];
                            //通过类路径加载类(key)
                            keyClassMap.put(key, Class.forName(className));
                        }
                    }
                } catch (Exception e) {
                    log.info("SPI 加载失败：" + e);
                }
            }
        }
        //类的名字,(key,Class<?>)
        loadMap.put(loadClass.getName(), keyClassMap);
        return keyClassMap;
    }

    /**
     * 实例化对象：
     * @param tClass 序列化器接口
     * @param key  具体的key
     * @return
     * @param <T>
     */
    public static <T> T getInstance(Class<?> tClass, String key) {

        //获得接口名
        String tClassName = tClass.getName();
        Map<String, Class<?>> keyClassMap = loadMap.get(tClassName);
        if (keyClassMap == null) {
            throw  new RuntimeException(String.format("SpiLoader 未加载%s类型", tClassName));
        }
        //如果keyClassMap不存在key
        if (!keyClassMap.containsKey(key)) {
            throw  new RuntimeException(String.format("SpiLoader的%s类型, 不存在key=%s的类型", tClassName, key));
        }
        //如果找到对应的key
        Class<?> implClass = keyClassMap.get(key);
        //从实例中进行加载：获取实现类的全路径：
        String implClassName = implClass.getName();
        //如果不存在，就直接实例化然后存储到instanceCache中：
        if (!instanceCache.containsKey(implClassName)){
            try {
                //实例化对象：
                instanceCache.put(implClassName,implClass.newInstance());
            } catch (Exception e) {
                String errorMessage=String.format("%s 实例化失败",implClassName);
                throw new RuntimeException(errorMessage,e);
            }
        }
        //返回对应的实例化对象：
        return (T) instanceCache.get(implClassName);
    }


    /**
     * 测试
     * @param args
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        loadAll();
        System.out.println(loadMap);
        Serializer serializer = getInstance(Serializer.class, "kryo");
        System.out.println(serializer);
    }
}


