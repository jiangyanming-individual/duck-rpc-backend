package com.jiang.example.common.model;

import java.io.Serializable;

public class User implements Serializable {

    private static final long serialVersionUID = 7705870522438297692L;

    private  String username;

    public User() {
    }

    public User(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
