package com.example.waterdrink_weightloss.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.waterdrink_weightloss.R;
import com.example.waterdrink_weightloss.Model.PrefKey;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GenderFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GenderFragment extends Fragment {

    Button set;
    SharedPreferences userDataSharedPreferences;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public GenderFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GenderFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GenderFragment newInstance(String param1, String param2) {
        GenderFragment fragment = new GenderFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d("Male","Male");
        View view= inflater.inflate(R.layout.fragment_gender, container, false);
        LinearLayout male = view.findViewById(R.id.male);
        LinearLayout female = view.findViewById(R.id.female);
        //set = view.findViewById(R.id.set);
        userDataSharedPreferences = getActivity().getSharedPreferences(PrefKey.SharePrefName, Context.MODE_PRIVATE);
        String g = userDataSharedPreferences.getString(PrefKey.Gender,"Male");

        if (!g.equals("-")) {
            if (g.equals("Male")){
                male.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.background_shape));
                female.setBackgroundColor(0);
            }
            else {
                female.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.background_shape));
                male.setBackgroundColor(0);
            }
        }

        male.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                male.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.background_shape));
//                male.setBackgroundColor(Integer.parseInt(String.valueOf(R.color.water_color)));
                female.setBackgroundColor(0);
                userDataSharedPreferences.edit().putString(PrefKey.Gender,"Male").apply();
            }
        });

        female.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                female.setBackgroundColor(Integer.parseInt(String.valueOf(R.color.water_color)));
                female.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.background_shape));
                male.setBackgroundColor(0);
                userDataSharedPreferences.edit().putString(PrefKey.Gender,"Female").apply();
            }
        });
/*
        set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });*/

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}