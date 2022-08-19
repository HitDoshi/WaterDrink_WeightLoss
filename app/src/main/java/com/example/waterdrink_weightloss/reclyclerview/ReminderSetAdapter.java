package com.example.waterdrink_weightloss.reclyclerview;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.waterdrink_weightloss.R;
import com.example.waterdrink_weightloss.Model.PrefKey;
import com.example.waterdrink_weightloss.Model.ReminderTime;
import com.example.waterdrink_weightloss.activity.WaterIntakeActivity;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.List;

import io.paperdb.Paper;

public class ReminderSetAdapter extends RecyclerView.Adapter<ReminderSetAdapter.ViewHolder>{
    private List<ReminderTime> listdata;
    List<ReminderListData> l;
    Activity activity;
    SharedPreferences sharedPreferences;

    // RecyclerView recyclerView;
    public ReminderSetAdapter(Activity activity , List<ReminderTime> listdata) {
        this.listdata = listdata;
        this.activity = activity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Paper.init(activity);
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.reminder_list2, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);

        return viewHolder;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        final ReminderTime reminderListData = listdata.get(position);

        int drink_ml = reminderListData.getMl();

        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR,reminderListData.getHour());
        c.set(Calendar.MINUTE,reminderListData.getMin());

        holder.time_textView.setText(String.format("%02d",c.get(Calendar.HOUR))+":"+
                String.format("%02d",c.get(Calendar.MINUTE)) + " "+ (c.get((c.get(Calendar.AM_PM)))==1?"PM":"AM"));
        sharedPreferences = activity.getSharedPreferences(PrefKey.SharePrefName,Context.MODE_PRIVATE);

        holder.ml_textView.setText(drink_ml+ " ml");

        if(drink_ml==300)
        {
            holder.glass_img.setImageResource(R.drawable.ml2);
        }

        if(drink_ml>300)
        {
            holder.glass_img.setImageResource(R.drawable.ml3);
        }
        
        boolean theme = sharedPreferences.getBoolean(PrefKey.Theme,true);
        if(!theme)
        {
            holder.linear.setBackgroundColor(activity.getResources().getColor(R.color.light_blue));
        }

        holder.menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openOptionMenu(view,position);
            }
        });
    }

    @Override
    public int getItemCount() {

        //return listdata.size();
        if(listdata.size()>0)

        return listdata.size();

        else {
            activity.startActivity(new Intent(activity, WaterIntakeActivity.class));
            return 0;
        }

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView menu,glass_img;
        public TextView time_textView,ml_textView ;
        public ConstraintLayout linearLayout;
        public CardView cardView;
        public LinearLayout linear;

        public ViewHolder(View itemView) {
            super(itemView);

            time_textView = (TextView) itemView.findViewById(R.id.time);
            ml_textView = (TextView) itemView.findViewById(R.id.ml);
            menu = (ImageView) itemView.findViewById(R.id.menu);
            glass_img = (ImageView) itemView.findViewById(R.id.glass);
            linearLayout = (ConstraintLayout) itemView.findViewById(R.id.linearlayout);
            cardView = (CardView) itemView.findViewById(R.id.cardview);
            linear = (LinearLayout) itemView.findViewById(R.id.linear);

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
                AlarmManager alarmManager = (AlarmManager) activity.getSystemService(Context.ALARM_SERVICE);
                listdata = Paper.book().read("ReminderTimeList");
                PendingIntent pendingIntent = PendingIntent.getBroadcast(activity,listdata.get(position).getTemp(),
                        listdata.get(position).getIntent(),PendingIntent.FLAG_UPDATE_CURRENT);
                alarmManager.cancel(pendingIntent);
                pendingIntent.cancel();
                listdata.remove(position);
                Paper.book().write("ReminderTimeList",listdata);

                if(listdata.size()!=0)
                    notifyDataSetChanged();
                else
                    activity.startActivity(new Intent(activity, WaterIntakeActivity.class));
                return true;
            }
        });
        popup.show();
    }

}
