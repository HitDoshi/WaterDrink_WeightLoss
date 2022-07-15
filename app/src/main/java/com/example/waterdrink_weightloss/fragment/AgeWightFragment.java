package com.example.waterdrink_weightloss.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.waterdrink_weightloss.R;
import com.example.waterdrink_weightloss.activity.UserInformation;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AgeWightFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AgeWightFragment extends Fragment {

    NumberPicker age , weight ;
    Button set;
    SharedPreferences userDataSharedPreferences;
    SeekBar seekBar;
    TextView setTarget;
    EditText editTarget;
    int DrinkTarget;
    SharedPreferences targetSharedPref;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AgeWightFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AgeWightFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AgeWightFragment newInstance(String param1, String param2) {
        AgeWightFragment fragment = new AgeWightFragment();
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
        View view =  inflater.inflate(R.layout.fragment_age_wight, container, false);

        age = view.findViewById(R.id.select_age);
        weight = view.findViewById(R.id.select_weight);
        //set = view.findViewById(R.id.set);
        userDataSharedPreferences = getActivity().getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
        targetSharedPref = getActivity().getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);

        String a1;
        String w1 ;
        a1  = userDataSharedPreferences.getString("age","21");
        w1 = userDataSharedPreferences.getString("weight","55");
        age.setMinValue(1);
        age.setMaxValue(100);
        age.setValue(Integer.parseInt(a1));

        weight.setMinValue(1);
        weight.setMaxValue(1000);
        weight.setValue(Integer.parseInt(w1));

        age.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int i) {
                return String.format("%02d", i);
            }
        });

        weight.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int i) {
                return String.format("%02d", i);
            }
        });

        age.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int oldValue, int newValue) {
                userDataSharedPreferences.edit().putString("age", String.valueOf(age.getValue())).apply();
            }
        });

        weight.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int oldValue, int newValue) {
                userDataSharedPreferences.edit().putString("weight", String.valueOf(newValue)).apply();
            }
        });

        seekBar = view.findViewById(R.id.seekbar);
        editTarget = view.findViewById(R.id.targetSetEditText);
        setTarget = view.findViewById(R.id.target);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int target, boolean b) {
                DrinkTarget = target + 500 ;
                setTarget.setText(DrinkTarget +" ml");
                editTargetSharedPref(DrinkTarget);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        editTarget.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (!editTarget.getText().toString().equals("")) {
                    DrinkTarget = Integer.parseInt(editTarget.getText().toString());
                } else {
                    DrinkTarget = 1500;
                }
                setTarget.setText(editTarget.getText() + " ml");
                editTargetSharedPref(DrinkTarget);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

       /* set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userDataSharedPreferences.edit().putString("age", String.valueOf(age.getValue())).apply();
                userDataSharedPreferences.edit().putString("weight", String.valueOf(weight.getValue())).apply();
            }
        });
*/
        return view;
    }

    private void editTargetSharedPref(int target) {
        targetSharedPref.edit().putInt("target_ml",target).apply();
    }

    @Override
    public void onStart() {
        super.onStart();

        int t = targetSharedPref.getInt("target_ml",1500);
        setTarget.setText(t+"ml");
        editTarget.setText(t+"");
        seekBar.setProgress(t-500);

    }
}