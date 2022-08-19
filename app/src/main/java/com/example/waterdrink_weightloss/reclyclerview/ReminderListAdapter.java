package com.example.waterdrink_weightloss.reclyclerview;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.waterdrink_weightloss.Model.PrefKey;
import com.example.waterdrink_weightloss.R;
import com.example.waterdrink_weightloss.Model.ReminderTime;
import com.example.waterdrink_weightloss.Recevier.ReminderBroadCast;
import com.example.waterdrink_weightloss.activity.SetReminderActivity;
import com.example.waterdrink_weightloss.activity.WaterIntakeActivity;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import io.paperdb.Paper;

public class ReminderListAdapter extends RecyclerView.Adapter<ReminderListAdapter.ViewHolder>{
    private List<ReminderTime> listdata;
    List<ReminderListData> l;
    ArrayList<PendingIntent> pendingIntentArrayList = new ArrayList<PendingIntent>();
    Activity activity;
    List<ReminderTime> pendingReminderList = new ArrayList<ReminderTime>();
    List<ReminderTime> reminderTime = new ArrayList<ReminderTime>();
    MaterialTimePicker materialTimePicker ;
    Calendar current_time_calender;
    AlertDialog set_ml_dialog;
    int timer_hour;
    int timer_min;
    ImageView tick , close;
    TextView time;
    EditText set_ml_edittext;
    SharedPreferences reminderSharedPreferences;
    AlertDialog.Builder builder;
    View dialogView;

    // RecyclerView recyclerView;
    public ReminderListAdapter(Activity activity , List<ReminderTime> listdata) {
        this.listdata = listdata;
        this.activity = activity;
    }

    public ReminderListAdapter(){
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Paper.init(activity);
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.reminder_list, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);

        reminderSharedPreferences = activity.getSharedPreferences(PrefKey.SharePrefName, Context.MODE_PRIVATE);
        builder = new AlertDialog.Builder(activity);//this -- important not write other
        dialogView = activity.getLayoutInflater().inflate(R.layout.drinkml_reminderset,null);
        //Custom Dialog box add
        builder.setView(dialogView);

        set_ml_dialog = builder.create();
        set_ml_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        tick = dialogView.findViewById(R.id.tick);
        set_ml_edittext = dialogView.findViewById(R.id.targetSetEditText);
        time = dialogView.findViewById(R.id.time);
        close = dialogView.findViewById(R.id.close);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        final ReminderTime reminderListData = listdata.get(position);

        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR,reminderListData.getHour());
        c.set(Calendar.MINUTE,reminderListData.getMin());

        holder.textView.setText(String.format("%02d",c.get(Calendar.HOUR))+":"+
                String.format("%02d",c.get(Calendar.MINUTE)) + " "+ (c.get(c.get(Calendar.AM_PM))==1?"PM":"AM"));

        holder.menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openOptionMenu(view,position);
            }
        });
    }

    @Override
    public int getItemCount() {

//        return listdata.size();
        if(listdata.size()>0)
            return 1;
        else {
            activity.startActivity(new Intent(activity, SetReminderActivity.class));
            return 0;
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView menu;
        public TextView textView , ml_textView;
        public ConstraintLayout linearLayout;
        public CardView cardView;
        public ViewHolder(View itemView) {
            super(itemView);

            textView = (TextView) itemView.findViewById(R.id.time);
            menu = (ImageView) itemView.findViewById(R.id.menu);
            linearLayout = (ConstraintLayout) itemView.findViewById(R.id.linearlayout);
            cardView = (CardView) itemView.findViewById(R.id.cardview) ;
        }
    }

    public void openOptionMenu(View v,final int position){
        Context wrapper = new ContextThemeWrapper(activity, R.style.YOURSTYLE_PopupMenu);
        PopupMenu popup = new PopupMenu(wrapper, v);
        popup.getMenuInflater().inflate(R.menu.option_menu, popup.getMenu());

        /*  The below code in try catch is responsible to display icons*/

            try {
                Field[] fields = popup.getClass().getDeclaredFields();
                for (Field field : fields) {
                    if ("mPopup".equals(field.getName())) {
                        field.setAccessible(true);
                        Object menuPopupHelper = field.get(popup);
                        Class<?> classPopupHelper = Class.forName(menuPopupHelper.getClass().getName());
                        Method setForceIcons = classPopupHelper.getMethod("setForceShowIcon", boolean.class);
                        setForceIcons.invoke(menuPopupHelper, true);
                        break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                if(item.getItemId()==R.id.delete_reminder) {
                    AlarmManager alarmManager = (AlarmManager) activity.getSystemService(Context.ALARM_SERVICE);
                    listdata = Paper.book().read("ReminderTimeList");
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(activity, listdata.get(0).getTemp(),
                            listdata.get(0).getIntent(), PendingIntent.FLAG_UPDATE_CURRENT);
                    alarmManager.cancel(pendingIntent);
                    pendingIntent.cancel();
                    listdata.remove(position);
                    Paper.book().write("ReminderTimeList", listdata);

                    if (listdata.size() != 0) {
                        Calendar current_time = Calendar.getInstance();
                        current_time.set(Calendar.HOUR_OF_DAY, 23);
                        current_time.set(Calendar.MINUTE, 59);
                        current_time.set(Calendar.SECOND, 59);

                        if (current_time.getTimeInMillis() < listdata.get(0).getMilli_time()) {
                            activity.startActivity(new Intent(activity, WaterIntakeActivity.class));
                        } else {
                            notifyDataSetChanged();
                        }
                    } else
                        activity.startActivity(new Intent(activity, WaterIntakeActivity.class));
                }

                if (item.getItemId()==R.id.edit_reminder)
                {
                    current_time_calender = Calendar.getInstance();

                    materialTimePicker = new MaterialTimePicker.Builder()
                            .setTimeFormat(TimeFormat.CLOCK_12H)
                            .setHour(current_time_calender.get(Calendar.HOUR_OF_DAY))
                            .setMinute(current_time_calender.get(Calendar.MINUTE))
                            .setTitleText("SET REMINDER")
                            .setTheme(R.style.AppTheme_MaterialTimePickerTheme)
                            .build();

                    materialTimePicker.show(((AppCompatActivity)activity).getSupportFragmentManager(),"TIMER");

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

                            set_ml_dialog.setCancelable(false);
                            set_ml_dialog.show();

//                timedialog.show();

                }
            });

        tick.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    set_ml_dialog.dismiss();
                    addSelectedTimer();
                    notifyDataSetChanged();
                }
            });

        close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    set_ml_dialog.dismiss();
                }
            });
                }
                return true;
            }
        });
        popup.show();

    }

    private void addSelectedTimer(){
        current_time_calender = Calendar.getInstance();
        Calendar calendar = Calendar.getInstance();

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

        AlarmManager alarmManager1 = (AlarmManager) activity.getSystemService(Context.ALARM_SERVICE);
        PendingIntent pendingIntent1 = PendingIntent.getBroadcast(activity, reminderTime.get(0).getTemp(),
                reminderTime.get(0).getIntent(), PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager1.cancel(pendingIntent1);
        pendingIntent1.cancel();
        reminderTime.remove(0);
        Paper.book().write("ReminderTimeList", reminderTime);

        Intent intent = new Intent(activity, ReminderBroadCast.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(activity,
                temp, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        //alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis() , pendingIntent );
        //for repeting
        AlarmManager alarmManager = (AlarmManager) activity.
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
    }

}
