package com.example.waterdrink_weightloss.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.PluralsRes;
import androidx.recyclerview.widget.RecyclerView;

import com.example.waterdrink_weightloss.R;
import com.example.waterdrink_weightloss.activity.Model.PrefKey;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class ProgressBarAdapter extends RecyclerView.Adapter<ProgressBarAdapter.ViewHolder> {

    ArrayList<Integer> arrayList = new ArrayList<>();
    Activity activity;
    int temp = 0;
    int target_ml = 1500 ;
    SharedPreferences sharedPreferences;
    // RecyclerView recyclerView;
    public ProgressBarAdapter(Activity activity , int temp) {
        this.activity = activity;
        this.temp = temp;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.set_progressbar, parent, false);
        ProgressBarAdapter.ViewHolder viewHolder = new ProgressBarAdapter.ViewHolder(listItem);

        sharedPreferences = activity.getSharedPreferences(PrefKey.SharePrefName, Context.MODE_PRIVATE);

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

        holder.progressBar.setProgress(temp);

        if (temp<99)
            holder.textview_progress.setText(temp+"%");
        else
            holder.textview_progress.setText(100+"%");
        temp=0;
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView textview_progress ;
        ProgressBar progressBar;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textview_progress = itemView.findViewById(R.id.textview_progress);
            progressBar = itemView.findViewById(R.id.progressbar);
        }
    }
}
