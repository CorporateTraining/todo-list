package com.thoughtworks.todo_list.ui.task;

import com.thoughtworks.todo_list.repository.task.entity.Task;
import com.thoughtworks.todo_list.repository.user.entity.User;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Single;

public interface TaskRepository {
    Maybe<Task> findById(String taskId);

    Single<List<Task>> findTasks();

    Completable deleteTask(Task task);

    Completable updateTask(Task task);

    Completable save(Task task);
}
