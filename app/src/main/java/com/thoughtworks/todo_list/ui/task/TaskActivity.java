package com.thoughtworks.todo_list.ui.task;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.thoughtworks.todo_list.MainApplication;
import com.thoughtworks.todo_list.R;
import com.thoughtworks.todo_list.ui.login.LoginActivity;
import com.thoughtworks.todo_list.ui.task.model.TaskModel;

import java.util.Date;

import static com.thoughtworks.todo_list.ui.login.LoginActivity.SHARED_ID;
import static com.thoughtworks.todo_list.ui.login.LoginActivity.SHARED_NAME;
import static com.thoughtworks.todo_list.ui.login.LoginActivity.SHARED_USER;

public class TaskActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TextView weekAndDay, month, taskNumber;
    private Button createButton;
    private RecyclerView recyclerView;
    private MyAdapter myAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private TaskViewModel taskViewModel;
    private SharedPreferences preferences;
    public final static String TASK_VIEW_KEY = "TASK_MODEL_KEY";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activiey_task);
        toolbar = findViewById(R.id.task_toolbar);
        setActionBar(toolbar);
        toolbar.inflateMenu(R.menu.home_menu);
        weekAndDay = findViewById(R.id.week_and_day);
        createButton = findViewById(R.id.create_task_button);
        month = findViewById(R.id.month);
        taskNumber = findViewById(R.id.task_number);
        setDateToTitle();
        taskViewModel = obtainViewModel();
        circularDisplayTask();
        preferences = getSharedPreferences(SHARED_USER, Context.MODE_PRIVATE);
        Integer userId = preferences.getInt(SHARED_ID, 0);
        taskViewModel.getTasks(userId);
        listenerUserLogout();
        listenerJumpCreateTaskPage();
    }

    private void listenerJumpCreateTaskPage() {
        createButton.setOnClickListener(view -> {
            Intent intent = new Intent(TaskActivity.this, CreateTaskActivity.class);
            startActivity(intent);
        });
    }

    private void circularDisplayTask() {
        taskViewModel.observeTasks(this, tasks -> {
            recyclerView = findViewById(R.id.my_recycle_view);
            recyclerView.setHasFixedSize(true);
            layoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(layoutManager);
            myAdapter = new MyAdapter(tasks, taskViewModel, this);
            recyclerView.setAdapter(myAdapter);
            recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
            taskNumber.setText(String.format("%s个任务", tasks.size()));
        });
    }

    private void listenerUserLogout() {
        toolbar.setOnMenuItemClickListener(menuItem -> {
            switch (menuItem.getItemId()) {
                case R.id.logout:
                    removeUserInfoJumpLoginPage();
                    break;
                default:
                    break;
            }
            return true;
        });
    }

    private void removeUserInfoJumpLoginPage() {
        SharedPreferences.Editor edit = preferences.edit();
        edit.remove(SHARED_ID);
        edit.remove(SHARED_NAME);
        edit.apply();
        Intent intent = new Intent(TaskActivity.this, LoginActivity.class);
        startActivity(intent);
        Toast.makeText(this, getResources().getText(R.string.success_logout), Toast.LENGTH_SHORT).show();
    }

    private void setDateToTitle() {
        Date date = new Date();
        String weekFormat = DateFormat.format("EEEE", date).toString();
        String monthFormat = DateFormat.format("MMM", date).toString();
        String dayFormat = DateFormat.format("dd", date).toString();
        weekAndDay.setText(String.format("%s, %sth", weekFormat, dayFormat));
        month.setText(monthFormat);
    }

    private TaskViewModel obtainViewModel() {
        TaskRepository taskRepository = (((MainApplication) getApplicationContext())).getTaskRepository();
        TaskViewModel taskViewModel = new ViewModelProvider(this).get(TaskViewModel.class);
        taskViewModel.setTaskRepository(taskRepository);
        return taskViewModel;
    }

    public void jumpCreateTaskPage(TaskModel taskModel){
        Intent intent = new Intent(TaskActivity.this, CreateTaskActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(TASK_VIEW_KEY, taskModel);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}