package com.example.mickey.healthy;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class BMIFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_bmi, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Button backButton = getView().findViewById(R.id.bmi_back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("bmi", "go to menu");
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.main_view, new MenuFragment())
                        .commit();
            }
        });
        Button calculateButton = getView().findViewById(R.id.bmi_calculate_button);
        calculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText weight = getView().findViewById(R.id.bmi_weight);
                EditText height = getView().findViewById(R.id.bmi_height);
                String weightStr = weight.getText().toString();
                String heightStr = height.getText().toString();
                if (weightStr.isEmpty() || heightStr.isEmpty())
                {
                    Toast.makeText(getContext(), "กรุณากรอกข้อมูลให้ครบ", Toast.LENGTH_SHORT).show();
                    Log.d("bmi", "some field is empty");
                }
                else
                {
                    double weightNum = Double.parseDouble(weightStr);
                    double heightNum = Double.parseDouble(heightStr)/100;
                    double bmi = weightNum / (heightNum*heightNum);
                    String bmiStr = String.format("%.2f", bmi);
                    TextView bmiValue = getView().findViewById(R.id.bmi_bmi_value);
                    bmiValue.setText(bmiStr);
                    Log.d("bmi", "bmi is : " + bmiStr);
                }
            }
        });
    }
}
