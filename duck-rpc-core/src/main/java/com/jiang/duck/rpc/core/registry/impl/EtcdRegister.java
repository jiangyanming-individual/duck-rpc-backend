package com.jiang.duck.rpc.core.registry.impl;

import cn.hutool.json.JSONUtil;
import com.jiang.duck.rpc.core.config.RegisterConfig;
import com.jiang.duck.rpc.core.model.ServiceMetaInfo;
import com.jiang.duck.rpc.core.registry.Register;
import io.etcd.jetcd.*;
import io.etcd.jetcd.options.GetOption;
import io.etcd.jetcd.options.PutOption;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * etcdRegistry: etcd注册中心实现：
 */

@Slf4j
public class EtcdRegister implements Register {


    /**
     * etcd 默认路径：
     */
    public static final String ETCD_ROOT_UTL = "/rpc/";

    private Client client;

    private KV kvClient;

    /**
     * 服务初始化，创建对应的kv 客户端
     *
     * @param registerConfig
     */
    @Override
    public void init(RegisterConfig registerConfig) {
        // 创建客户端节点：
        client = Client
                .builder()
                .endpoints(registerConfig.getAddress())
                .connectTimeout(Duration.ofMinutes(registerConfig.getTimeout()))
                .build();
        System.out.println("client connect success ...");
        //创建kv 客户端
        kvClient = client.getKVClient();
    }

    /**
     * 服务注册
     *
     * @param serviceMetaInfo
     * @throws Exception
     */
    @Override
    public void registry(ServiceMetaInfo serviceMetaInfo) throws Exception {
        //创建一个租赁的客户端
        Lease leaseClient = client.getLeaseClient();
        long leaseId = leaseClient.grant(30).get().getID();

        //设置要存储的key, value:
        String registryKey = ETCD_ROOT_UTL + serviceMetaInfo.getServiceNodeKey();
        ByteSequence key = ByteSequence.from(registryKey, StandardCharsets.UTF_8);
        ByteSequence value = ByteSequence.from(JSONUtil.toJsonStr(serviceMetaInfo), StandardCharsets.UTF_8);

        //将键值对和租赁时间对应起来
        PutOption putOption = PutOption.builder().withLeaseId(leaseId).build();
        kvClient.put(key, value, putOption);
    }

    /**
     * 服务注销：
     *
     * @param serviceMetaInfo
     */
    @Override
    public void unRegistry(ServiceMetaInfo serviceMetaInfo) {
        String key = ETCD_ROOT_UTL + serviceMetaInfo.getServiceNodeKey();
        ByteSequence byteSequence = ByteSequence.from(key, StandardCharsets.UTF_8);
        kvClient.delete(byteSequence);
    }

    @Override
    public List<ServiceMetaInfo> serviceDiscovery(String serviceKey) {

        System.out.println("服务的key：" + serviceKey);
        String searchPrefix = ETCD_ROOT_UTL + serviceKey + "/";
        List<ServiceMetaInfo> serviceMetaInfoList = new ArrayList<>();
        //前缀查询；
        try {
            GetOption getOption = GetOption.builder().isPrefix(true).build();
            List<KeyValue> keyValueList = kvClient
                    .get(ByteSequence.from(searchPrefix, StandardCharsets.UTF_8), getOption)
                    .get()
                    .getKvs();
            System.out.println("keyValueList:" + keyValueList);
            //解析服务：
            serviceMetaInfoList = keyValueList
                    .stream()
                    .map((keyValue) -> {
                        //得到value,转为Bean 对象：
                        String value = keyValue.getValue().toString(StandardCharsets.UTF_8);
                        return JSONUtil.toBean(value, ServiceMetaInfo.class);
                    }).collect(Collectors.toList());
            return serviceMetaInfoList;
        } catch (Exception e) {
            throw new RuntimeException("获取服务列表失败", e);
        }
    }

    /**
     * 服务节点销毁
     */
    @Override
    public void destroy() {
        log.info("当前节点下线。。。");
        if (kvClient != null) {
            kvClient.close();
        }
        if (client != null) {
            client.close();
        }
    }
}
