package com.thoughtworks.todo_list;

import android.app.Application;

import androidx.room.Room;

import com.thoughtworks.todo_list.repository.AppDatabase;
import com.thoughtworks.todo_list.repository.task.TaskRepositoryImpl;
import com.thoughtworks.todo_list.repository.user.UserRepositoryImpl;
import com.thoughtworks.todo_list.ui.login.UserRepository;
import com.thoughtworks.todo_list.ui.task.TaskRepository;

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

}
