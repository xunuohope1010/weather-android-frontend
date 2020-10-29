package com.example.hw9tab8.adapter.main;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class AutoSuggestAdapter extends ArrayAdapter<String> implements Filterable {
    private ArrayList<String>list;
    public AutoSuggestAdapter(@NonNull Context context, int resource) {
        super(context, resource);
        list = new ArrayList<>();
    }
    public void setList(ArrayList<String>list){
        this.list.clear();
        this.list.addAll(list);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Nullable
    @Override
    public String getItem(int position) {
        return list.get(position);
    }
    public String getObject(int position){
        return list.get(position);
    }

    @NonNull
    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if (constraint!=null){
                    filterResults.values=list;
                    filterResults.count=list.size();
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results!=null&&results.count>0){
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }
            }
        };
        return filter;
    }
}
