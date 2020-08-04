package com.thoughtworks.todo_list.task;

import android.icu.text.SimpleDateFormat;
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
import java.util.Locale;
import java.util.UUID;

import io.reactivex.internal.operators.single.SingleCreate;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.openContextualActionModeOverflowMenu;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
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
    private List<TaskModel> taskModels;

    @Rule
    public ActivityTestRule<TaskActivity> mActivityRule = new ActivityTestRule<>(TaskActivity.class, false, false);

    @Before
    public void before() {

        applicationContext = (MainApplication) InstrumentationRegistry.getInstrumentation().getTargetContext().getApplicationContext();
        taskRepository = applicationContext.getTaskRepository();
        taskModel = new TaskModel(CREATE_ID, CREATE_TITLE, CREATE_DESCRIPTION, DATE,
                CREATE_DATE, IS_CHECKED, IS_REMIND, CREATE_USER_ID);
        taskModels = new ArrayList<>();
        taskModels.add(taskModel);
        when(taskRepository.findTasks(anyInt())).thenReturn(new SingleCreate<>(emitter -> emitter.onSuccess(taskModels)));
        mActivityRule.launchActivity(null);
    }

    @Test
    public void should_show_week_and_day() {
        Date date = new Date();
        String week = DateFormat.format("EEEE", date).toString();
        String month = DateFormat.format("MMM", date).toString();
        String day = DateFormat.format("dd", date).toString();
        String weekDay = String.format("%s, %sth", week, day);
        String taskNumber = taskModels.size() + "个任务";

        onView(withId(R.id.week_and_day)).check(matches(withText(weekDay)));
        onView(withId(R.id.month)).check(matches(withText(month)));
        onView(withId(R.id.task_number)).check(matches(withText(taskNumber)));
    }

    @Test
    public void should_return_task_info_where_click_task_item() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY年MM月dd日", Locale.CHINA);
        onView(withId(R.id.my_recycle_view))
                .perform(actionOnItemAtPosition(0, click()));
        onView(withId(R.id.create_title)).check(matches(withText(CREATE_TITLE)));
        onView(withId(R.id.create_description)).check(matches(withText(CREATE_DESCRIPTION)));
        onView(withId(R.id.create_date_info)).check(matches(withText(simpleDateFormat.format(DATE))));
    }

    @Test
    public void should_logout_successfully_when_click_logout_menu() {
        String logout_menu = "登出";
        openContextualActionModeOverflowMenu();
        onView(withText(logout_menu)).perform(click());
        onView(withText(R.string.success_logout))
                .inRoot(withDecorView(not(is(mActivityRule.getActivity().getWindow().getDecorView()))))
                .check(matches(isDisplayed()));
    }


}