package com.thoughtworks.todo_list.ui.task;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.thoughtworks.todo_list.R;

public class NotificationActivity extends AppCompatActivity {
    private Button tv_send_notification;
    public static final String TASK_NOTIFICATION = "TaskNotification";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        tv_send_notification = findViewById(R.id.tv_send_notification);
        tv_send_notification.setOnClickListener(view -> {

//            taskAlarManager();
        });

    }

    private void taskAlarManager(Context context) {
        AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent myIntent = new Intent(context, TaskReceiver.class);

        myIntent.setAction(TASK_NOTIFICATION);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, myIntent, 0);
        alarm.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 5 * 1000, sender);
    }
}