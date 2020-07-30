package com.thoughtworks.todo_list.ui.task;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.thoughtworks.todo_list.R;

public class TaskListItemActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list_item);
    }
}