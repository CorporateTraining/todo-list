package com.thoughtworks.todo_list.ui.task;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.thoughtworks.todo_list.MainApplication;
import com.thoughtworks.todo_list.R;
import com.thoughtworks.todo_list.ui.login.LoginActivity;

import java.util.Date;

public class TaskActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TextView weekAndDay, month, taskNumber;
    private CheckBox itemCheckBox;
    private RecyclerView recyclerView;
    private MyAdapter myAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private TaskViewModel taskViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activiey_task);
        toolbar = findViewById(R.id.task_toolbar);
        toolbar.inflateMenu(R.menu.home_menu);
        weekAndDay = findViewById(R.id.week_and_day);
        itemCheckBox = findViewById(R.id.item_check_box);
        month = findViewById(R.id.month);
        taskNumber = findViewById(R.id.task_number);
        setDateToTitle();
        taskViewModel = obtainViewModel();
        taskViewModel.observeTasks(this, tasks -> {
            recyclerView = findViewById(R.id.my_recycle_view);
            recyclerView.setHasFixedSize(true);
            layoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(layoutManager);
            myAdapter = new MyAdapter(tasks, taskViewModel);
            recyclerView.setAdapter(myAdapter);
            recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
            taskNumber.setText(String.format("%s个任务", tasks.size()));
        });
        taskViewModel.getTasks();
        userLogout();
    }

    private void userLogout() {
        toolbar.setOnMenuItemClickListener(menuItem -> {
            switch (menuItem.getItemId()){
                case R.id.logout:
                    Intent intent = new Intent(TaskActivity.this, LoginActivity.class);
                    startActivity(intent);
                    break;
                default:
                    break;
            }
            return true;
        });
    }

    private void setDateToTitle() {
        Date date = new Date();
        String weekFormat = DateFormat.format("EEEE", date).toString();
        String monthFormat = DateFormat.format("MMM", date).toString();
        String dayFormat = DateFormat.format("dd", date).toString();
        weekAndDay.setText(String.format("%s, %sth", weekFormat,dayFormat));
        month.setText(monthFormat);
    }

    private TaskViewModel obtainViewModel() {
        TaskRepository taskRepository = (((MainApplication) getApplicationContext())).getTaskRepository();
        TaskViewModel taskViewModel = new ViewModelProvider(this).get(TaskViewModel.class);
        taskViewModel.setTaskRepository(taskRepository);
        return taskViewModel;
    }
}