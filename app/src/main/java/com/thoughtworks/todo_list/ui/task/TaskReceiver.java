package com.thoughtworks.todo_list.ui.task;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.core.app.NotificationCompat;

import com.thoughtworks.todo_list.R;
import com.thoughtworks.todo_list.repository.utils.GsonUtil;
import com.thoughtworks.todo_list.ui.task.model.TaskModel;

import java.util.Date;

import static com.thoughtworks.todo_list.ui.task.TaskAlarManager.TASK_MODEL_KEY;
import static com.thoughtworks.todo_list.ui.task.TaskAlarManager.TASK_NOTIFICATION;

public class TaskReceiver extends BroadcastReceiver {
    private static final String TODO_LIST = "TodoList";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (TASK_NOTIFICATION.equals(intent.getAction())) {
            String taskModelJson = intent.getStringExtra(TASK_MODEL_KEY);
            TaskModel taskModel = GsonUtil.stringFromJson(taskModelJson, TaskModel.class);
            if (taskModel != null) {
                showNotification(context, taskModel);
            }
        }
    }

    private void showNotification(Context context, TaskModel taskModel) {
        String id = taskModel.getId();
        String name = taskModel.getTitle();
        Intent intent = new Intent(context, TaskActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel notificationChannel = notificationManager.getNotificationChannel(id);
        if (notificationChannel == null) {
            int importance = NotificationManager.IMPORTANCE_LOW;
            notificationChannel = new NotificationChannel(id, name, importance);
            notificationManager.createNotificationChannel(notificationChannel);
        }


        Notification notification = new NotificationCompat
                .Builder(context, id)
                .setContentTitle(taskModel.getTitle())
                .setSmallIcon(R.drawable.ic_baseline_notifications_active_24)
                .setContentText(taskModel.getDescription())
                .setDefaults(Notification.DEFAULT_ALL)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setTicker(TODO_LIST)
                .setWhen(System.currentTimeMillis())
                .build();

        int intValue = Long.valueOf(taskModel.getCreateDate().getTime()).intValue();
        notificationManager.notify(intValue, notification);
    }
}
