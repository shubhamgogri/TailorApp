package com.example.tailorapp.login;

public class UserApI {
    private String username;
    private String userId;
    private static UserApI instance;

    public static UserApI getInstance(){
        if (instance == null){
            instance = new UserApI();
        }
        return instance;
    }

    public UserApI() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
