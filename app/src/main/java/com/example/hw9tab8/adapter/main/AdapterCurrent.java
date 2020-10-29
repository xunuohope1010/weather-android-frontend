package com.example.hw9tab8.adapter.main;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hw9tab8.R;
import com.example.hw9tab8.bean.City;

import java.util.ArrayList;

public class AdapterCurrent extends RecyclerView.Adapter<AdapterCurrent.MyViewHolder> {
    private Context context;
    private int [] icon;
    private ArrayList<String>values;
    private String[]title;

    public AdapterCurrent(Context context, int []icon, ArrayList<String>values,String[]title) {
        this.context = context;
        this.icon = icon;
        this.values = values;
        this.title = title;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = View.inflate(context, R.layout.one_of_four,null);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.ivIcon.setImageResource(icon[position]);
        holder.tvType.setText(title[position]);
        holder.tvData.setText(values.get(position));

    }

    @Override
    public int getItemCount() {
        return 4;
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        private ImageView ivIcon;
        private TextView tvData;
        private TextView tvType;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ivIcon = itemView.findViewById(R.id.iv_icon);
            tvData = itemView.findViewById(R.id.tv_data);
            tvType = itemView.findViewById(R.id.tv_type);


        }
    }
}
