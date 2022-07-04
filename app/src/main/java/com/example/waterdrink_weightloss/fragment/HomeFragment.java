package com.example.waterdrink_weightloss.fragment;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.waterdrink_weightloss.R;

public class HomeFragment extends Fragment {

    ProgressBar progressBar;
    TextView textView;
    AlertDialog.Builder builder1;
    AlertDialog.Builder builder2;
    AlertDialog weather_alertDialog;
    AlertDialog physical_alertDialog;
    View dialogView1;
    View dialogView2;
    ImageView weather , physical;
    TextView weather_cancel , weather_ok ;
    TextView physical_cancel , physical_ok;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
    }

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("create","create");
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        //weather dialog
        builder1 = new AlertDialog.Builder(getContext());
        dialogView1 = getLayoutInflater().inflate(R.layout.weather_dialog,null);
        //Custom Dialog box add
        builder1.setView(dialogView1);

        weather_alertDialog = builder1.create();
        weather_alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        //Physical Activity Dialog
        builder2 = new AlertDialog.Builder(getContext());
        dialogView2 = getLayoutInflater().inflate(R.layout.physical_activity_dialog,null);
        //Custom Dialog box add
        builder2.setView(dialogView2);

        physical_alertDialog = builder2.create();
        physical_alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.responsiveui, container, false);

        progressBar = view.findViewById(R.id.progress_bar);
        textView = view.findViewById(R.id.textview_progress);
        weather = view.findViewById(R.id.weather);
        physical = view.findViewById(R.id.physical_activity);
        weather_cancel = dialogView1.findViewById(R.id.cancel);
        weather_ok = dialogView1.findViewById(R.id.ok);
        physical_cancel = dialogView2.findViewById(R.id.cancel);
        physical_ok = dialogView2.findViewById(R.id.ok);

        progressBar.setProgress(90);
        textView.setText(String.valueOf(10));

        weather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                weather_alertDialog.show();
            }
        });

        physical.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                physical_alertDialog.show();
            }
        });

        weather_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                weather_alertDialog.dismiss();
            }
        });

        weather_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                weather_alertDialog.dismiss();
            }
        });

        physical_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                physical_alertDialog.dismiss();
            }
        });

        physical_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                physical_alertDialog.dismiss();
            }
        });
        Log.d("HomeFragment","HomeFragment Call");
        return view;
    }
}