package com.example.waterdrink_weightloss.reclyclerview;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.waterdrink_weightloss.R;
import com.example.waterdrink_weightloss.activity.Model.ReminderTime;
import com.example.waterdrink_weightloss.activity.ReminderActivity;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;

public class ReminderListAdapter extends RecyclerView.Adapter<ReminderListAdapter.ViewHolder>{
    private List<ReminderTime> listdata;
    List<ReminderListData> l;
    ArrayList<PendingIntent> pendingIntentArrayList = new ArrayList<PendingIntent>();
    Activity activity;
    List<ReminderTime> pendingReminderList = new ArrayList<ReminderTime>();
    List<ReminderTime> reminderTime = new ArrayList<ReminderTime>();

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

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        final ReminderTime reminderListData = listdata.get(position);

        holder.textView.setText(String.format("%02d",reminderListData.getHour())+":"+
                String.format("%02d",reminderListData.getMin()) );
        ReminderActivity reminderActivity = new ReminderActivity();
//        Log.d("Size...",reminderActivity.pendingIntentArrayList.size()+"");

        /*holder.imageView.setImageResource(listdata[position].getImgId());*/
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(view.getContext(),"click on item: "+reminderListData.getTime(),Toast.LENGTH_LONG).show();
               /* pendingReminderList = Paper.book().read("PendingReminderList");
                 Log.d("PendingReminderList",pendingReminderList.size()+"");
                reminderTime = Paper.book().read("PendingReminderList");
                Log.d("PendingReminderList",reminderTime.size()+"")*/;
            }
        });

        holder.menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openOptionMenu(view,position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listdata.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView menu;
        public TextView textView;
        public ConstraintLayout linearLayout;
        public ViewHolder(View itemView) {
            super(itemView);

            textView = (TextView) itemView.findViewById(R.id.time);
            menu = (ImageView) itemView.findViewById(R.id.menu);
            linearLayout = (ConstraintLayout) itemView.findViewById(R.id.linearlayout);
        }
    }

    public void openOptionMenu(View v,final int position){
        PopupMenu popup = new PopupMenu(v.getContext(), v);
        popup.getMenuInflater().inflate(R.menu.option_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                AlarmManager alarmManager = (AlarmManager) activity.getSystemService(Context.ALARM_SERVICE);
                listdata = Paper.book().read("ReminderTimeList");
                PendingIntent pendingIntent = PendingIntent.getBroadcast(activity,position,
                        listdata.get(position).getPendingIntent(),PendingIntent.FLAG_UPDATE_CURRENT);
                alarmManager.cancel(pendingIntent);
                pendingIntent.cancel();
                listdata.remove(position);
                Paper.book().write("ReminderTimeList",listdata);
                notifyDataSetChanged();
                return true;
            }
        });
        popup.show();
    }

    /*public void deleteFirstReminder()
    {
        if(listdata.size()>0) {
            listdata.remove(0);
            notifyDataSetChanged();
        }
    }*/
}
