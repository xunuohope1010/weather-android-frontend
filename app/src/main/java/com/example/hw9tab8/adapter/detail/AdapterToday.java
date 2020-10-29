package com.example.hw9tab8.adapter.detail;

import android.content.Context;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hw9tab8.R;
import com.example.hw9tab8.bean.City;

import java.util.ArrayList;

public class AdapterToday extends RecyclerView.Adapter<AdapterToday.MyViewHolder> {
    private Context context;
    private int[]icon;
    private String[]titleArr;
    private ArrayList<String>values;

    public AdapterToday(Context context, ArrayList<String> values, int[]icon, String[]titleArr) {
        this.context = context;
        this.icon = icon;
        this.titleArr = titleArr;
        this.values = values;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = View.inflate(context, R.layout.one_of_nine,null);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.tvData.setText(values.get(position));
        holder.tvType.setText(titleArr[position]);
        holder.ivIcon.setImageResource(icon[position]);

        int dimensionInDp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 90, context.getResources().getDisplayMetrics());
        if (position==4){
            holder.tvData.setVisibility(View.GONE);
            holder.ivIcon.getLayoutParams().height = dimensionInDp;
            holder.ivIcon.getLayoutParams().width = dimensionInDp;
        }
        if (position==7){
            holder.ivIcon.setColorFilter(context.getResources().getColor(R.color.purple));
        }

    }

    @Override
    public int getItemCount() {
        return 9;
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
