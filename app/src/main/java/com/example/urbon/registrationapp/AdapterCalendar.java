package com.example.urbon.registrationapp;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.urbon.registrationapp.models.Hour;

import java.util.List;

public class AdapterCalendar extends RecyclerView.Adapter<AdapterCalendar.MyViewHolder> {
    private List<Hour> hourList;

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView timeText, phoneText, ownerText, petText;

        MyViewHolder(View view) {
            super(view);
            timeText = view.findViewById(R.id.timeText);
            phoneText = view.findViewById(R.id.phoneText);
            ownerText = view.findViewById(R.id.ownerName);
            petText = view.findViewById(R.id.petName);
        }
    }

    public AdapterCalendar(List<Hour> hourList) {
        this.hourList = hourList;
    }

    public void updateList(List<Hour> hourList) {
        this.hourList = hourList;
        this.notifyDataSetChanged();
    }

    @Override
    public AdapterCalendar.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_hour, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(AdapterCalendar.MyViewHolder holder, int position) {
        Hour hour = hourList.get(position);
        holder.timeText.setText(hour.getTime());
        holder.phoneText.setText(hour.getPhone());
        holder.ownerText.setText(hour.getOwnerName());
        holder.petText.setText(hour.getPetName());
    }

    @Override
    public int getItemCount() {
        return hourList.size();
    }
}
