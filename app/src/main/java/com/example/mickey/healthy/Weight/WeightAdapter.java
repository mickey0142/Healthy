package com.example.mickey.healthy.Weight;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.mickey.healthy.R;

import java.util.ArrayList;
import java.util.List;

public class WeightAdapter extends ArrayAdapter<Weight> {
    List<Weight> weights = new ArrayList<Weight>();
    Context context;

    public WeightAdapter(Context context, int resource, List<Weight> objects)
    {
        super(context, resource, objects);
        this.weights = objects;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {
        View weightItem = LayoutInflater.from(context).inflate(R.layout.fragment_weight_item, parent, false);
        TextView date = weightItem.findViewById(R.id.weight_item_date);
        TextView weight = weightItem.findViewById(R.id.weight_item_weight);
        TextView status = weightItem.findViewById(R.id.weight_item_status);

        Weight row = weights.get(position);
        date.setText(row.getDate());
        weight.setText(Integer.toString(row.getWeight()));
        status.setText(row.getStatus());

        return weightItem;
    }
}
