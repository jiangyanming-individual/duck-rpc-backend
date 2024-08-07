package com.jiang.example.consumer;


import com.jiang.duck.rpc.core.config.RpcConfig;
import com.jiang.duck.rpc.core.proxy.ServiceProxyFactory;
import com.jiang.duck.rpc.core.utils.ConfigUtils;
import com.jiang.example.common.model.User;
import com.jiang.example.common.service.UserService;

/**
 * 服务调用者
 */
public class EasyConsumerExample {
    public static void main(String[] args) {

        //打印配置文件的信息：
        RpcConfig rpc = ConfigUtils.loadConfig(RpcConfig.class, "rpc");
        System.out.println("rpcConfig:" + rpc);
        User user = new User("duck");
        //代理对象静态代理
        // UserServiceProxy  userServiceProxy= new UserServiceProxy();

        //动态代理 + 代理工厂的模式：
        UserService userService = ServiceProxyFactory.getProxy(UserService.class);
        User newUser = userService.getUser(user);
        if (newUser != null) {
            System.out.println("得到用户的名字：" + newUser.getUsername());
        } else {
            System.out.println("user == null");
        }
        System.out.println("number: " + userService.getNumber());
    }
}
