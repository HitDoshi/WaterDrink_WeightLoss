package com.example.waterdrink_weightloss.fragment;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.waterdrink_weightloss.Database.DBHandler;
import com.example.waterdrink_weightloss.Database.DataModel;
import com.example.waterdrink_weightloss.R;
import com.example.waterdrink_weightloss.Model.PrefKey;
import com.example.waterdrink_weightloss.Model.ReminderTime;
import com.example.waterdrink_weightloss.databinding.FragmentHomeBinding;
import com.example.waterdrink_weightloss.reclyclerview.ReminderListAdapter;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import io.paperdb.Paper;

public class HomeFragment extends Fragment{

    SharedPreferences sharedPreferences;
    int CupSize = 300 , target_ml = 1500  ,total_ml = 0;
    int id = 0;
    int day,month,year, percentage;
    String glass_add_record=null;
    String[] glass_add_record_array1 ;
    ArrayList<ArrayList<Integer>> timeList = new ArrayList<ArrayList<Integer>>();
    ArrayList<String> glass_add_record_array2 = new ArrayList<>();

    Boolean isThere = false;
    ArrayList<DataModel> arrayList = new ArrayList<>();
    DBHandler dbHandler;

    /*ProgressBar progressBar;
    TextView textView;*/
    //weather dialog
    AlertDialog weather_alertDialog;
    AlertDialog.Builder builder1;
    View dialogView1;
    TextView g1,g2,g3,g4;
    TextView w,w1,w2,w3,w4;
    ImageView f1,f2,f3,f4;
    LinearLayout normal , warm , hot , cold ;

    //physical activity dialog
    AlertDialog physical_alertDialog;
    AlertDialog.Builder builder2;
    View dialogView2;
    TextView t1,t2,t3,t4,t;
    ImageView i1,i2,i3,i4;
    TextView p1,p2,p3,p4;
    LinearLayout sedentary , lightly , moderate , very ;

    //rate dialog
    AlertDialog rate_us_dialog ;
    AlertDialog.Builder rate_dialog_builder;
    View dialogView;
    TextView later , rate_app , text2;
    RatingBar ratingBar;

    ImageView weather , physical;
    TextView weather_cancel , weather_ok ;
    TextView physical_cancel , physical_ok;
    FragmentHomeBinding fb;
    List<ReminderTime> reminderListDataList = new ArrayList<>();
    ArrayList<PendingIntent> pendingIntentArrayList = new ArrayList<PendingIntent>();
    int wakeupHour , wakeupMin ,  badHour , badMin , interval=60 ;
    boolean rate = false;
    boolean reminder_layout=true , tips_layout=false ;

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
        Paper.init(getContext());
        sharedPreferences = getActivity().getSharedPreferences(PrefKey.SharePrefName, Context.MODE_PRIVATE);

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

        //rate dialog
        rate_dialog_builder = new AlertDialog.Builder(getContext());
        dialogView = getLayoutInflater().inflate(R.layout.rate_app_dialog,null);
        //Custom Dialog box add
        rate_dialog_builder.setView(dialogView);
        rate_us_dialog = rate_dialog_builder.create();
        rate_us_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dbHandler = new DBHandler(getContext());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        fb = FragmentHomeBinding.inflate(getLayoutInflater());

        //View view = inflater.inflate(R.layout.responsiveui, container, false);

        /*progressBar = view.findViewById(R.id.progress_bar);
        textView = view.findViewById(R.id.textview_progress);
        weather = view.findViewById(R.id.weather);
        physical = view.findViewById(R.id.physical_activity);
        weather_cancel = dialogView1.findViewById(R.id.cancel);
        weather_ok = dialogView1.findViewById(R.id.ok);
        physical_cancel = dialogView2.findViewById(R.id.cancel);
        physical_ok = dialogView2.findViewById(R.id.ok);*/

