package com.app.e_notice;

import java.io.Serializable;

public class Student implements Serializable {

    int id;
    String name, email, bio, branch, username, password, mobile_no, subscribed_group_id,profile_pic;

    public Student() {
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", bio='" + bio + '\'' +
                ", branch='" + branch + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", mobile_no='" + mobile_no + '\'' +
                ", subscribed_group_id='" + subscribed_group_id + '\'' +
                ", profile_pic='" + profile_pic + '\'' +
                '}';
    }

    public String getProfile_pic() {
        return profile_pic;
    }

    public void setProfile_pic(String profile_pic) {
        this.profile_pic = profile_pic;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMobile_no() {
        return mobile_no;
    }

    public void setMobile_no(String mobile_no) {
        this.mobile_no = mobile_no;
    }

    public String getSubscribed_group_id() {
        return subscribed_group_id;
    }

    public void setSubscribed_group_id(String subscribed_group_id) {
        this.subscribed_group_id = subscribed_group_id;
    }

}
