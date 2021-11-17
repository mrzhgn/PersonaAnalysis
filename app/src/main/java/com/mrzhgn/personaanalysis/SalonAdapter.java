package com.mrzhgn.personaanalysis;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SalonAdapter extends RecyclerView.Adapter<SalonAdapter.ViewHolder> {

    private LayoutInflater inflater;
    private List<Salon> salons;
    private OnItemListener mOnItemListener;

    SalonAdapter(Context context, List<Salon> salons, OnItemListener onItemListener) {
        this.salons = salons;
        this.inflater = LayoutInflater.from(context);
        mOnItemListener = onItemListener;
    }
    @Override
    public SalonAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.salon_item, null);
        return new SalonAdapter.ViewHolder(view, mOnItemListener);
    }

    @Override
    public void onBindViewHolder(SalonAdapter.ViewHolder holder, int position) {
        Salon salon = salons.get(position);
        holder.titleView.setText(salon.getTitle());
        holder.adresView.setText(salon.getAdres());
    }

    public void clear() {
        salons.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<Salon> list) {
        salons.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return salons.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final TextView titleView, adresView;

        OnItemListener onItemListener;

        ViewHolder(@NonNull View view, OnItemListener onItemListener){
            super(view);
            titleView = (TextView) view.findViewById(R.id.salon_text);
            adresView = (TextView) view.findViewById(R.id.salon_adress);
            this.onItemListener = onItemListener;

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Salon clicked = salons.get(getAdapterPosition());
            onItemListener.onItemClick(clicked);
        }
    }

    public interface OnItemListener {
        void onItemClick(Salon salon);
    }

}
