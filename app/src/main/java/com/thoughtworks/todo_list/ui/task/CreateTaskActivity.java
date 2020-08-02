package com.thoughtworks.todo_list.ui.task;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.thoughtworks.todo_list.ui.task.model.TaskModel;

import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

import static com.thoughtworks.todo_list.ui.login.LoginActivity.SHARED_ID;
import static com.thoughtworks.todo_list.ui.login.LoginActivity.SHARED_NAME;
import static com.thoughtworks.todo_list.ui.login.LoginActivity.SHARED_USER;
import static com.thoughtworks.todo_list.ui.task.TaskActivity.TASK_VIEW_KEY;
import static com.thoughtworks.todo_list.ui.task.TaskAlarManager.taskAlarManager;

public class CreateTaskActivity extends AppCompatActivity {
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日", Locale.CHINA);
    private Toolbar toolbar;
    private EditText createTitle, createDescription;
    private TextView createDateInfo;
    private CheckBox createIsChecked;
    private Switch createIsRemind;
    private CalendarView calendarView;
    private Button createSubmitButton, deleteButton;
    private TaskViewModel taskViewModel;
    private Boolean hasDate = false;
    private Boolean hasTitle = false;
    private TaskModel taskModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task);
        toolbar = findViewById(R.id.create_task_toolbar);
        createTitle = findViewById(R.id.create_title);
        createDescription = findViewById(R.id.create_description);
        createIsChecked = findViewById(R.id.create_check_box);
        createIsRemind = findViewById(R.id.create_switch);
        calendarView = findViewById(R.id.calendar_view);
        createDateInfo = findViewById(R.id.create_date_info);
        createSubmitButton = findViewById(R.id.create_button);
        deleteButton = findViewById(R.id.delete_button);
        fillingTaskData();

        taskViewModel = obtainViewModel();
        toolbar.setNavigationOnClickListener(v -> finish());
        calendarView.setVisibility(View.INVISIBLE);
        createSubmitButton.setEnabled(hasDate && hasTitle);
        listenerEmptyTitle();
        listenerShowCalendarView();
        listenerSelectCalendarData();
        listenerSubmitTaskData();
    }

    private void fillingTaskData() {
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            taskModel = (TaskModel) bundle.getSerializable(TASK_VIEW_KEY);
            if (taskModel != null) {
                createTitle.setText(taskModel.getTitle());
                createDescription.setText(taskModel.getDescription());
                createIsChecked.setChecked(taskModel.getChecked());
                createIsRemind.setChecked(taskModel.getRemind());
                createDateInfo.setText(simpleDateFormat.format(taskModel.getDate()));
                hasDate = true;
                hasTitle = true;
                this.createDateInfo.setTextColor(ContextCompat.getColor(this, R.color.colorTextBlue));
                deleteButton.setVisibility(View.VISIBLE);
                deleteTaskJumpTaskListPage();
            }
        }
    }

    private void deleteTaskJumpTaskListPage() {
        deleteButton.setOnClickListener(view -> {
            taskViewModel.deleteTask(taskModel);
            jumpTaskPage();
        });
    }

    private void listenerShowCalendarView() {
        createDateInfo.setOnClickListener(view -> {
            int visible = calendarView.getVisibility() == View.VISIBLE ? View.INVISIBLE : View.VISIBLE;
            calendarView.setVisibility(visible);
        });
    }

    private void listenerSubmitTaskData() {
        createSubmitButton.setOnClickListener(view -> {
            try {
                if (taskModel != null) {
                    updateTaskData();
                } else {
                    createTaskData();
                }
                if (isTimerNotification()) {
                    taskAlarManager(this, taskModel);
                }
                jumpTaskPage();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        });
    }

    private boolean isTimerNotification() {
        return taskModel.getRemind() && !taskModel.getChecked() && taskModel.getDate().getTime() > new Date().getTime();
    }

    private void jumpTaskPage() {
        Intent intent = new Intent(CreateTaskActivity.this, TaskActivity.class);
        startActivity(intent);
    }

    private void createTaskData() throws ParseException {
        Integer userId = getUserId();
        Date date = simpleDateFormat.parse(createDateInfo.getText().toString());
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.HOUR, 6);// 24小时制
        date = cal.getTime();
        taskModel = new TaskModel(
                UUID.randomUUID().toString().replace("-", "").toLowerCase(),
                createTitle.getText().toString(),
                createDescription.getText().toString(),
                date,
                new Date(),
                createIsChecked.isChecked(),
                createIsRemind.isChecked(),
                userId);
        taskViewModel.saveTask(taskModel);
    }

    private void updateTaskData() throws ParseException {
        Integer userId = getUserId();
        taskModel.setTitle(createTitle.getText().toString());
        taskModel.setDescription(createDescription.getText().toString());
        taskModel.setDate(simpleDateFormat.parse(createDateInfo.getText().toString()));
        taskModel.setChecked(createIsChecked.isChecked());
        taskModel.setRemind(createIsRemind.isChecked());
        taskModel.setUserId(userId);
        taskViewModel.updateTask(taskModel);
    }

    @NotNull
    private Integer getUserId() {
        SharedPreferences preferences = getSharedPreferences(SHARED_USER, Context.MODE_PRIVATE);
        return preferences.getInt(SHARED_ID, 0);
    }

    private void listenerSelectCalendarData() {
        calendarView.setOnDateChangeListener((calendarView, year, month, day) -> {
            this.calendarView.setVisibility(View.INVISIBLE);
            this.createDateInfo.setText(String.format("%s年%s月%s日", year, month + 1, day));
            this.createDateInfo.setTextColor(ContextCompat.getColor(this, R.color.colorTextBlue));
            createSubmitButton.setEnabled(hasTitle);
            hasDate = true;
        });
    }

    private void listenerEmptyTitle() {
        createTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                hasTitle = createTitle.getText().toString().length() > 0;
                createSubmitButton.setEnabled(hasTitle && hasDate);
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