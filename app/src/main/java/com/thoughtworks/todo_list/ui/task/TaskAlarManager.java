package com.thoughtworks.todo_list.ui.task;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.thoughtworks.todo_list.ui.task.model.TaskModel;

import java.util.Calendar;

public class TaskAlarManager {
    public static final String TASK_NOTIFICATION = "TaskNotification";
    public static final String TASK_MODEL_KEY = "TASK_MODEL_KEY";

    public static void taskAlarManager(Context context, TaskModel taskModel) {
        AlarmManager alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, TaskReceiver.class);
        Bundle bundle = new Bundle();

        bundle.putSerializable(TASK_MODEL_KEY, taskModel);
        intent.putExtras(bundle);
        intent.setAction(TASK_NOTIFICATION);
        context.sendBroadcast(intent);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
        Calendar cal = Calendar.getInstance();
        cal.setTime(taskModel.getDate());
        alarm.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), sender);
    }
}
