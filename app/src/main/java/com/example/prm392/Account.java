package com.example.prm392;

public class Account {
    private String id;
    private String avatar;
    private String userName;
    private String email;
    private String password;
    private int roleId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public Account() {
    }

    public Account(String id, String avatar, String userName, String email, String password, int roleId) {
        this.id = id;
        this.avatar = avatar;
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.roleId = roleId;
    }
}
