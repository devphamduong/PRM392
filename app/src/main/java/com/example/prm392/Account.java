package com.example.prm392;

public class Account {
    private String id;
    private String avatar;
    private String fullname;
    private String gender;
    private String dob;
    private String mobile;
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
    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
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

    public Account(String gender, String dob, String mobile) {
        this.gender = gender;
        this.dob = dob;
        this.mobile = mobile;
    }

    public Account(String id, String avatar, String fullname, String gender, String dob, String mobile, String userName, String email, String password, int roleId) {
        this.id = id;
        this.avatar = avatar;
        this.fullname = fullname;
        this.gender = gender;
        this.dob = dob;
        this.mobile = mobile;
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.roleId = roleId;
    }
}
