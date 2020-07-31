package com.thoughtworks.todo_list.repository.task.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.thoughtworks.todo_list.repository.task.model.TaskRequest;

import java.util.Date;
import java.util.UUID;

@Entity
public class Task {
    @PrimaryKey
    @NonNull
    private String id;
    private String title;
    private String description;
    private Date date;
    private Date createDate;
    private Boolean isChecked;
    private Boolean isRemind;

    public Task() {
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Date getDate() {
        return date;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public Boolean getChecked() {
        return isChecked;
    }

    public Boolean getRemind() {
        return isRemind;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public void setChecked(Boolean checked) {
        isChecked = checked;
    }

    public void setRemind(Boolean remind) {
        isRemind = remind;
    }

    public Task build(TaskRequest task) {
        this.id = task.getId();
        this.title = task.getTitle();
        this.description = task.getDescription();
        this.date = task.getDate();
        this.createDate = task.getCreateDate();
        this.isChecked = task.getChecked();
        this.isRemind = task.getRemind();
        return this;
    }

    public Task create(TaskRequest task) {
        this.id = UUID.randomUUID().toString().replace("-", "").toLowerCase();
        this.title = task.getTitle();
        this.description = task.getDescription();
        this.date = task.getDate();
        this.createDate = new Date();
        this.isChecked = task.getChecked();
        this.isRemind = task.getRemind();
        return this;
    }

}