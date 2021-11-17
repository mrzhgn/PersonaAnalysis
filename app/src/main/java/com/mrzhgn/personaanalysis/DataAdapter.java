package com.mrzhgn.personaanalysis;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder> {

    private LayoutInflater inflater;
    private List<DataItem> dataItems;

    DataAdapter(Context context, List<DataItem> dataItems) {
        this.dataItems = dataItems;
        this.inflater = LayoutInflater.from(context);
    }
    @Override
    public DataAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DataAdapter.ViewHolder holder, int position) {
        DataItem dataItem = dataItems.get(position);
        holder.titleView.setText(dataItem.getTitle());
        holder.valueView.setText(dataItem.getValue());
        GradientDrawable background = (GradientDrawable) holder.thresholdLayout.getBackground();
        if (position % 2 == 0) background.setColor(Color.parseColor("#ff6f74"));
        else background.setColor(Color.parseColor("#74c652"));
    }

    public void clear() {
        dataItems.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<DataItem> list) {
        dataItems.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return dataItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        final TextView titleView, valueView;
        final LinearLayout thresholdLayout;

        ViewHolder(@NonNull View view){
            super(view);
            titleView = (TextView) view.findViewById(R.id.title);
            valueView = (TextView) view.findViewById(R.id.value);
            thresholdLayout = (LinearLayout) view.findViewById(R.id.threshold);
        }
    }
}