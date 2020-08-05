package com.thoughtworks.todo_list.ui.task;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;

import com.google.gson.Gson;
import com.thoughtworks.todo_list.repository.utils.GsonUtil;
import com.thoughtworks.todo_list.ui.task.model.TaskModel;

import java.util.Calendar;
import java.util.Date;

public class TaskAlarManager {
    public static final String TASK_NOTIFICATION = "TaskNotification";
    public static final String TASK_MODEL_KEY = "TASK_MODEL_KEY";

    public static void taskAlarManager(Context context, TaskModel taskModel) {
        AlarmManager alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, TaskReceiver.class);
        String taskModelJson = GsonUtil.jsonToString(taskModel);
        intent.putExtra(TASK_MODEL_KEY, taskModelJson);
        intent.setAction(TASK_NOTIFICATION);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
        alarm.setExact(AlarmManager.RTC_WAKEUP, taskModel.getDate().getTime(), sender);
    }
}
