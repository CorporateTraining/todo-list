package com.thoughtworks.todo_list;

import android.app.Application;

import androidx.room.Room;

import com.thoughtworks.todo_list.repository.AppDatabase;
import com.thoughtworks.todo_list.repository.task.TaskRepositoryImpl;
import com.thoughtworks.todo_list.repository.task.model.TaskModel;
import com.thoughtworks.todo_list.repository.user.UserRepositoryImpl;
import com.thoughtworks.todo_list.ui.login.UserRepository;
import com.thoughtworks.todo_list.ui.task.TaskRepository;

import java.util.Date;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MainApplication extends Application {
    private AppDatabase appDatabase;
    private UserRepository userRepository;
    private TaskRepository taskRepository;

    @Override
    public void onCreate() {
        super.onCreate();
        appDatabase = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, this.getClass().getSimpleName()).build();
        userRepository = new UserRepositoryImpl(appDatabase.userDBDataSource());
        taskRepository = new TaskRepositoryImpl(appDatabase.taskDataSource());
        initTask();
    }

    public UserRepository userRepository() {
        return userRepository;
    }

    public TaskRepository getTaskRepository() {
        return taskRepository;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        appDatabase.close();
    }

    public void initTask() {
        for (int i = 0; i < 10; i++) {
            Date date = new Date();
            TaskModel task = new TaskModel("大家好，今天中午吃大餐" + i, "description", date, date, i <= 6, true);
            taskRepository.save(task)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe();
        }
    }
}
