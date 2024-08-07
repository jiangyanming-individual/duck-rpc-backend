package com.jiang.example.common.service;

import com.jiang.example.common.model.User;

/**
 * 接口
 */
public interface UserService {


    /**
     * 获取用户名的方法：
     * @param user
     * @return
     */
    User getUser(User user);


    /**
     * 返回默认
     * @return
     */
    default short getNumber() {
        return 1;
    }
}
