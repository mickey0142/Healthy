package com.example.mickey.healthy;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.mickey.healthy.Weight.WeightFragment;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class MenuFragment extends Fragment {
    ArrayList<String> menu = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        menu.add("BMI");
        menu.add("Weight");
        menu.add("Sleep");
        menu.add("SignOut");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_menu, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final ArrayAdapter<String> menuAdapter = new ArrayAdapter<String>(
                getActivity(), android.R.layout.simple_list_item_1, menu
        );

        ListView menuList = getView().findViewById(R.id.menu_list);
        menuList.setAdapter(menuAdapter);
        menuList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long l) {
                Log.d("menu", "click on : " + menu.get(i));
                if (i == 0)
                {
                    getActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.main_view, new BMIFragment())
                            .commit();
                }
                else if (i == 1)
                {
                    getActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.main_view, new WeightFragment())
                            .commit();
                }
                else if (i == 2)
                {
                    getActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.main_view, new SleepFragment())
                            .addToBackStack(null)
                            .commit();
                }
                else if (i == 3)
                {
                    FirebaseAuth.getInstance().signOut();
                    getActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.main_view, new LoginFragment())
                            .commit();
                }
            }
        });
    }
}
