package com.example.waterdrink_weightloss.reclyclerview;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.waterdrink_weightloss.R;

import java.util.List;

public class ReminderListAdapter extends RecyclerView.Adapter<ReminderListAdapter.ViewHolder>{
    private List<ReminderListData> listdata;

    // RecyclerView recyclerView;
    public ReminderListAdapter(List<ReminderListData> listdata) {
        this.listdata = listdata;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.reminder_list, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final ReminderListData reminderListData = listdata.get(position);

        holder.textView.setText(reminderListData.getTime());

        /*holder.imageView.setImageResource(listdata[position].getImgId());*/
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(view.getContext(),"click on item: "+reminderListData.getTime(),Toast.LENGTH_LONG).show();
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
                listdata.remove(position);
                notifyDataSetChanged();
                return true;
            }
        });
        popup.show();
    }
}
