package com.example.hw9tab8.adapter.main;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hw9tab8.R;
import com.example.hw9tab8.bean.City;
import com.example.hw9tab8.bean.CityList;
import com.example.hw9tab8.bean.Match;
import com.example.hw9tab8.bean.Weekly;

import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

public class AdapterWeekly extends RecyclerView.Adapter<AdapterWeekly.MyViewHolder> {
    private Context context;
    private ArrayList<Weekly>list;
    private long offset;

    public AdapterWeekly(Context context, City city) {
        this.context = context;
        list = city.getList();

        String timezoneTarget = city.getTimezone();
        String timezoneClient = CityList.getFirstCity().getTimezone();
        TimeZone clientTime =TimeZone.getTimeZone(timezoneClient);
        long offsetClient = clientTime.getRawOffset();
        TimeZone targetTime =TimeZone.getTimeZone(timezoneTarget);
        long offsetTarget = targetTime.getRawOffset();
        offset = offsetTarget - offsetClient;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = View.inflate(context, R.layout.one_day_list,null);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Weekly weekly = list.get(position);

        Date date = new Date(weekly.getTime() * 1000+offset*2);
        holder.tvDate.setText(android.text.format.DateFormat.format("MM/dd/yyyy", date));
        holder.tvTemperatureLow.setText(String.valueOf(weekly.getTemperatureLow()));
        holder.tvTemperatureHigh.setText(String.valueOf(weekly.getTemperatureHigh()));

        for (int i=0;i< Match.iconText.length;i++){
            if (weekly.getIcon().equals(Match.iconText[i])){
                holder.ivIcon.setImageResource(Match.iconId[i]);
            }
        }


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        private ImageView ivIcon;
        private TextView tvDate;
        private TextView tvTemperatureLow;
        private TextView tvTemperatureHigh;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ivIcon = itemView.findViewById(R.id.iv_icon);
            tvDate = itemView.findViewById(R.id.tv_date);
            tvTemperatureLow = itemView.findViewById(R.id.tv_temperature_low);
            tvTemperatureHigh = itemView.findViewById(R.id.tv_temperature_high);
        }
    }
}
