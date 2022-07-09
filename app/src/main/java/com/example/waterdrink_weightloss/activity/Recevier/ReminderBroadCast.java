package com.example.waterdrink_weightloss.activity.Recevier;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationChannelCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.waterdrink_weightloss.R;
import com.example.waterdrink_weightloss.reclyclerview.ReminderListAdapter;

public class ReminderBroadCast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        Vibrator vibrator = (Vibrator) context
                .getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(1);

        //Toast.makeText(context, "Vibrate", Toast.LENGTH_SHORT).show();
        Log.d("abcvibrate","Vibrate");

        NotificationCompat.Builder builder = new NotificationCompat.Builder(
                context,"Drink Water").setContentTitle("Water Drink Reminder")
                .setSmallIcon(R.drawable.img)
                .setContentText("Let's Drink Some Water And Healthy Your Body")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(200,builder.build());

      /*  ReminderListAdapter reminderListAdapter = new ReminderListAdapter();
        reminderListAdapter.deleteFirstReminder();*/
    }
}
