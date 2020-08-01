package com.thoughtworks.todo_list.repository.user.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.thoughtworks.todo_list.ui.login.model.UserModel;

@Entity
public class User {
    @PrimaryKey
    @NonNull
    private int id;
    private String name;
    private String password;

    public User() {
    }
    @Ignore
    public User(int id, String name, String password) {
        this.id = id;
        this.name = name;
        this.password = password;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public User build(UserModel userModel) {
        this.id = userModel.getId();
        this.name = userModel.getName();
        this.password = userModel.getPassword();
        return this;
    }
}

