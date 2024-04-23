package com.example.designweather;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class HourlyAdapter extends RecyclerView.Adapter<HourlyAdapter.viewholder> {
    ArrayList<Hourly> items;
    Context context;

    public HourlyAdapter(ArrayList<Hourly> items) {
        this.items = items;

    }

    @NonNull
    @Override
    public HourlyAdapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context=parent.getContext();
        View inflate= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_hourly,parent,false);
        return new viewholder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull HourlyAdapter.viewholder holder, int position) {
        holder.txtHour.setText(items.get(position).getHour());
        holder.txtTemp.setText(items.get(position).getTemp()+ "°C");
        Picasso.get().load("https://openweathermap.org/img/wn/"+items.get(position).getImage()+".png").into(holder.imgWeather);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class viewholder extends RecyclerView.ViewHolder {
        TextView txtHour,txtTemp;
        ImageView imgWeather;
        public viewholder(@NonNull View itemView) {
            super(itemView);
            txtHour=itemView.findViewById(R.id.txtHour);
            txtTemp=itemView.findViewById(R.id.txttemphourly);
            imgWeather=itemView.findViewById(R.id.imgStatus);
        }
    }
}
