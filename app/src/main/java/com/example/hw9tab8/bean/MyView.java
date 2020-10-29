package com.example.hw9tab8.bean;

import android.view.View;

import com.example.hw9tab8.adapter.main.MainPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class MyView {
    public static List<View> viewList = new ArrayList<>();//ViewPager数据源
    public static MainPagerAdapter myPagerAdapter;//适配器
    public static String REST_API = "https://fine-branch-259801.appspot.com/";
//    public static String REST_API = "http://10.0.2.2:8080/";
}
