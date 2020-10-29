package com.example.hw9tab8.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hw9tab8.R;

import com.example.hw9tab8.adapter.detail.AdapterPhoto;
import com.example.hw9tab8.adapter.detail.AdapterToday;
import com.example.hw9tab8.adapter.detail.DetailPagerAdapter;
import com.example.hw9tab8.bean.City;
import com.example.hw9tab8.bean.CityTemp;
import com.example.hw9tab8.bean.Match;
import com.example.hw9tab8.bean.Weekly;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity {
    private List<View> viewList = new ArrayList<>();//ViewPager数据源
    private DetailPagerAdapter detailPagerAdapter;//适配器
    private ViewPager viewPager;
    private int count = 0; //页面展示的数据，无实际作用
    private TabLayout tabLayout;

    private City city;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_controller);

        city = CityTemp.getCity();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setTitle(city.getLocation());

        String[] titleArr = {"Today", "Weekly", "Photos"};

        detailPagerAdapter = new DetailPagerAdapter(viewList, titleArr);//创建适配器实例
        viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(detailPagerAdapter);//为ViewPager设置适配器

        tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);


        addToday();
        addWeekly();
        addPhotos();

        tabLayout.getTabAt(0).setText("Today");
        tabLayout.getTabAt(1).setText("Weekly");
        tabLayout.getTabAt(2).setText("Photos");

        tabLayout.getTabAt(0).setIcon(R.drawable.calendar_today);
        tabLayout.getTabAt(1).setIcon(R.drawable.trending_up);
        tabLayout.getTabAt(2).setIcon(R.drawable.google_photos);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tab.getIcon().setColorFilter(getResources().getColor(android.R.color.white), PorterDuff.Mode.SRC_IN);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                tab.getIcon().setColorFilter(getResources().getColor(android.R.color.darker_gray), PorterDuff.Mode.SRC_IN);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                tab.getIcon().setColorFilter(getResources().getColor(android.R.color.white), PorterDuff.Mode.SRC_IN);
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.twitter_button, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {// app icon in action bar clicked; goto parent activity.
            finish();
            return true;
        }
        if (item.getItemId() == R.id.bt_twitter) {
//            Toast.makeText(DetailActivity.this, "twitter", Toast.LENGTH_SHORT).show();
            String text = "Check out " + city.getLocation() + "'s Weather! It's " + city.getTemperature() + "°F!%0A%23CSCI571WeatherSearch";
            Uri uri = Uri.parse("https://twitter.com/intent/tweet?text=" + text); // missing 'http://' will cause crashed
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    private void addToday() {
        LayoutInflater inflater = LayoutInflater.from(this);//获取LayoutInflater的实例
        View view = inflater.inflate(R.layout.today_pager, null);//调用LayoutInflater实例的inflate()方法来加载页面的布局


        RecyclerView recyclerViewToday = view.findViewById(R.id.rv_today);

        String[] titleArr = {"Wind Speed", "Pressure", "Precipitation", "Temperature",
                "", "Humidity", "Visibility", "Cloud Cover", "Ozone"};

        if (city.getIcon().equals("partly-cloudy-night")) {
            titleArr[4] = "cloudy night";
        }
        else if (city.getIcon().equals("partly-cloudy-day")) {
            titleArr[4] = "cloudy day";
        }
        else {
            titleArr[4] = city.getIcon().replace('-', ' ');
        }


        ArrayList<String> values = new ArrayList<>();
        values.add(city.getTemperature() + " mph");
        values.add(city.getPressure() + " mb");
        values.add(city.getPrecipIntensity() + " mmph");
        values.add(city.getTemperature() + "°F");
        values.add("");
        values.add(city.getHumidity() + "%");
        values.add(city.getVisibility() + " km");
        values.add(city.getCloudCover() + "%");
        values.add(city.getOzone() + " DU");

        int[] icon = {R.drawable.weather_windy, R.drawable.gauge, R.drawable.weather_pouring,
                R.drawable.thermometer, 0, R.drawable.water_percent, R.drawable.eye_outline,
                R.drawable.weather_fog, R.drawable.earth};


        for (int i = 0; i < Match.iconText.length; i++) {
            if (city.getIcon().equals(Match.iconText[i])) {
                icon[4] = Match.iconId[i];
                break;
            }
        }


        AdapterToday adapterToday = new AdapterToday(DetailActivity.this, values, icon, titleArr);
        recyclerViewToday.setAdapter(adapterToday);

        recyclerViewToday.setLayoutManager(new GridLayoutManager(DetailActivity.this, 3, GridLayoutManager.VERTICAL, false));


        viewList.add(view);//为数据源添加一项数据
        detailPagerAdapter.notifyDataSetChanged();//通知UI更新
    }

    private void addWeekly() {
        LayoutInflater inflater = LayoutInflater.from(this);//获取LayoutInflater的实例
        View view = inflater.inflate(R.layout.weekly_pager, null);//调用LayoutInflater实例的inflate()方法来加载页面的布局

        TextView tvSummary = view.findViewById(R.id.tv_summary);
        tvSummary.setText(city.getSummaryDaily());

        ImageView ivIcon = view.findViewById(R.id.iv_icon);

        for (int i = 0; i < Match.iconText.length; i++) {
            if (city.getIconDaily().equals(Match.iconText[i])) {
                ivIcon.setImageResource(Match.iconId[i]);
            }
        }

        ArrayList<Entry> entriesLow = new ArrayList<>();
        ArrayList<Entry> entriesHigh = new ArrayList<>();
        ArrayList<Weekly> list = city.getList();
        Weekly weekly;
        for (int i = 0; i < list.size(); i++) {
            weekly = list.get(i);
            entriesLow.add(new Entry(i, weekly.getTemperatureLow()));
            entriesHigh.add(new Entry(i, weekly.getTemperatureHigh()));
        }

        LineChart lineChart = view.findViewById(R.id.lineChart);
        LineDataSet dataSetLow = new LineDataSet(entriesLow, "Minimum Temperature");
        LineDataSet dataSetHigh = new LineDataSet(entriesHigh, "Maximum Temperature");

        dataSetLow.setColor(getResources().getColor(android.R.color.holo_purple));
        dataSetHigh.setColor(getResources().getColor(android.R.color.holo_orange_light));

        LineData data = new LineData();
        data.addDataSet(dataSetLow);
        data.addDataSet(dataSetHigh);
        lineChart.setData(data);

        Legend legend = lineChart.getLegend();
        legend.setTextColor(getResources().getColor(android.R.color.white));
        legend.setTextSize(15f);
        legend.setFormSize(16f);

        legend.setXEntrySpace(25f);

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.TOP);
        xAxis.setTextColor(getResources().getColor(android.R.color.white));
        xAxis.setDrawGridLines(false);

        YAxis yAxisRight = lineChart.getAxisRight();
        yAxisRight.setTextColor(getResources().getColor(android.R.color.white));

        YAxis yAxisLeft = lineChart.getAxisLeft();
        yAxisLeft.setTextColor(getResources().getColor(android.R.color.white));

        viewList.add(view);//为数据源添加一项数据
        detailPagerAdapter.notifyDataSetChanged();//通知UI更新
    }

    private void addPhotos() {
        LayoutInflater inflater = LayoutInflater.from(this);//获取LayoutInflater的实例
        View view = inflater.inflate(R.layout.photo_pager, null);//调用LayoutInflater实例的inflate()方法来加载页面的布局


        RecyclerView recyclerViewPhoto = view.findViewById(R.id.rv_photos);
        AdapterPhoto adapterPhoto = new AdapterPhoto(DetailActivity.this, city);
        recyclerViewPhoto.setAdapter(adapterPhoto);

        recyclerViewPhoto.setLayoutManager(new LinearLayoutManager(DetailActivity.this, LinearLayoutManager.VERTICAL, false));


        viewList.add(view);//为数据源添加一项数据
        detailPagerAdapter.notifyDataSetChanged();//通知UI更新
    }
}
