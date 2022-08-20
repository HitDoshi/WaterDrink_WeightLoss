package com.example.waterdrink_weightloss.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.util.Pair;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.waterdrink_weightloss.R;
import com.example.waterdrink_weightloss.Model.PrefKey;
import com.example.waterdrink_weightloss.Model.ReminderTime;
import com.example.waterdrink_weightloss.Recevier.ReminderBroadCast;
import com.example.waterdrink_weightloss.fragment.HomeFragment;
import com.example.waterdrink_weightloss.reclyclerview.ReminderListData;
import com.example.waterdrink_weightloss.reclyclerview.ReminderSetAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import io.paperdb.Paper;

public class SetReminderActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    FloatingActionButton floatingActionButton;
    SharedPreferences reminderSharedPreferences;

    List<ReminderListData> reminderListDataList = new ArrayList<>();
    public ArrayList<PendingIntent> pendingIntentArrayList = new ArrayList<PendingIntent>();
    List<ReminderTime> reminderTime = new ArrayList<ReminderTime>();
    Drawable upArrow;

    //dialog
    AlertDialog set_ml_dialog;
    AlertDialog.Builder builder;
    View dialogView;
    TextView SET,CANCEL,t1,t2 ;
    TextView time;
    EditText set_ml_edittext;
    Calendar current_time_calender,calendar;

    TimePickerDialog timedialog;
    MaterialTimePicker materialTimePicker ;
    int timer_hour , timer_min ;
    boolean theme;

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getSupportActionBar().setTitle("Reminder");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        upArrow = this.getDrawable(R.drawable.arrow_back);
        setContentView(R.layout.activity_set_reminder);
        recyclerView = findViewById(R.id.setReminderRecylerView);
        floatingActionButton = findViewById(R.id.fab_AddReminder);
        Paper.init(this);

        reminderSharedPreferences = getSharedPreferences(PrefKey.SharePrefName, Context.MODE_PRIVATE);
        builder = new AlertDialog.Builder(this);//this -- important not write other
        dialogView = getLayoutInflater().inflate(R.layout.drinkml_reminderset,null);
        //Custom Dialog box add
        builder.setView(dialogView);

        set_ml_dialog = builder.create();
        set_ml_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        //dialog
//        tick = dialogView.findViewById(R.id.tick);
        set_ml_edittext = dialogView.findViewById(R.id.targetSetEditText);
        time = dialogView.findViewById(R.id.time);
        SET = dialogView.findViewById(R.id.set);
        CANCEL = dialogView.findViewById(R.id.cancel);
        t1 = dialogView.findViewById(R.id.t1);
        t2 = dialogView.findViewById(R.id.t2);
        current_time_calender = Calendar.getInstance();
        calendar = Calendar.getInstance();

        if(reminderSharedPreferences.getBoolean(PrefKey.Theme,true)){
            darkStatusBar();
        }else{
            lightStatusBar();
        }

        reminderTime = Paper.book().read("ReminderTimeList");

        if(reminderTime!=null && reminderTime.size()!=0) {
            setRecylerView();
        }
        else{
            reminderTime = new ArrayList<ReminderTime>();
        }

        theme = reminderSharedPreferences.getBoolean(PrefKey.Theme,true);

        if(theme){
            dialogView.setBackgroundResource(R.drawable.dark_dialog_shape);
            set_ml_edittext.setTextColor(Color.WHITE);
            t1.setTextColor(Color.WHITE);
            t2.setTextColor(Color.WHITE);
            time.setTextColor(Color.WHITE);
            //setWeatherDialogDarkMode();
        }else{
            dialogView.setBackgroundResource(R.drawable.light_dialog_shape);
        }

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //setData();
                //set_ReminderDialog.show();
//                TimePickerDialog.OnTimeSetListener mTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
//                    @Override
//                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
//
//                        Toast.makeText(SetReminderActivity.this, hourOfDay+" "+minute
//                                , Toast.LENGTH_SHORT).show();
//                        timer_hour = hourOfDay ;
//                        timer_min = minute ;
//                        addSelectedTimer();
//                    }
//                };
//                timedialog = new TimePickerDialog(SetReminderActivity.this
//                        ,mTimeSetListener,current_time_calender.get(Calendar.HOUR_OF_DAY),
//                        current_time_calender.get(Calendar.MINUTE),false);

                current_time_calender = Calendar.getInstance();

                materialTimePicker = new MaterialTimePicker.Builder()
                        .setTimeFormat(TimeFormat.CLOCK_12H)
                        .setHour(current_time_calender.get(Calendar.HOUR_OF_DAY))
                        .setMinute(current_time_calender.get(Calendar.MINUTE))
                        .setTitleText("SET REMINDER")
                        .setTheme(R.style.AppTheme_MaterialTimePickerTheme)
                        .build();

                materialTimePicker.show(getSupportFragmentManager(),"TIMER");

                materialTimePicker.addOnPositiveButtonClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        timer_hour = materialTimePicker.getHour() ;
                        timer_min = materialTimePicker.getMinute() ;
//                        Toast.makeText(SetReminderActivity.this, timer_hour+" "+timer_min
//                                , Toast.LENGTH_SHORT).show();

                        //addSelectedTimer();
                        Calendar c = Calendar.getInstance();
                        c.set(Calendar.HOUR,timer_hour);
                        c.set(Calendar.MINUTE,timer_min);

                        time.setText(String.format("%02d",c.get(Calendar.HOUR))+":"+
                                String.format("%02d",c.get(Calendar.MINUTE)) + " "+ (c.get((c.get(Calendar.AM_PM)))==1?"PM":"AM"));

                        set_ml_dialog.setCancelable(false);
                        set_ml_dialog.show();
                    }
                });

