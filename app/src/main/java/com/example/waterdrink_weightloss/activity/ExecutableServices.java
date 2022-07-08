package com.example.waterdrink_weightloss.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.util.Log;
import android.widget.Toast;

import com.example.waterdrink_weightloss.reclyclerview.ReminderListAdapter;

public class ExecutableServices extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        Vibrator vibrator = (Vibrator) context
                .getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(1);

        //Toast.makeText(context, "Vibrate", Toast.LENGTH_SHORT).show();
        Log.d("abcvibrate","Vibrate");

      /*  ReminderListAdapter reminderListAdapter = new ReminderListAdapter();
        reminderListAdapter.deleteFirstReminder();*/
    }
}
