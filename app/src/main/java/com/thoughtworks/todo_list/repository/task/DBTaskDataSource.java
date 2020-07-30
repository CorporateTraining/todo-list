package com.thoughtworks.todo_list.repository.task;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;


import com.thoughtworks.todo_list.repository.task.entity.Task;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Single;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface DBTaskDataSource extends TaskDataSource {
    @Query("SELECT * FROM task WHERE id = :id")
    Maybe<Task> findById(String id);

    @Query("SELECT * FROM task")
    Single<List<Task>> findTasks();

    @Delete
    Completable deleteTask(Task task);

    @Update
    Completable updateTask(Task task);

    @Insert(onConflict = REPLACE)
    Completable save(Task task);
}
