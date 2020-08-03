package com.thoughtworks.todo_list.repository.task;

import com.thoughtworks.todo_list.repository.task.entity.Task;
import com.thoughtworks.todo_list.ui.task.model.TaskModel;
import com.thoughtworks.todo_list.ui.task.TaskRepository;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Single;

public class TaskRepositoryImpl implements TaskRepository {
    private TaskDataSource dataSource;

    public TaskRepositoryImpl(TaskDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Maybe<TaskModel> findById(String taskId) {
        return dataSource.findById(taskId).map(task -> new TaskModel().build(task));
    }

    @Override
    public Single<List<TaskModel>> findTasks(Integer userId) {
        return dataSource.findTasks(userId).map(tasks -> {
            List<TaskModel> taskRespons = new ArrayList<>();
            for (Task task : tasks) {
                TaskModel taskModel = new TaskModel().build(task);
                taskRespons.add(taskModel);
            }
            return taskRespons;
        });
    }

    @Override
    public Completable deleteTask(TaskModel taskModel) {
        Task task = new Task().build(taskModel);
        return dataSource.deleteTask(task);
    }

    @Override
    public Completable updateTask(TaskModel taskModel) {
        Task task = new Task().build(taskModel);
        return dataSource.updateTask(task);
    }

    @Override
    public Completable save(TaskModel taskModel) {
        Task task = new Task().build(taskModel);
        return dataSource.save(task);
    }
}