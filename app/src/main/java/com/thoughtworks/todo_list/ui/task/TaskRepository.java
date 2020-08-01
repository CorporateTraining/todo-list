package com.thoughtworks.todo_list.ui.task;

import com.thoughtworks.todo_list.ui.task.model.TaskModel;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Single;

public interface TaskRepository {
    Maybe<TaskModel> findById(String taskId);

    Single<List<TaskModel>> findTasks(Integer userId);

    Completable deleteTask(TaskModel task);

    Completable updateTask(TaskModel task);

    Completable save(TaskModel task);
}
