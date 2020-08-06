package com.thoughtworks.todo_list.task;

import androidx.test.espresso.contrib.PickerActions;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.thoughtworks.todo_list.MainApplication;
import com.thoughtworks.todo_list.R;
import com.thoughtworks.todo_list.ui.task.CreateTaskActivity;
import com.thoughtworks.todo_list.ui.task.TaskRepository;
import com.thoughtworks.todo_list.ui.task.model.TaskModel;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Calendar;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isEnabled;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.not;

@RunWith(AndroidJUnit4.class)
public class CreateActivityTest {
    private TaskRepository taskRepository;
    private TaskModel taskModel;
    private MainApplication applicationContext;
    @Rule
    public ActivityTestRule<CreateTaskActivity> mActivityRule = new ActivityTestRule<>(CreateTaskActivity.class, false, false);

    @Before
    public void before() {

        applicationContext = (MainApplication) InstrumentationRegistry.getInstrumentation().getTargetContext().getApplicationContext();
        taskRepository = applicationContext.getTaskRepository();
        mActivityRule.launchActivity(null);
    }

    @Test
    public void should_visible_toolbar_and_date_and_checkbox_and_switch() {
        onView(withId(R.id.create_task_toolbar)).check(matches(isDisplayed()));
        onView(withId(R.id.create_date_info)).check(matches(withParent(withId(R.id.create_title_layout))));
        onView(withId(R.id.create_check_box)).check(matches(withParent(withId(R.id.create_title_layout))));
        onView(withId(R.id.create_switch)).check(matches(withParent(withId(R.id.create_title_layout))));
    }

    @Test
    public void should_create_failed_when_correct_title(){
        String title = "title";
        String description = "description";
        onView(withId(R.id.create_title)).perform(typeText(title)).perform(closeSoftKeyboard());
        onView(withId(R.id.create_description)).perform(typeText(description)).perform(closeSoftKeyboard());
        onView(withId(R.id.create_task_button)).check(matches(not(isEnabled())));
    }

    @Test
    public void should_create_successfully_when_correct_title_and_correct_date(){
        String title = "title";
        String description = "description";
        onView(withId(R.id.create_title)).perform(typeText(title)).perform(closeSoftKeyboard());
        onView(withId(R.id.create_description)).perform(typeText(description)).perform(closeSoftKeyboard());
        onView(withId(R.id.create_date_info)).perform(click());
        onView(withId(R.id.date_picker_view)).perform(PickerActions.setDate(Calendar.YEAR, Calendar.MONTH, Calendar.DAY_OF_MONTH));
        onView(withId(R.id.create_task_button)).check(matches(isEnabled()));
    }
}
