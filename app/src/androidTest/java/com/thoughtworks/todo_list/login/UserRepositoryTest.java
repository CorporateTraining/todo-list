package com.thoughtworks.todo_list.login;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.room.Room;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.thoughtworks.todo_list.repository.AppDatabase;
import com.thoughtworks.todo_list.repository.utils.Encryptor;
import com.thoughtworks.todo_list.ui.login.UserRepository;
import com.thoughtworks.todo_list.repository.user.UserRepositoryImpl;
import com.thoughtworks.todo_list.repository.user.entity.User;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.reactivex.schedulers.Schedulers;


@RunWith(AndroidJUnit4.class)
public class UserRepositoryTest {
    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private AppDatabase appDatabase;

    private UserRepository userRepository;

    @Before
    public void setUp() {
        appDatabase = Room.inMemoryDatabaseBuilder(
                InstrumentationRegistry.getInstrumentation().getTargetContext(),
                AppDatabase.class).build();
        userRepository = new UserRepositoryImpl(appDatabase.userDBDataSource());
    }

    @After
    public void tearDown() {
        appDatabase.close();
    }

    @Test
    public void should_find_correct_user() {
        final String CREATE_USER_NAME = "xiaoming";
         final Integer CREATE_USER_ID = 1;
         final String CREATE_PASSWORD = "123456";
        User savedUser = new User(CREATE_USER_ID, CREATE_USER_NAME, Encryptor.md5(CREATE_PASSWORD));
        appDatabase.userDBDataSource().save(savedUser).subscribeOn(Schedulers.io()).subscribe();
        userRepository.findByName(CREATE_USER_NAME).test()
                .assertValue(user -> user.getId() == savedUser.getId());
    }
}