package com.jiang.test;


import com.jiang.duck.rpc.core.config.RegisterConfig;
import com.jiang.duck.rpc.core.model.ServiceMetaInfo;
import com.jiang.duck.rpc.core.registry.Register;
import com.jiang.duck.rpc.core.registry.impl.EtcdRegister;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class EtcdServiceTest {

     final Register etcdRegister=new EtcdRegister();

    /**
     * 初始化：
     */
    @Before
    public void testInit(){
        RegisterConfig registerConfig = new RegisterConfig();
        registerConfig.setAddress("http://localhost:2379"); //注册中心地址
        etcdRegister.init(registerConfig);
    }


    /**
     * 测试服务
     * @throws Exception
     */
    @Test
    public void testRegister() throws Exception {

        //实例化服务1
        ServiceMetaInfo serviceMetaInfo=new ServiceMetaInfo();
        serviceMetaInfo.setServiceName("myService");
        serviceMetaInfo.setServiceVersion("1.0");
        serviceMetaInfo.setServiceHost("localhost");
        serviceMetaInfo.setServicePort("1234");
        //注册服务
        etcdRegister.registry(serviceMetaInfo);

        //实例化服务2
        ServiceMetaInfo serviceMetaInfo2=new ServiceMetaInfo();
        serviceMetaInfo2.setServiceName("myService");
        serviceMetaInfo2.setServiceVersion("1.0");
        serviceMetaInfo2.setServiceHost("localhost");
        serviceMetaInfo2.setServicePort("1235");
        //注册服务
        etcdRegister.registry(serviceMetaInfo2);


        //实例化服务类：
        serviceMetaInfo=new ServiceMetaInfo();
        serviceMetaInfo.setServiceName("myService");
        serviceMetaInfo.setServiceVersion("2.0");
        serviceMetaInfo.setServiceHost("localhost");
        serviceMetaInfo.setServicePort("1234");
        //注册服务
        etcdRegister.registry(serviceMetaInfo);
    }


    /**
     * 服务注销
     * @throws Exception
     */
    @Test
    public void unRegister() throws Exception {
        //实例化服务类：
        ServiceMetaInfo serviceMetaInfo=new ServiceMetaInfo();
        serviceMetaInfo.setServiceName("myService");
        serviceMetaInfo.setServiceVersion("1.0");
        serviceMetaInfo.setServiceHost("localhost");
        serviceMetaInfo.setServicePort("1234");
        //注册服务
        etcdRegister.unRegistry(serviceMetaInfo);
    }


    /**
     * 服务发现,需要后面执行才能发现
     * @throws Exception
     */
    @Test
    public void discoveryTest() throws Exception {

        ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
        serviceMetaInfo.setServiceName("myService");
        serviceMetaInfo.setServiceVersion("1.0");
        String serviceKey = serviceMetaInfo.getServiceKey();
        System.out.println("serviceKey:" + serviceKey);

        //服务发现：
        List<ServiceMetaInfo> serviceMetaInfoList = etcdRegister.serviceDiscovery(serviceKey);
        System.out.println(serviceMetaInfoList);
        //判断是否为空
        Assert.assertNotNull(serviceMetaInfoList);
    }

}


