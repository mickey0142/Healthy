package com.example.mickey.healthy.Weight;

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
import android.widget.Toast;

import com.example.mickey.healthy.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class WeightFormFragment extends Fragment {
    FirebaseAuth fbAuth;
    FirebaseFirestore fbStore;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_weight_form, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        fbAuth = FirebaseAuth.getInstance();
        fbStore = FirebaseFirestore.getInstance();

        final String uid = fbAuth.getUid();
        Button addButton = getView().findViewById(R.id.weight_form_add_button);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText weight = getView().findViewById(R.id.weight_form_weight);
                EditText date = getView().findViewById(R.id.weight_form_date);
                int weightInt = Integer.parseInt(weight.getText().toString());
                String dateStr = date.getText().toString().replace("/", "-");
                Weight weightObj = new Weight(dateStr, weightInt);
                fbStore.collection("myfitness").document(uid).collection("weight").document(dateStr).set(weightObj)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d("weight", "add weight to firebase");
                                getActivity()
                                        .getSupportFragmentManager()
                                        .beginTransaction()
                                        .replace(R.id.main_view, new WeightFragment())
                                        .commit();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("weight", "Error from weightform : " + e.getMessage());
                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        Button backButton = getView().findViewById(R.id.weight_form_back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("weight", "back to weight");
                getActivity()
                        .getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.main_view, new WeightFragment())
                        .commit();
            }
        });
    }
}
