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
import android.widget.ListView;
import android.widget.Toolbar;

import com.example.mickey.healthy.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;

public class WeightFragment extends Fragment {
    ArrayList<Weight> weights = new ArrayList<>();
    FirebaseAuth fbAuth;
    FirebaseFirestore fbStore;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_weight, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        fbAuth = FirebaseAuth.getInstance();
        fbStore = FirebaseFirestore.getInstance();
        final ListView weightList = getView().findViewById(R.id.weight_weight_list);
        final WeightAdapter weightAdapter = new WeightAdapter(getActivity(), R.layout.fragment_weight_item, weights);
        weightList.setAdapter(weightAdapter);

        Button addButton = getView().findViewById(R.id.weight_add_button);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity()
                        .getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.main_view, new WeightFormFragment())
                        .commit();
                Log.d("weight", "go to weightform");
            }
        });

        fbStore.collection("myfitness").document(fbAuth.getUid()).collection("weight")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@javax.annotation.Nullable QuerySnapshot DocumentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {
                        weightAdapter.clear();
                        for (QueryDocumentSnapshot doc: DocumentSnapshots)
                        {
                            weights.add(doc.toObject(Weight.class));
                        }
                        Collections.reverse(weights);
                        for (int i=0; i < weights.size() - 1 ; i++)
                        {
                            if (weights.get(i).getWeight() > weights.get(i + 1).getWeight())
                            {
                                weights.get(i).setStatus("Up");
                            }
                            else if (weights.get(i).getWeight() < weights.get(i + 1).getWeight())
                            {
                                weights.get(i).setStatus("Down");
                            }
                        }
                        weightAdapter.notifyDataSetChanged();
                    }
                });
    }
}
