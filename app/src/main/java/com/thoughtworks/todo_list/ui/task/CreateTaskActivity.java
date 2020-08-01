package com.thoughtworks.todo_list.ui.task;

import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.thoughtworks.todo_list.MainApplication;
import com.thoughtworks.todo_list.R;
import com.thoughtworks.todo_list.repository.task.entity.Task;
import com.thoughtworks.todo_list.ui.task.model.TaskModel;

import java.text.ParseException;
import java.util.Date;
import java.util.Locale;

public class CreateTaskActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private EditText createTitle, createDescription;
    private TextView createDateInfo;
    private CheckBox createIsChecked;
    private Switch createIsRemind;
    private CalendarView calendarView;
    private Button createSubmit;
    private TaskViewModel taskViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task);
        toolbar = findViewById(R.id.create_task_toolbar);
        createTitle = findViewById(R.id.create_title);
        createDescription = findViewById(R.id.create_description);
        createIsChecked = findViewById(R.id.create_check_box);
        createIsRemind = findViewById(R.id.create_switch);
        createIsRemind = findViewById(R.id.create_switch);
        calendarView = findViewById(R.id.calendar_view);
        createDateInfo = findViewById(R.id.create_date_info);
        createSubmit = findViewById(R.id.create_button);

        taskViewModel = obtainViewModel();
        toolbar.setNavigationOnClickListener(v -> finish());
        calendarView.setVisibility(View.INVISIBLE);
        createSubmit.setEnabled(false);
        createDateInfo.setOnClickListener(view -> {
            this.calendarView.setVisibility(View.VISIBLE);
            createSubmit.setEnabled(true);
        });
        calendarView.setOnDateChangeListener((calendarView, year, month, day) -> {
            this.calendarView.setVisibility(View.INVISIBLE);
            this.createDateInfo.setText(String.format("%s年%s月%s日", year, month + 1, day));
            this.createDateInfo.setTextColor(ContextCompat.getColor(this, R.color.colorTextBlue));
        });
        createSubmit.setOnClickListener(view -> {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(" yyyy年MM月dd日", Locale.CHINA);
            try {
                TaskModel taskModel = new TaskModel(
                        createTitle.getText().toString(),
                        createDescription.getText().toString(),
                        simpleDateFormat.parse(createDateInfo.getText().toString()),
                        createIsChecked.isChecked(),
                        createIsRemind.isChecked());
                taskViewModel.saveTask(taskModel);
                Intent intent = new Intent(CreateTaskActivity.this, TaskActivity.class);
                startActivity(intent);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        });

    }

    private TaskViewModel obtainViewModel() {
        TaskRepository taskRepository = (((MainApplication) getApplicationContext())).getTaskRepository();
        TaskViewModel taskViewModel = new ViewModelProvider(this).get(TaskViewModel.class);
        taskViewModel.setTaskRepository(taskRepository);
        return taskViewModel;
    }
}