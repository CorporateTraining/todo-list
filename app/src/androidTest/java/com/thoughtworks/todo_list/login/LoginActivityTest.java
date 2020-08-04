package com.thoughtworks.todo_list.login;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.thoughtworks.todo_list.MainApplication;
import com.thoughtworks.todo_list.R;
import com.thoughtworks.todo_list.ui.login.model.UserModel;
import com.thoughtworks.todo_list.repository.utils.Encryptor;
import com.thoughtworks.todo_list.ui.login.LoginActivity;
import com.thoughtworks.todo_list.ui.login.UserRepository;
import com.thoughtworks.todo_list.ui.task.TaskRepository;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;

import java.util.ArrayList;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.Maybe;
import io.reactivex.MaybeObserver;
import io.reactivex.internal.operators.maybe.MaybeCreate;
import io.reactivex.internal.operators.single.SingleCreate;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.hasErrorText;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.thoughtworks.todo_list.ui.login.LoginActivity.SHARED_ID;
import static com.thoughtworks.todo_list.ui.login.LoginActivity.SHARED_NAME;
import static com.thoughtworks.todo_list.ui.login.LoginActivity.SHARED_USER;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;


@RunWith(AndroidJUnit4.class)
public class LoginActivityTest {
    private UserRepository userRepository;
    private TaskRepository taskRepository;
    private UserModel user;
    private MainApplication applicationContext;

    @Rule
    public ActivityTestRule<LoginActivity> mActivityRule = new ActivityTestRule<>(LoginActivity.class, false, false);
    private final String CREATE_USER_NAME = "xiaoming";
    private final Integer CREATE_USER_ID = 1;
    private final String CREATE_PASSWORD = "123456";
    @Before
    public void before() {

        applicationContext = (MainApplication) InstrumentationRegistry.getInstrumentation().getTargetContext().getApplicationContext();
        userRepository = applicationContext.userRepository();
        taskRepository = applicationContext.getTaskRepository();
        removeSharedUser();
        user = new UserModel(CREATE_USER_ID, CREATE_USER_NAME, Encryptor.md5(CREATE_PASSWORD));
        when(userRepository.save(ArgumentMatchers.any())).thenReturn(new Completable() {
            @Override
            protected void subscribeActual(CompletableObserver observer) {
                observer.onComplete();
            }
        });
        when(taskRepository.findTasks(CREATE_USER_ID)).thenReturn(new SingleCreate<>(emitter -> emitter.onSuccess(new ArrayList<>())));
        mActivityRule.launchActivity(null);
    }

    private void removeSharedUser() {
        SharedPreferences preferences = applicationContext.getSharedPreferences(SHARED_USER, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = preferences.edit();
        edit.remove(SHARED_ID);
        edit.remove(SHARED_NAME);
        edit.apply();
    }

    @Test
    public void should_login_successfully_when_login_given_correct_username_and_password() {
        when(userRepository.findByName(CREATE_USER_NAME)).thenReturn(new MaybeCreate<>(emitter -> emitter.onSuccess(user)));

        onView(ViewMatchers.withId(R.id.username)).perform(typeText(CREATE_USER_NAME)).perform(closeSoftKeyboard());
        onView(withId(R.id.password)).perform(typeText(CREATE_PASSWORD)).perform(closeSoftKeyboard());
        onView(withId(R.id.login)).perform(click());
        onView(withText(R.string.login_welcome)).inRoot(withDecorView(not(is(mActivityRule.getActivity().getWindow().getDecorView()))))
                .check(matches(isDisplayed()));
    }

    @Test
    public void should_login_failed_when_login_given_invalid_password() {
        final String WRONG_PASSWORD = "1234567";
        when(userRepository.findByName(CREATE_USER_NAME)).thenReturn(new MaybeCreate<>(emitter -> emitter.onSuccess(user)));

        onView(withId(R.id.username)).perform(typeText(CREATE_USER_NAME)).perform(closeSoftKeyboard());
        onView(withId(R.id.password)).perform(typeText(WRONG_PASSWORD)).perform(closeSoftKeyboard());
        onView(withId(R.id.login)).perform(click());
        onView(withText(R.string.login_failed_password)).inRoot(withDecorView(not(is(mActivityRule.getActivity().getWindow().getDecorView()))))
                .check(matches(isDisplayed()));
    }

    @Test
    public void should_login_failed_when_login_given_username_does_not_exist() {
        final String WRONG_USERNAME = "notexist";
        final String WRONG_PASSWORD = "1234567";
        when(userRepository.findByName(WRONG_USERNAME)).thenReturn(new Maybe<UserModel>() {
            @Override
            protected void subscribeActual(MaybeObserver<? super UserModel> observer) {
                observer.onComplete();
            }
        });
        when(userRepository.getUserByNetwork()).thenReturn(new MaybeCreate<>(emitter -> emitter.onSuccess(user)));
        onView(withId(R.id.username)).perform(typeText(WRONG_USERNAME)).perform(closeSoftKeyboard());
        onView(withId(R.id.password)).perform(typeText(WRONG_PASSWORD)).perform(closeSoftKeyboard());
        onView(withId(R.id.login)).perform(click());
        onView(withText(R.string.login_failed_username)).inRoot(withDecorView(not(is(mActivityRule.getActivity().getWindow().getDecorView()))))
                .check(matches(isDisplayed()));
    }

    @Test
    public void should_login_failed_when_login_given_username_invalid() {
        when(userRepository.findByName(anyString())).thenReturn(new Maybe<UserModel>() {
            @Override
            protected void subscribeActual(MaybeObserver<? super UserModel> observer) {
                observer.onComplete();
            }
        });
        final String SHORT_USERNAME = "s";
        onView(withId(R.id.username)).perform(typeText(SHORT_USERNAME));
        onView(withId(R.id.username)).check(matches(hasErrorText(applicationContext.getString(R.string.invalid_username))));
    }

    @Test
    public void should_login_failed_when_login_given_password_invalid() {
        when(userRepository.findByName(anyString())).thenReturn(new Maybe<UserModel>() {
            @Override
            protected void subscribeActual(MaybeObserver<? super UserModel> observer) {
                observer.onComplete();
            }
        });
        onView(withId(R.id.username)).perform(typeText(CREATE_USER_NAME)).perform(closeSoftKeyboard());
        final String SHORT_PASSWORD = "123";
        onView(withId(R.id.password)).perform(typeText(SHORT_PASSWORD));
        onView(withId(R.id.password)).check(matches(hasErrorText(applicationContext.getString(R.string.invalid_password))));
    }
}