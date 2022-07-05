package com.example.waterdrink_weightloss.fragment;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.waterdrink_weightloss.Database.DBHandler;
import com.example.waterdrink_weightloss.Database.DataModel;
import com.example.waterdrink_weightloss.R;
import com.example.waterdrink_weightloss.databinding.FragmentHomeBinding;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.Calendar;

public class HomeFragment extends Fragment {

    SharedPreferences sharedPreferences;
    int glass_size = 300 , target_ml = 500  ,total_ml = 0;
    int id = 0;
    int day,month,year, percentage;
    String glass_add_record=null;
    String[] glass_add_record_array1 ;
    ArrayList<String> glass_add_record_array2 = new ArrayList<>();

    Boolean isThere = false;
    ArrayList<DataModel> arrayList = new ArrayList<>();
    DBHandler dbHandler;

    /*ProgressBar progressBar;
    TextView textView;*/
    AlertDialog.Builder builder1;
    AlertDialog.Builder builder2;
    AlertDialog weather_alertDialog;
    AlertDialog physical_alertDialog;
    View dialogView1;
    View dialogView2;
    ImageView weather , physical;
    TextView weather_cancel , weather_ok ;
    TextView physical_cancel , physical_ok;
    FragmentHomeBinding fb;

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

//        fb = DataBindingUtil.setContentView(getActivity(),R.layout.responsiveui);
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
        dbHandler = new DBHandler(getContext());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        fb = FragmentHomeBinding.inflate(getLayoutInflater());



        View view = inflater.inflate(R.layout.responsiveui, container, false);

        /*progressBar = view.findViewById(R.id.progress_bar);
        textView = view.findViewById(R.id.textview_progress);
        weather = view.findViewById(R.id.weather);
        physical = view.findViewById(R.id.physical_activity);
        weather_cancel = dialogView1.findViewById(R.id.cancel);
        weather_ok = dialogView1.findViewById(R.id.ok);
        physical_cancel = dialogView2.findViewById(R.id.cancel);
        physical_ok = dialogView2.findViewById(R.id.ok);*/
        weather_cancel = dialogView1.findViewById(R.id.cancel);
        weather_ok = dialogView1.findViewById(R.id.ok);
        physical_cancel = dialogView2.findViewById(R.id.cancel);
        physical_ok = dialogView2.findViewById(R.id.ok);

    /*    binding.progressBar.setProgress(90);
        binding.targetTextview.setText(String.valueOf(90));
*/
        /*weather.setOnClickListener(new View.OnClickListener() {
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
        });*/
        Log.d("HomeFragment","HomeFragment Call");
        return fb.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        /*fb.progressBar.setProgress(90);
        fb.targetTextview.setText("90");
        */
        fb.weather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                weather_alertDialog.show();
            }
        });
        fb.physicalActivity.setOnClickListener(new View.OnClickListener() {
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

        fb.addGlass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(target_ml!=0) {
                    total_ml += glass_size ;

                    if (glass_add_record!=null) {
                        glass_add_record += "," + String.valueOf(glass_size);
                    }
                    else {
                        glass_add_record = String.valueOf(glass_size);
                    }
                    setCompletedData();
                    dbHandler.updateData(id,day,month,year,total_ml,glass_add_record);

                    Log.d("update",glass_add_record+"");
                }
                else
                    Toast.makeText(getContext(), "Please Set Target", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();

        arrayList.clear();
        arrayList = dbHandler.readData();

        final Calendar calendar = Calendar.getInstance();
        //Toast.makeText(this, arrayList.size()  + "", Toast.LENGTH_SHORT).show();

        day = calendar.get(Calendar.DAY_OF_MONTH);
        month = calendar.get(Calendar.MONTH) + 1;
        year = calendar.get(Calendar.YEAR);

        Log.d("Start",day+" "+month+" "+year);

        for (int i = 0; i < arrayList.size(); i++) {
            if (day == arrayList.get(i).getDay() && month == arrayList.get(i).getMonth()
                    && year == arrayList.get(i).getYear()) {
                id = arrayList.get(i).getId();
                total_ml = arrayList.get(i).getAchievement();
                glass_add_record = arrayList.get(i).getGlass_add_record();
                setCompletedData();
                isThere = true;
            }
        }

        if(isThere)
        {
            isThere=false;
            Log.d("true",total_ml+"");
        }
        else {
            dbHandler.addNewDayRecord(day, month, year, 0, "0");

            arrayList.clear();
            arrayList = dbHandler.readData();

            for (int i = 0; i < arrayList.size(); i++) {
                if (day == arrayList.get(i).getDay() && month == arrayList.get(i).getMonth()
                        && year == arrayList.get(i).getYear()) {
                    id = arrayList.get(i).getId();
                    total_ml = arrayList.get(i).getAchievement();
                    glass_add_record = arrayList.get(i).getGlass_add_record();
                    Log.d("false", total_ml + "");
                }
            }
        }
    }

    private void setCompletedData() {
        fb.progressBar.setProgress(total_ml);
        fb.targetTextview.setText("Drink "+target_ml+" ml");
        percentage = ((int) (  ( (float) (total_ml)) / (float) (target_ml) * 100));
        fb.textviewProgress.setText(percentage + " %");
        int remain = target_ml - total_ml;
        if (remain>0)
            fb.rememberWaterText.setText(remain + " ml more");
        else
            fb.rememberWaterText.setText("Completed Today Target");
    }
}