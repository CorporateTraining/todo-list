package com.thoughtworks.todo_list;

import android.content.Context;
import android.os.SystemClock;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.thoughtworks.todo_list.ui.login.UserRepository;
import com.thoughtworks.todo_list.repository.user.entity.User;
import com.thoughtworks.todo_list.repository.utils.Encryptor;
import com.thoughtworks.todo_list.ui.login.LoginActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.reactivex.Maybe;
import io.reactivex.MaybeObserver;
import io.reactivex.internal.operators.maybe.MaybeCreate;

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
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.mockito.Mockito.when;


@RunWith(AndroidJUnit4.class)
public class LoginActivityTest {
    private UserRepository userRepository;
    private User user;
    private MainApplication applicationContext;

    @Rule
    public ActivityTestRule<LoginActivity> mActivityRule = new ActivityTestRule<>(LoginActivity.class);

    @Before
    public void before() {
        applicationContext = (MainApplication) InstrumentationRegistry.getInstrumentation().getTargetContext().getApplicationContext();
        userRepository = applicationContext.userRepository();
        user = new User(1, "xiaoming", "123456");
    }

    @Test
    public void should_login_successfully_when_login_given_correct_username_and_password() {
        when(userRepository.findByName("xiaoming")).thenReturn(new MaybeCreate(emitter -> emitter.onSuccess(user)));

        onView(withId(R.id.username)).perform(typeText("xiaoming")).perform(closeSoftKeyboard());
        onView(withId(R.id.password)).perform(typeText("123456")).perform(closeSoftKeyboard());
        onView(withId(R.id.login)).perform(click());
        onView(withText(R.string.login_welcome)).inRoot(withDecorView(not(is(mActivityRule.getActivity().getWindow().getDecorView()))))
                .check(matches(isDisplayed()));
    }

    @Test
    public void should_login_failed_when_login_given_invalid_password() {
        when(userRepository.findByName("xiaoming")).thenReturn(new MaybeCreate(emitter -> emitter.onSuccess(user)));

        onView(withId(R.id.username)).perform(typeText("xiaoming")).perform(closeSoftKeyboard());
        onView(withId(R.id.password)).perform(typeText("1234567")).perform(closeSoftKeyboard());
        onView(withId(R.id.login)).perform(click());
        onView(withText(R.string.login_failed_password)).inRoot(withDecorView(not(is(mActivityRule.getActivity().getWindow().getDecorView()))))
                .check(matches(isDisplayed()));
    }

    @Test
    public void should_login_failed_when_login_given_username_does_not_exist() {
        when(userRepository.findByName("notexist")).thenReturn(new Maybe<User>() {
            @Override
            protected void subscribeActual(MaybeObserver<? super User> observer) {
                observer.onComplete();
            }
        });
        onView(withId(R.id.username)).perform(typeText("notexist")).perform(closeSoftKeyboard());
        onView(withId(R.id.password)).perform(typeText("1234567")).perform(closeSoftKeyboard());
        onView(withId(R.id.login)).perform(click());
        onView(withText(R.string.login_failed_username)).inRoot(withDecorView(not(is(mActivityRule.getActivity().getWindow().getDecorView()))))
                .check(matches(isDisplayed()));
    }

    @Test
    public void should_login_failed_when_login_given_username_invalid() {
        when(userRepository.findByName("notexist")).thenReturn(new Maybe<User>() {
            @Override
            protected void subscribeActual(MaybeObserver<? super User> observer) {
                observer.onComplete();
            }
        });
        onView(withId(R.id.username)).perform(typeText("s"));
        onView(withId(R.id.username)).check(matches(hasErrorText(applicationContext.getString(R.string.invalid_username))));
    }

    @Test
    public void should_login_failed_when_login_given_password_invalid() {
        when(userRepository.findByName("notexist")).thenReturn(new Maybe<User>() {
            @Override
            protected void subscribeActual(MaybeObserver<? super User> observer) {
                observer.onComplete();
            }
        });
        onView(withId(R.id.username)).perform(typeText("notexist")).perform(closeSoftKeyboard());
        onView(withId(R.id.password)).perform(typeText("123"));
        onView(withId(R.id.password)).check(matches(hasErrorText(applicationContext.getString(R.string.invalid_password))));
    }
}