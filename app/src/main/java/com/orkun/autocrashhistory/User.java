package com.orkun.autocrashhistory;

public class User {
    public String userId, name, password;

    public User(){}

    public User(String userId, String name, String password){
        this.userId = userId;
        this.name = name;
        this.password = password;
    }

    public String getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }
}
