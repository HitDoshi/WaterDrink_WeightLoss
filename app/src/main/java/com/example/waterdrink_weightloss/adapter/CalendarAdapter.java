package com.example.waterdrink_weightloss.adapter;

import android.app.Activity;
import android.app.PendingIntent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.waterdrink_weightloss.Database.DataModel;
import com.example.waterdrink_weightloss.R;
import com.example.waterdrink_weightloss.reclyclerview.ReminderListAdapter;
import com.example.waterdrink_weightloss.reclyclerview.ReminderListData;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.ViewHolder> {

    ArrayList<DataModel> arrayList = new ArrayList<>();
    Activity activity;
    Calendar calendar = Calendar.getInstance();
    // RecyclerView recyclerView;
    public CalendarAdapter(Activity activity , ArrayList<DataModel> arrayList, Calendar calendar) {
        calendar = Calendar.getInstance();
        this.arrayList = arrayList;
        this.activity = activity;
        this.calendar = calendar;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.list_date, parent, false);
        CalendarAdapter.ViewHolder viewHolder = new CalendarAdapter.ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        int temp = 0;
        for(int i=0;i<7;i++)
        {
            calendar.set(Calendar.DAY_OF_WEEK,i+1);

            for (int j=0;j<arrayList.size();j++) {
                if (calendar.get(Calendar.DAY_OF_MONTH)==arrayList.get(j).getDay()){
                    /*total += arrayList.get(j).getAchievement();
                    sum = arrayList.get(j).getAchievement();*/
                        temp = ((int) (  ( (float) (int)(arrayList.get(j).getAchievement())) /
                                (float) (1500) * 100));
                    Toast.makeText(activity, "Hi", Toast.LENGTH_SHORT).show();
                    }
            }

            holder.progressBar.setProgress(temp);
            if (temp<99)
                holder.textview_progress.setText(temp+"%");
            else
                holder.textview_progress.setText(100+"%");
            temp=0;
            holder.name.setText(i+1+"");
        }

        //holder.textview_progress.setText(arrayList.get(position).getDay());

        /*holder.imageView.setImageResource(listdata[position].getImgId());*/
    }

    @Override
    public int getItemCount() {
        return 7;
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