        //weather dialog
        weather_cancel = dialogView1.findViewById(R.id.cancel);
        weather_ok = dialogView1.findViewById(R.id.ok);
        normal = dialogView1.findViewById(R.id.normal_weather);
        warm = dialogView1.findViewById(R.id.warm_weather);
        hot = dialogView1.findViewById(R.id.hot_weather);
        cold = dialogView1.findViewById(R.id.cold_weather);
        w = dialogView1.findViewById(R.id.w);
        w1 = dialogView1.findViewById(R.id.w1);
        w2 = dialogView1.findViewById(R.id.w2);
        w3 = dialogView1.findViewById(R.id.w3);
        w4 = dialogView1.findViewById(R.id.w4);
        g1 = dialogView1.findViewById(R.id.t1);
        g2 = dialogView1.findViewById(R.id.t2);
        g3 = dialogView1.findViewById(R.id.t3);
        g4 = dialogView1.findViewById(R.id.t4);
        f1 = dialogView1.findViewById(R.id.i1);
        f2 = dialogView1.findViewById(R.id.i2);
        f3 = dialogView1.findViewById(R.id.i3);
        f4 = dialogView1.findViewById(R.id.i4);

        //physical activity dialog
        physical_cancel = dialogView2.findViewById(R.id.cancel);
        physical_ok = dialogView2.findViewById(R.id.ok);
        sedentary = dialogView2.findViewById(R.id.sedentary);
        lightly = dialogView2.findViewById(R.id.lightly_activity);
        moderate = dialogView2.findViewById(R.id.moderate_active);
        very = dialogView2.findViewById(R.id.very_activity);
        t = dialogView2.findViewById(R.id.t);
        t1  = dialogView2.findViewById(R.id.t1);
        t2  = dialogView2.findViewById(R.id.t2);
        t3  = dialogView2.findViewById(R.id.t3);
        t4  = dialogView2.findViewById(R.id.t4);
        p1  = dialogView2.findViewById(R.id.p1);
        p2  = dialogView2.findViewById(R.id.p2);
        p3  = dialogView2.findViewById(R.id.p3);
        p4  = dialogView2.findViewById(R.id.p4);
        i1  = dialogView2.findViewById(R.id.i1);
        i2  = dialogView2.findViewById(R.id.i2);
        i3  = dialogView2.findViewById(R.id.i3);
        i4  = dialogView2.findViewById(R.id.i4);

        //rate_app Dialog
        later = dialogView.findViewById(R.id.later);
        rate_app = dialogView.findViewById(R.id.rate);
        ratingBar = dialogView.findViewById(R.id.ratingBar);
        text2 = dialogView.findViewById(R.id.t2);

        boolean theme = sharedPreferences.getBoolean(PrefKey.Theme,true);
        rate = sharedPreferences.getBoolean(PrefKey.RateApp,false);

        if(theme){
            dialogView1.setBackgroundResource(R.drawable.dark_dialog_shape);
            setWeatherDialogDarkMode();

            dialogView2.setBackgroundResource(R.drawable.dark_dialog_shape);
            setPhysicalDialogDarkMode();

            dialogView.setBackgroundResource(R.drawable.dark_dialog_shape);
            setRateDialogDarkMode();

        }else{
            dialogView1.setBackgroundResource(R.drawable.light_dialog_shape);
            dialogView2.setBackgroundResource(R.drawable.light_dialog_shape);
            dialogView.setBackgroundResource(R.drawable.light_dialog_shape);

            fb.totalDrink.setTextColor(Integer.parseInt(String.valueOf(R.color.dark_blue)));
            fb.rememberWaterText.setTextColor(Integer.parseInt(String.valueOf(R.color.dark_blue)));
        }

        if(rate){
            rate_us_dialog.setCancelable(false);
            rate_us_dialog.show();

            Log.d("Rate Dialog","Display");
        }

        later.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharedPreferences.edit().putBoolean(PrefKey.RateApp,false).commit();
                rate = false;
                rate_us_dialog.dismiss();
            }
        });

        rate_app.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharedPreferences.edit().putBoolean(PrefKey.RateApp,false).apply();
                rate = false;
                rate_us_dialog.dismiss();

                if(ratingBar.getRating()>3) {
                    Intent intent = new Intent(android.content.Intent.ACTION_VIEW);

                    //Copy App URL from Google Play Store.
                    intent.setData(Uri.parse("http://play.google.com/store/apps/details?id=" +
                            getActivity().getPackageName()));

                    startActivity(intent);
                }

            }
        });

        Log.d("HomeFragment","HomeFragment Call");
        return fb.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        /*fb.progressBar.setProgress(90);
        fb.targetTextview.setText("90");
        */

        tips_layout = sharedPreferences.getBoolean(PrefKey.Tips,false) ;
        if (tips_layout){
            fb.linear1.setVisibility(View.GONE);
        }

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

        fb.addGlassLinearlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDrinkWaterData(CupSize);
            }
        });

