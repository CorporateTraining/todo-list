package com.thoughtworks.todo_list.repository.task;

import java.util.ArrayList;
import java.util.List;

public class Task {
    public String title;
    public String description;
    public int number;
    public String avatar;

    public Task(String title, String description, int number, String avatar) {
        this.title = title;
        this.description = description;
        this.number = number;
        this.avatar = avatar;
    }

    public static List<Task> initTaskData() {
        List<Task> dataList = new ArrayList<>();
        for (int i = 1; i <= 100; i++) {
            dataList.add(new Task( "title" + i, "desp" + i, i, "https://loremflickr.com/180/180?lock=" + i));
        }
        return dataList;
    }
}
