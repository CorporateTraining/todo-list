package com.thoughtworks.todo_list.repository.task;

import com.thoughtworks.todo_list.repository.task.entity.Task;
import com.thoughtworks.todo_list.ui.task.TaskRepository;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Single;

public class TaskRepositoryImpl implements TaskRepository {
    private TaskDataSource dataSource;
    public TaskRepositoryImpl(TaskDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Maybe<Task> findById(String name) {
        return dataSource.findById(name);
    }

    @Override
    public Single<List<Task>> findTasks() {
        return dataSource.findTasks();
    }

    @Override
    public Completable deleteTask(Task task) {
        return dataSource.deleteTask(task);
    }

    @Override
    public Completable updateTask(Task task) {
        return dataSource.updateTask(task);
    }

    @Override
    public Completable save(Task task) {
        return dataSource.save(task);
    }
}