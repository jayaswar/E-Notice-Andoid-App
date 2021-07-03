package com.app.e_notice;

import java.io.Serializable;

public class Group implements Serializable {

    int id;
    String name, admin, username, password, created_at;

    public Group() {
    }

    public Group(String name) {

        this.name = name;
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

    public String getAdmin() {
        return admin;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
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

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    @Override
    public String toString() {
        return "Group{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", admin='" + admin + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", created_at='" + created_at + '\'' +
                '}';
    }
}
