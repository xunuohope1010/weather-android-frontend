package com.example.hw9tab8.adapter.detail;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hw9tab8.R;
import com.example.hw9tab8.bean.City;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdapterPhoto extends RecyclerView.Adapter<AdapterPhoto.MyViewHolder> {
    private Context context;
    private ArrayList<String>list;

    public AdapterPhoto(Context context, City city) {
        this.context = context;
        this.list = city.getImgList();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = View.inflate(context, R.layout.one_photo,null);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Picasso.with(context).load(list.get(position)).fit().centerCrop().into(holder.ivImg);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        private ImageView ivImg;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ivImg = itemView.findViewById(R.id.iv_img);
        }
    }
}