//        fb.plus.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                setDrinkWaterData(1);
//            }
//        });
//
//        fb.minus.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                if(total_ml>0)
//                    setDrinkWaterData(-1);
//            }
//        });

        fb.deleteLast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(glass_add_record!=null && !glass_add_record.equals("0"))
                {
                    glass_add_record_array1 = glass_add_record.split(",");

                    glass_add_record = null;

                    int size = glass_add_record_array1.length;

                    total_ml -= Integer.parseInt(glass_add_record_array1[size-1]);

                    for (int i=0;i<size-1;i++)
                    {
                        if (i==0)
                            glass_add_record = glass_add_record_array1[i];
                        else
                            glass_add_record = glass_add_record + "," + glass_add_record_array1[i];
                    }

                    dbHandler.updateData(id,day,month,year,total_ml,glass_add_record);

                    setCompletedData();
                }

            }
        });

        Log.d("onViewCreated","onViewCreated Home Fragment");
    }

    private void setDrinkWaterData(int drink) {
        if(target_ml!=0) {

            total_ml += drink;

            if (glass_add_record!=null) {
                glass_add_record += "," + String.valueOf(CupSize);
            }
            else {
                glass_add_record = String.valueOf(CupSize);
            }

            setCompletedData();
            dbHandler.updateData(id,day,month,year,total_ml,glass_add_record);

            Log.d("update",glass_add_record+"");
            Log.d("Glass",total_ml+"");
        }
        else
            Toast.makeText(getContext(), "SET Water Drink Goal", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStart() {
        super.onStart();

        target_ml = sharedPreferences.getInt(PrefKey.Target_ml, 1500);
        CupSize = sharedPreferences.getInt(PrefKey.CupSize, 300);
        fb.targetTextview.setText(target_ml + " ml");
        fb.addGlass.setText(CupSize +"ml");

        arrayList.clear();
        arrayList = dbHandler.readData();

        final Calendar calendar = Calendar.getInstance();

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
                    setCompletedData();
                    Log.d("false", total_ml + "");
                }
            }
        }
    }

    private void setCompletedData() {

        fb.targetTextview.setText("Drink "+target_ml+" ml");
        percentage = ((int) (  ( (float) (total_ml)) / (float) (target_ml) * 100));

        fb.progressBar.setProgress(percentage);

        if(percentage==0)
        {
            fb.tipText.setText(R.string.tip1);
        }

        if(percentage>0)
        {
            fb.tipText.setText(R.string.tip2);
        }

        if(percentage>40){
            fb.tipText.setText(R.string.tip3);
        }

        if(percentage>=80){
            fb.tipText.setText(R.string.tip4);
        }

        if(percentage>=100)
        {
            fb.tipText.setText(R.string.tip5);
        }

        if(percentage<100)
            fb.textviewProgress.setText(percentage + " %");
        else
            fb.textviewProgress.setText(100 + " %");
        int remain = target_ml - total_ml;
        if (remain>0)
            fb.rememberWaterText.setText(remain + " ml Remain");
        else
            fb.rememberWaterText.setText("Completed");

        fb.totalDrink.setText(total_ml +" ml Drink");
    }

    @Override
    public void onResume() {
        super.onResume();

        Log.d("Resume","Resume HomeFragment");
        try {

            wakeupHour = sharedPreferences.getInt(PrefKey.Wake_up_Hour,7);
            wakeupMin = sharedPreferences.getInt(PrefKey.Wake_up_Min,0);

            badHour = sharedPreferences.getInt(PrefKey.Bed_Hour,11);
            badMin = sharedPreferences.getInt(PrefKey.Bed_Min,0);

            interval = sharedPreferences.getInt(PrefKey.Interval,60);

            setReminder();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    void setSharedPreferencesData()
    {
        sharedPreferences = getActivity().getSharedPreferences(PrefKey.SharePrefName, Context.MODE_PRIVATE);

        sharedPreferences.edit().putInt("target_ml",target_ml).putInt(PrefKey.CupSize, CupSize) .apply();

        fb.targetTextview.setText(target_ml + " ml");
    }

    void setReminder() throws ParseException {

        reminderListDataList = new ArrayList<>();
        sharedPreferences = getActivity().getSharedPreferences(PrefKey.SharePrefName, Context.MODE_PRIVATE);
        reminderListDataList = Paper.book().read("ReminderTimeList");

        Log.d("R",sharedPreferences.getBoolean(PrefKey.ReminderOnOff,false)+"");
        if(reminderListDataList!=null && reminderListDataList.size()!=0) {
            Log.d("S", reminderListDataList.size() + "");
                setRecyclerView();
        }
        else{
            fb.cardview.setVisibility(View.GONE);
            reminder_layout = false;
        }

//        if(tips_layout && !reminder_layout){
//            // Gets linearlayout
//
//// Gets the layout params that will allow you to resize the layout
//            ViewGroup.LayoutParams params = fb.linear4.getLayoutParams();
//// Changes the height and width to the specified *pixels*
//            params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
//            fb.linear4.setLayoutParams(params);
//
//            Log.d("Layout","Set");
//        }
//        else {
//            // Gets linearlayout
//
//// Gets the layout params that will allow you to resize the layout
//            ViewGroup.LayoutParams params = fb.linear4.getLayoutParams();
//// Changes the height and width to the specified *pixels*
//            params.height = 0;
//            fb.linear4.setLayoutParams(params);
//
//        }
        //Log.d("Cal",cal.getTime()+"");
    }

    public void setRecyclerView()
    {
        ReminderListAdapter adapter = new ReminderListAdapter(getActivity(),reminderListDataList);
        fb.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        fb.recyclerView.setHasFixedSize(true);
        fb.recyclerView.setAdapter(adapter);

        Calendar current_time = Calendar.getInstance();
        current_time.set(Calendar.HOUR_OF_DAY,23);
        current_time.set(Calendar.MINUTE,59);
        current_time.set(Calendar.SECOND,59);

        if(current_time.getTimeInMillis()<reminderListDataList.get(0).getMilli_time())
        {
            fb.reminderText.setText("Tomorrow's Reminder");
        }

        //Log.d("Tag",reminderListDataList.size()+"");
    }

    private void setPhysicalDialogDarkMode(){
        t.setTextColor(Color.WHITE);
        t1.setTextColor(Color.WHITE);
        t2.setTextColor(Color.WHITE);
        t3.setTextColor(Color.WHITE);
        t4.setTextColor(Color.WHITE);
        p1.setTextColor(Color.WHITE);
        p2.setTextColor(Color.WHITE);
        p3.setTextColor(Color.WHITE);
        p4.setTextColor(Color.WHITE);
        i1.setColorFilter(Color.WHITE);
        i2.setColorFilter(Color.WHITE);
        i3.setColorFilter(Color.WHITE);
        i4.setColorFilter(Color.WHITE);
    }

    private void setWeatherDialogDarkMode(){
        w.setTextColor(Color.WHITE);
        w1.setTextColor(Color.WHITE);
        w2.setTextColor(Color.WHITE);
        w3.setTextColor(Color.WHITE);
        w4.setTextColor(Color.WHITE);
        g1.setTextColor(Color.WHITE);
        g2.setTextColor(Color.WHITE);
        g3.setTextColor(Color.WHITE);
        g4.setTextColor(Color.WHITE);
        f1.setColorFilter(Color.WHITE);
        f2.setColorFilter(Color.WHITE);
        f3.setColorFilter(Color.WHITE);
        f4.setColorFilter(Color.WHITE);
    }

    private void setRateDialogDarkMode(){
        later.setTextColor(Color.WHITE);
        text2.setTextColor(Color.WHITE);
    }
}