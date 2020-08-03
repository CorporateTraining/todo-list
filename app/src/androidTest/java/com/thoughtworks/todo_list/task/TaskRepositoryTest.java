package com.thoughtworks.todo_list.task;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.room.Room;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.thoughtworks.todo_list.repository.AppDatabase;
import com.thoughtworks.todo_list.repository.task.TaskRepositoryImpl;
import com.thoughtworks.todo_list.repository.task.entity.Task;
import com.thoughtworks.todo_list.repository.user.UserRepositoryImpl;
import com.thoughtworks.todo_list.repository.user.entity.User;
import com.thoughtworks.todo_list.repository.utils.Encryptor;
import com.thoughtworks.todo_list.ui.login.UserRepository;
import com.thoughtworks.todo_list.ui.task.TaskRepository;
import com.thoughtworks.todo_list.ui.task.model.TaskModel;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import io.reactivex.schedulers.Schedulers;


@RunWith(AndroidJUnit4.class)
public class TaskRepositoryTest {
    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();
    private AppDatabase appDatabase;
    private TaskRepository taskRepository;
    private TaskModel taskModel;
    private String CREATE_TITLE = "title";
    private String CREATE_DESCRIPTION = "description";
    private Date DATE = new Date();
    private Date CREATE_DATE = new Date();
    private boolean IS_CHECKED = false;
    private boolean IS_REMIND = true;
    private int CREATE_USER_ID = 1;

    @Before
    public void setUp() {

        appDatabase = Room.inMemoryDatabaseBuilder(
                InstrumentationRegistry.getInstrumentation().getTargetContext(),
                AppDatabase.class).build();
        taskRepository = new TaskRepositoryImpl(appDatabase.taskDataSource());
        taskModel = new TaskModel(
                UUID.randomUUID().toString().replace("-", "").toLowerCase(),
                CREATE_TITLE,
                CREATE_DESCRIPTION,
                DATE,
                CREATE_DATE,
                IS_CHECKED,
                IS_REMIND,
                CREATE_USER_ID);
    }

    @After
    public void tearDown() {
        appDatabase.close();
    }

    @Test
    public void should_return_list_tasks_where_given_correct_user_id() {

        appDatabase.taskDataSource().save(new Task()
                .build(taskModel))
                .subscribeOn(Schedulers.io())
                .subscribe();
        taskRepository.findTasks(CREATE_USER_ID)
                .test()
                .assertValue(taskModels -> taskModels.size() == 1);
        taskRepository.findTasks(CREATE_USER_ID)
                .test()
                .assertValue(taskModels -> taskModels.get(0).getUserId() == CREATE_USER_ID);
    }
}