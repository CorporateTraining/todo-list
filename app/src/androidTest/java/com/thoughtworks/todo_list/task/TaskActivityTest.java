package com.thoughtworks.todo_list.task;

import android.text.format.DateFormat;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.thoughtworks.todo_list.MainApplication;
import com.thoughtworks.todo_list.R;
import com.thoughtworks.todo_list.ui.task.TaskActivity;
import com.thoughtworks.todo_list.ui.task.TaskRepository;
import com.thoughtworks.todo_list.ui.task.model.TaskModel;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.internal.operators.single.SingleCreate;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;


@RunWith(AndroidJUnit4.class)
public class TaskActivityTest {
    private TaskRepository taskRepository;
    private TaskModel taskModel;
    private MainApplication applicationContext;
    private final String CREATE_ID = UUID.randomUUID().toString().replace("-", "").toLowerCase();
    private final String CREATE_TITLE = "title";
    private final String CREATE_DESCRIPTION = "description";
    private final Date DATE = new Date();
    private final Date CREATE_DATE = new Date();
    private final boolean IS_CHECKED = false;
    private final boolean IS_REMIND = true;
    private final int CREATE_USER_ID = 1;

    @Rule
    public ActivityTestRule<TaskActivity> mActivityRule = new ActivityTestRule<>(TaskActivity.class, false, false);

    @Before
    public void before() {

        applicationContext = (MainApplication) InstrumentationRegistry.getInstrumentation().getTargetContext().getApplicationContext();
        taskRepository = applicationContext.getTaskRepository();
        taskModel = new TaskModel(CREATE_ID, CREATE_TITLE, CREATE_DESCRIPTION, DATE,
                CREATE_DATE, IS_CHECKED, IS_REMIND, CREATE_USER_ID);
        when(taskRepository.findTasks(anyInt())).thenReturn(new SingleCreate<>(emitter->emitter.onSuccess(new ArrayList<>())));
        mActivityRule.launchActivity(null);
    }

    @Test
    public void should_show_week_and_day() {
        Date date = new Date();
        String WEEK = DateFormat.format("EEEE", date).toString();
        String MONTH = DateFormat.format("MMM", date).toString();
        String DAY = DateFormat.format("dd", date).toString();
        String WEEK_DAY = String.format("%s, %sth", WEEK, DAY);

        onView(withId(R.id.week_and_day)).check(matches(withText(WEEK_DAY)));
        onView(withId(R.id.month)).check(matches(withText(MONTH)));
    }
}