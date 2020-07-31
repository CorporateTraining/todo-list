package com.thoughtworks.todo_list.repository.task;

import com.thoughtworks.todo_list.repository.task.entity.Task;
import com.thoughtworks.todo_list.repository.task.model.TaskRequest;
import com.thoughtworks.todo_list.repository.task.model.TaskResponse;
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

    public Maybe<TaskResponse> findById(String name) {
        return dataSource.findById(name).map(task -> new TaskResponse().build(task));
    }

    @Override
    public Single<List<TaskResponse>> findTasks() {
        return dataSource.findTasks().map(tasks -> {
            List<TaskResponse> taskResponses = new ArrayList<>();
            for (Task task : tasks) {
                TaskResponse taskResponse = new TaskResponse().build(task);
                taskResponses.add(taskResponse);
            }
            return taskResponses;
        });
    }

    @Override
    public Completable deleteTask(TaskRequest taskRequest) {
        Task task = new Task().build(taskRequest);
        return dataSource.deleteTask(task);
    }

    @Override
    public Completable updateTask(TaskRequest taskRequest) {
        Task task = new Task().build(taskRequest);
        return dataSource.updateTask(task);
    }

    @Override
    public Completable save(TaskRequest taskRequest) {
        Task task = new Task().create(taskRequest);
        return dataSource.save(task);
    }
}