package com.thoughtworks.todo_list.repository.task.model;

import java.util.Date;

public class TaskRequest {
    private String id;
    private String title;
    private String description;
    private Date date;
    private Date createDate;
    private Boolean isChecked;
    private Boolean isRemind;

    public TaskRequest(String title, String description, Date date, Date createDate, Boolean isChecked, Boolean isRemind) {
        this.title = title;
        this.description = description;
        this.date = date;
        this.createDate = createDate;
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

    public Boolean getRemind() {
        return isRemind;
    }
}
