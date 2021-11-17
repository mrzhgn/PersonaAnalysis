package com.mrzhgn.personaanalysis;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

class DataAdapter2 extends RecyclerView.Adapter<DataAdapter2.ViewHolder> {

    private LayoutInflater inflater;
    private List<DataItem> dataItems;

    DataAdapter2(Context context, List<DataItem> dataItems) {
        this.dataItems = dataItems;
        this.inflater = LayoutInflater.from(context);
    }
    @Override
    public DataAdapter2.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_2, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DataAdapter2.ViewHolder holder, int position) {
        DataItem dataItem = dataItems.get(position);
        holder.titleView.setText(dataItem.getTitle());
        holder.valueView.setText(dataItem.getValue());

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

        ViewHolder(@NonNull View view){
            super(view);
            titleView = (TextView) view.findViewById(R.id.title);
            valueView = (TextView) view.findViewById(R.id.value);
        }
    }
}