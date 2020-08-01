package com.thoughtworks.todo_list.ui.task.model;

import com.thoughtworks.todo_list.repository.task.entity.Task;

import java.io.Serializable;
import java.util.Date;

public class TaskModel implements Serializable {
    private String id;
    private String title;
    private String description;
    private Date date;
    private Date createDate;
    private Boolean isChecked;
    private Boolean isRemind;

    public TaskModel() {
    }

    public TaskModel(String title, String description, Date date, Boolean isChecked, Boolean isRemind) {
        this.title = title;
        this.description = description;
        this.date = date;
        this.isChecked = isChecked;
        this.isRemind = isRemind;
    }

    public String getId() {
        return id;
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

    public void setChecked(Boolean checked) {
        isChecked = checked;
    }

    public Boolean getRemind() {
        return isRemind;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setRemind(Boolean remind) {
        isRemind = remind;
    }

    public TaskModel build(Task task) {
        this.id = task.getId();
        this.title = task.getTitle();
        this.description = task.getDescription();
        this.date = task.getDate();
        this.createDate = task.getCreateDate();
        this.isChecked = task.getChecked();
        this.isRemind = task.getRemind();
        return this;
    }

}
