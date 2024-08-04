package com.jiang.example.consumer;


import com.jiang.example.common.model.User;
import com.jiang.example.common.service.UserService;

/**
 * 服务调用者
 */
public class EasyConsumerExample {
    public static void main(String[] args) {
        User user = new User("duck");

        UserService userService = null;
        User newUser = userService.getUser(user);
        if (newUser != null) {
            System.out.println("得到用户的名字：" + newUser.getUsername());
        } else {
            System.out.println("user == null");
        }
    }
}
