package com.example.hw9tab8.adapter.detail;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class DetailPagerAdapter extends androidx.viewpager.widget.PagerAdapter {

    private List<View> viewList;
    private String[]titleArr;

    public DetailPagerAdapter(List<View> viewList,String[]titleArr) {
        this.viewList = viewList;
        this.titleArr = titleArr;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }

    @Override
    public int getCount() {
        return viewList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        container.addView(viewList.get(position));
        return viewList.get(position);
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }

//    @Nullable
//    @Override
//    public CharSequence getPageTitle(int position) {
//        if (position==0){
//            return "Today";
//        } else if (position==1){
//            return "weekly";
//        } else {
//            return "photos";
//        }
//    }

//    @Nullable
//    @Override
//    public CharSequence getPageTitle(int position) {
//        return titleArr[position];
//    }
}

