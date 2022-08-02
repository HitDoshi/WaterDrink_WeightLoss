package com.example.waterdrink_weightloss.fragment;

import android.app.PendingIntent;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.waterdrink_weightloss.Database.DBHandler;
import com.example.waterdrink_weightloss.Database.DataModel;
import com.example.waterdrink_weightloss.R;
import com.example.waterdrink_weightloss.activity.Model.ReminderTime;
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

    ImageView weather , physical;
    TextView weather_cancel , weather_ok ;
    TextView physical_cancel , physical_ok;
    FragmentHomeBinding fb;
    List<ReminderTime> reminderListDataList = new ArrayList<>();
    ArrayList<PendingIntent> pendingIntentArrayList = new ArrayList<PendingIntent>();
    int wakeupHour , wakeupMin ,  badHour , badMin , interval=60 ;

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
        sharedPreferences = getActivity().getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);

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

        boolean theme = sharedPreferences.getBoolean("Theme",false);

        if(theme){
            dialogView1.setBackgroundResource(R.drawable.dark_dialog_shape);
            setWeatherDialogDarkMode();

            dialogView2.setBackgroundResource(R.drawable.dark_dialog_shape);
            setPhysicalDialogDarkMode();
        }else{
            dialogView1.setBackgroundResource(R.drawable.light_dialog_shape);
            dialogView2.setBackgroundResource(R.drawable.light_dialog_shape);
        }

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

        if (sharedPreferences.getBoolean("Tips",false)){
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

        fb.linear4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(target_ml!=0) {
                    total_ml += CupSize;

                    if (glass_add_record!=null) {
                        glass_add_record += "," + String.valueOf(CupSize);
                    }
                    else {
                        glass_add_record = String.valueOf(CupSize);
                    }
                    setCompletedData();
                    dbHandler.updateData(id,day,month,year,total_ml,glass_add_record);

                    Log.d("update",glass_add_record+"");
                }
                else
                    Toast.makeText(getContext(), "Please Set Target", Toast.LENGTH_SHORT).show();
            }
        });

        Log.d("onViewCreated","onViewCreated Home Fragment");
    }

    @Override
    public void onStart() {
        super.onStart();

        target_ml = sharedPreferences.getInt("target_ml", 1500);
        CupSize = sharedPreferences.getInt("CupSize", 300);
        fb.targetTextview.setText(target_ml + " ml");
        fb.addGlass.setText(CupSize +"ml");

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
        if(percentage>50)
        {
            fb.tipText.setText("Hold the water in your mouth for a while before swallowing");
        }
//        if(percentage<100)
            fb.textviewProgress.setText(percentage + " %");
//        else
//            fb.textviewProgress.setText(100 + " %");
        int remain = target_ml - total_ml;
        if (remain>0)
            fb.rememberWaterText.setText(remain + " ml more");
        else
            fb.rememberWaterText.setText("Completed");
    }

    @Override
    public void onResume() {
        super.onResume();

        Log.d("Resume","Resume HomeFragment");
        try {

            wakeupHour = sharedPreferences.getInt("wake_up_hour",7);
            wakeupMin = sharedPreferences.getInt("wake_up_min",0);

            badHour = sharedPreferences.getInt("bed_hour",11);
            badMin = sharedPreferences.getInt("bed_min",0);

            interval = sharedPreferences.getInt("Interval",60);

            setReminder();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    void setSharedPreferencesData()
    {
        sharedPreferences = getActivity().getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);

        sharedPreferences.edit().putInt("target_ml",target_ml).putInt("CupSize", CupSize) .apply();

        fb.targetTextview.setText(target_ml + " ml");
    }

    void setReminder() throws ParseException {

//        int i=wakeupHour , j=wakeupMin , min = badMin , hour = badHour ;
//        reminderListDataList.clear();
///*
//
//        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
//        df.setTimeZone(TimeZone.getTimeZone("UTC"));
//        Date d1 = df.parse("13:30:0"); //date 1
//        Date d2 = df.parse("0:120:0"); // date 2
//        long sum = d1.getTime() + d2.getTime();
//*/
///*
//        String myTime = "12:42";
//        SimpleDateFormat df1 = new SimpleDateFormat("HH:mm");
//        Date d = df1.parse(myTime);
//        Calendar cal = Calendar.getInstance();
//        cal.setTime(d);
//        cal.add(Calendar.MINUTE, 2);
//        String newTime = df1.format(cal.getTime());
//
//        Log.d("Time",newTime);*/
//
//        Calendar calendar = Calendar.getInstance();
//        Calendar currentTime = Calendar.getInstance();
//       /* SimpleDateFormat df = new SimpleDateFormat("HH");*/
//        //calendar.setTimeZone(TimeZone.getTimeZone("GMT+8"));
//        /*calendar.set(Calendar.HOUR_OF_DAY,wakeupHour);
//        calendar.set(Calendar.MINUTE,wakeupMin);
//        calendar.add(Calendar.MINUTE,interval);*/
///*
//
//        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
//
//        Intent intent = new Intent(getActivity(), ExecutableServices.class);
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), 0, intent, 0);
//
//        //alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis() , pendingIntent );
//        //for repeting
//        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis() ,
//                interval*60*1000,pendingIntent );
//*/
//
//      /*  Log.d("aaa", String.valueOf(df.parse(String.valueOf(a)).getHours()));
//        Log.d("Calender",calendar.get(Calendar.HOUR)+" "+calendar.get(Calendar.MINUTE));*/
//
//        int temp=0;
//
//        //Log.d("RealTime",calendar.getTimeInMillis()+"");
//        while (i<badHour)
//        {
//            calendar.set(Calendar.HOUR_OF_DAY,i);
//            calendar.set(Calendar.MINUTE,j);
//            calendar.set(Calendar.SECOND,0);
//            Log.d("Interval",interval+"");
//            calendar.add(Calendar.MINUTE,interval);
//            Log.d("Calender",calendar.get(Calendar.HOUR_OF_DAY)+" "+calendar.get(Calendar.MINUTE));
//
//            i = calendar.get(Calendar.HOUR_OF_DAY);
//            j = calendar.get(Calendar.MINUTE);
//            ReminderListData data = new ReminderListData();
//            String first , second  ;
//            first = String.format("%02d", i);
//            second = String.format("%02d", j);
//            data.setTime(first+":"+second);
//            int x = i;
//            if(System.currentTimeMillis()<calendar.getTimeInMillis()) {
//                reminderListDataList.add(data);
//            }
//
//           /* Intent intent = new Intent(getActivity(), ReminderBroadCast.class);
//            PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), temp , intent, 0);
//            //alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis() , pendingIntent );
//            //for repeting
//            AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
//            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis() , pendingIntent );
//            Log.d("Time",calendar.getTimeInMillis()+"");
//            pendingIntentArrayList.add(pendingIntent);
//            Log.d("Start",reminderListDataList.size()+"");
//            temp++;*/
//        }
        reminderListDataList.clear();
        sharedPreferences = getActivity().getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
        reminderListDataList = Paper.book().read("ReminderTimeList");

        Log.d("R",sharedPreferences.getBoolean("ReminderOnOff",false)+"");
        if(reminderListDataList!=null && reminderListDataList.size()!=0) {
            Log.d("S", reminderListDataList.size() + "");
                setRecyclerView();
        }
        else{
            fb.cardview.setVisibility(View.GONE);
        }
        //Log.d("Cal",cal.getTime()+"");
    }

    public void setRecyclerView()
    {
        ReminderListAdapter adapter = new ReminderListAdapter(getActivity(),reminderListDataList);
        fb.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        fb.recyclerView.setHasFixedSize(true);
        fb.recyclerView.setAdapter(adapter);
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
}