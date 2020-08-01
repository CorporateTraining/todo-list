package com.thoughtworks.todo_list.repository.task;


import com.thoughtworks.todo_list.repository.task.entity.Task;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Single;

public interface TaskDataSource {
    Maybe<Task> findById(String taskId);

    Single<List<Task>> findTasks(Integer userId);

    Completable deleteTask(Task task);

    Completable updateTask(Task task);

    Completable save(Task task);
}
