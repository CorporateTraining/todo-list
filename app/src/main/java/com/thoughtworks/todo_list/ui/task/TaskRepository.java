package com.thoughtworks.todo_list.ui.task;

import com.thoughtworks.todo_list.repository.task.model.TaskRequest;
import com.thoughtworks.todo_list.repository.task.model.TaskResponse;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Single;

public interface TaskRepository {
    Maybe<TaskResponse> findById(String taskId);

    Single<List<TaskResponse>> findTasks();

    Completable deleteTask(TaskRequest task);

    Completable updateTask(TaskRequest task);

    Completable save(TaskRequest task);
}