//                timedialog.show();

            }
        });

        SET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                set_ml_dialog.dismiss();
                addSelectedTimer();

            }
        });

        CANCEL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                set_ml_dialog.dismiss();
            }
        });

        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new
                ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
                    public boolean onMove(RecyclerView recyclerView,
                                          RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                        //final int Position = viewHolder.getAdapterPosition();
//                   final int toPos = viewHolder.getAdapterPosition();
//                    // move item in `fromPos` to `toPos` in adapter.

                        return true;// true if moved, false otherwise
                    }

                    @Override
                    public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                        //Remove swiped item from list and notify the RecyclerView
                        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

                        reminderTime = new ArrayList<>();
                        reminderTime = Paper.book().read("ReminderTimeList");
                        PendingIntent pendingIntent = PendingIntent.
                                getBroadcast(getApplicationContext()
                                        ,reminderTime.get(viewHolder.getAdapterPosition()).getTemp(),
                                reminderTime.get(viewHolder.getAdapterPosition()).
                                        getIntent(),PendingIntent.FLAG_UPDATE_CURRENT);
                        alarmManager.cancel(pendingIntent);
                        pendingIntent.cancel();
                        Log.d("DELETE",reminderTime.get(viewHolder.getAdapterPosition()).getMin()+"");
                        reminderTime.remove(viewHolder.getAdapterPosition());
                        Paper.book().write("ReminderTimeList",reminderTime);
                        setRecylerView();
                    }
                };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

    }

    private void setRecylerView() {
        ReminderSetAdapter adapter = new ReminderSetAdapter(this,reminderTime);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
    }

    public void darkStatusBar(){
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        getWindow().getDecorView().setSystemUiVisibility(View.VISIBLE);
        upArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
    }

    public void lightStatusBar(){
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        Spannable text = new SpannableString(getSupportActionBar().getTitle());
        text.setSpan(new ForegroundColorSpan(Color.BLACK), 0, text.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        getSupportActionBar().setTitle(text);
        upArrow.setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            startActivity(new Intent(getApplicationContext(), WaterIntakeActivity.class));
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void setData(){

        current_time_calender = Calendar.getInstance();

    }

    private void addSelectedTimer(){
        current_time_calender = Calendar.getInstance();
        calendar = Calendar.getInstance();

        int temp=0;
        long time = 0;
        int edittext_ml = Integer.parseInt(set_ml_edittext.getText().toString());
        reminderTime.clear();
        pendingIntentArrayList.clear();
        reminderTime = Paper.book().read("ReminderTimeList");
        if(reminderTime!=null && reminderTime.size()!=0) {
            temp = reminderSharedPreferences.getInt(PrefKey.Reminder_Count,0);
            temp = temp + 1;
            Log.d("Temp",temp+"");
        }
        else{
            reminderTime = new ArrayList<ReminderTime>();
            reminderSharedPreferences.edit().putInt(PrefKey.Reminder_Count,temp).apply();
        }

//        calendar.set(Calendar.HOUR_OF_DAY,hour.getValue());
//        calendar.set(Calendar.MINUTE,min.getValue());
//        calendar.set(Calendar.SECOND,0);

        calendar.set(Calendar.HOUR_OF_DAY,timer_hour);
        calendar.set(Calendar.MINUTE,timer_min);
        calendar.set(Calendar.SECOND,0);

        if(calendar.getTimeInMillis()>current_time_calender.getTimeInMillis())
        {
            Log.d("Time is Passed","No");
            time = 0;
            Log.d("Date",calendar.get(Calendar.DATE)+"");
        }
        else
        {
            Log.d("Time is Passed","Yes");
            time = 86400000;
            Log.d("Date",calendar.get(Calendar.DATE)+"");
        }

        Intent intent = new Intent(getApplicationContext(), ReminderBroadCast.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),
                temp, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        //alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis() , pendingIntent );
        //for repeting
        AlarmManager alarmManager = (AlarmManager) getApplicationContext().
                getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis()+time, pendingIntent);
        //Log.d("Time", calendar.getTimeInMillis() + "");
        pendingIntentArrayList.add(pendingIntent);
//
//        reminderTime.add(new ReminderTime(hour.getValue(),min.getValue(),intent,temp,
//                calendar.getTimeInMillis()+time));


        reminderTime.add(new ReminderTime(timer_hour,timer_min,intent,temp,
                calendar.getTimeInMillis()+time,edittext_ml));

        Collections.sort(reminderTime, new Comparator<ReminderTime>() {
            @Override
            public int compare(ReminderTime reminderTime, ReminderTime t) {
                return Long.compare(reminderTime.getMilli_time(), t.getMilli_time());
            }
        });

//                Collections.sort(reminderTime, new Comparator<ReminderTime>() {
//                    @Override
//                    public int compare(ReminderTime reminderTime, ReminderTime t) {
//                        return Integer.compare(reminderTime.getHour(), t.getHour());
//                    }
//                });

        Log.d("Total set Reminder",reminderTime.size()+" temp(RequestCode)= "+temp);
        for (int i=0;i<reminderTime.size();i++){
            Log.d("ReminderTime_Sort",reminderTime.get(i).getHour()+" : "+
                    reminderTime.get(i).getMin());
        }

        Paper.book().write("ReminderTimeList", reminderTime);
        reminderSharedPreferences.edit().putInt(PrefKey.Reminder_Count,temp).apply();
        setRecylerView();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(),WaterIntakeActivity.class));
        finish();
    }

}