package com.example.waterdrink_weightloss.activity.Recevier;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.waterdrink_weightloss.R;
import com.example.waterdrink_weightloss.activity.WaterIntakeActivity;
import com.example.waterdrink_weightloss.reclyclerview.ReminderListData;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;

public class ReminderBroadCast extends BroadcastReceiver {
    @Override

    public void onReceive(Context context, Intent intent) {

        List<ReminderListData> reminderListDataList = new ArrayList<>();
        Paper.init(context);

        Vibrator vibrator = (Vibrator) context
                .getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(1000);

        //Toast.makeText(context, "Vibrate", Toast.LENGTH_SHORT).show();
        Log.d("abcvibrate","Vibrate");

        NotificationCompat.Builder builder = new NotificationCompat.Builder(
                context,"Drink Water").setContentTitle("Water Drink Reminder")
                .setSmallIcon(R.drawable.frontpage)
                .setContentText("Let's Drink Some Water And Healthy Your Body")
                .setDefaults(Notification.DEFAULT_SOUND)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        Intent intent1 = new Intent(context, WaterIntakeActivity.class);
        intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,0,
                intent1,PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(200,builder.build());

        reminderListDataList = Paper.book().read("ReminderTimeList");
        reminderListDataList.remove(0);
        Paper.book().write("ReminderTimeList",reminderListDataList);

      /*  ReminderListAdapter reminderListAdapter = new ReminderListAdapter();
        reminderListAdapter.deleteFirstReminder();*/
    }
}
