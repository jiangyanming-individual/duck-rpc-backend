package com.jiang.example.provider;

import com.jiang.example.common.model.User;
import com.jiang.example.common.service.UserService;

/**
 * 服务实现类,进行测试
 */
public class UserServiceImpl implements UserService {
    @Override
    public User getUser(User user) {
        System.out.println("get User name is：" + user.getUsername());
        return user;
    }
}
