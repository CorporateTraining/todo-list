package com.thoughtworks.todo_list.repository.user.model;

import com.thoughtworks.todo_list.repository.user.entity.User;

public class UserModel {
    private int id;
    private String name;
    private String password;

    public UserModel() {
    }

    public UserModel(int id, String name, String password) {
        this.id = id;
        this.name = name;
        this.password = password;
    }

    public UserModel build(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.password = user.getPassword();
        return this;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }
}
