package com.example.waterdrink_weightloss.Recevier;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Vibrator;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import com.example.waterdrink_weightloss.Model.ReminderTime;
import com.example.waterdrink_weightloss.R;
import com.example.waterdrink_weightloss.Model.PrefKey;
import com.example.waterdrink_weightloss.activity.WaterIntakeActivity;
import com.example.waterdrink_weightloss.reclyclerview.ReminderListData;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import io.paperdb.Paper;

public class ReminderBroadCast extends BroadcastReceiver {
    @Override

    public void onReceive(Context context, Intent intent) {

        List<ReminderTime> reminderListDataList = new ArrayList<>();
        Paper.init(context);
        SharedPreferences userDataSharedPreferences;

        Calendar calendar = Calendar.getInstance();
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        userDataSharedPreferences = context.getSharedPreferences(PrefKey.SharePrefName, Context.MODE_PRIVATE);

        Vibrator vibrator = (Vibrator) context
                .getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(1000);

        //Toast.makeText(context, "Vibrate", Toast.LENGTH_SHORT).show();
        Log.d("Notification WakeUp","Vibrate");

        NotificationCompat.Builder builder = new NotificationCompat.Builder(
                context,"Drink Water").setContentTitle("Water Drink Reminder")
                .setSmallIcon(R.drawable.frontpage)
                .setColor(ContextCompat.getColor(context,R.color.water_color))
                .setContentText("Let's Drink Some Water And Healthy Your Body")
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        if(userDataSharedPreferences.getBoolean(PrefKey.ReminderOnOff,true)) {
            builder.setSound(alarmSound);
        }

        Intent intent1 = new Intent(context, WaterIntakeActivity.class);
        intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,0,
                intent1,PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(new Random().nextInt(),builder.build());

        reminderListDataList = Paper.book().read("ReminderTimeList");

        if(reminderListDataList!=null && reminderListDataList.size()!=0)
        {

            calendar.set(Calendar.HOUR_OF_DAY,reminderListDataList.get(0).getHour());
            calendar.set(Calendar.MINUTE,reminderListDataList.get(0).getMin());
            calendar.set(Calendar.SECOND,0);

            Intent i = new Intent(context, ReminderBroadCast.class);
            PendingIntent p = PendingIntent.getBroadcast(context,
                    reminderListDataList.get(0).getTemp(), i, PendingIntent.FLAG_UPDATE_CURRENT);
            //alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis() , pendingIntent );
            //for repeting
            AlarmManager alarmManager = (AlarmManager) context.
                    getSystemService(Context.ALARM_SERVICE);
            alarmManager.set(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis()+86400000, p);
            //Log.d("Time", calendar.getTimeInMillis() + "");

            reminderListDataList.add(new ReminderTime(
                    reminderListDataList.get(0).getHour(),reminderListDataList.get(0).getMin(),i,
                    reminderListDataList.get(0).getTemp(),calendar.getTimeInMillis()+86400000
            ));

            reminderListDataList.remove(0);
            Paper.book().write("ReminderTimeList",reminderListDataList);
        }

//        context.startActivity(new Intent(context,WaterIntakeActivity.class));

      /*  ReminderListAdapter reminderListAdapter = new ReminderListAdapter();
        reminderListAdapter.deleteFirstReminder();*/
    }
}
