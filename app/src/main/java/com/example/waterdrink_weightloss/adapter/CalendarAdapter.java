package com.example.waterdrink_weightloss.adapter;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.PluralsRes;
import androidx.recyclerview.widget.RecyclerView;

import com.example.waterdrink_weightloss.Database.DataModel;
import com.example.waterdrink_weightloss.R;
import com.example.waterdrink_weightloss.activity.Model.PrefKey;
import com.example.waterdrink_weightloss.activity.PrefManager;
import com.example.waterdrink_weightloss.reclyclerview.ReminderListAdapter;
import com.example.waterdrink_weightloss.reclyclerview.ReminderListData;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.ViewHolder> {

    ArrayList<Integer> arrayList = new ArrayList<>();
    Activity activity;
    ArrayList<String> name;
    int temp = 0;
    int target_ml = 1500 ;
    SharedPreferences sharedPreferences;
    Calendar calendar;
    Integer year , size;
    // RecyclerView recyclerView;
    public CalendarAdapter(Activity activity , ArrayList<Integer> arrayList, ArrayList<String> name,Integer year) {
        this.arrayList = arrayList;
        this.activity = activity;
        this.name = name;
        this.year = year;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.list_date, parent, false);
        CalendarAdapter.ViewHolder viewHolder = new CalendarAdapter.ViewHolder(listItem);

        sharedPreferences = activity.getSharedPreferences(PrefKey.SharePrefName,Context.MODE_PRIVATE);

        target_ml = sharedPreferences.getInt(PrefKey.Target_ml, 1500);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

            /*calendar.set(Calendar.DAY_OF_WEEK,i+1);

            for (int j=0;j<arrayList.size();j++) {
                if (calendar.get(Calendar.DAY_OF_MONTH)==arrayList.get(j).getDay()){
                    *//*total += arrayList.get(j).getAchievement();
                    sum = arrayList.get(j).getAchievement();*//*
                        temp = ((int) (  ( (float) (int)(arrayList.get(j).getAchievement())) /
                                (float) (1500) * 100));
                    //Toast.makeText(activity, "Hi", Toast.LENGTH_SHORT).show();
                    }
            }*/

        if(getItemCount()==12) {
            calendar = new GregorianCalendar(year, position, 1);
            size = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
            temp = ((int) (((float) (int) (arrayList.get(position)) / size ) /
                    (float) (target_ml) * 100));
        }
        else {
            temp = ((int) (((float) (int) (arrayList.get(position))) /
                    (float) (target_ml) * 100));
        }
            holder.progressBar.setProgress(temp);

            if (temp<99)
                holder.textview_progress.setText(temp+"%");
            else
                holder.textview_progress.setText(100+"%");
            temp=0;

            if(name!=null) {
                holder.name.setText(name.get(position));
            }
            else {
                holder.name.setText(position + 1 + "");
            }

        //holder.textview_progress.setText(arrayList.get(position).getDay());

        /*holder.imageView.setImageResource(listdata[position].getImgId());*/
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView textview_progress , name;
        ProgressBar progressBar;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textview_progress = itemView.findViewById(R.id.textview_progress);
            name = itemView.findViewById(R.id.name);
            progressBar = itemView.findViewById(R.id.progressbar);
        }
    }
}
