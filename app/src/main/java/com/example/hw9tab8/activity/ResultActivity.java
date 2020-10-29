package com.example.hw9tab8.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.hw9tab8.R;
import com.example.hw9tab8.adapter.main.AdapterCurrent;
import com.example.hw9tab8.adapter.main.AdapterWeekly;
import com.example.hw9tab8.bean.City;
import com.example.hw9tab8.bean.CityList;
import com.example.hw9tab8.bean.CityTemp;
import com.example.hw9tab8.bean.Match;
import com.example.hw9tab8.bean.MyView;
import com.example.hw9tab8.bean.Weekly;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class ResultActivity extends AppCompatActivity {
    private City cityParam;
    private FloatingActionButton fabAddDelete;
    private TextView tvResult;
    private View err;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.one_pager_main);

        cityParam = CityTemp.getCity();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setTitle(cityParam.getLocation());

        final View progressBar = findViewById(R.id.pb);
        progressBar.setVisibility(View.VISIBLE);

        fabAddDelete = findViewById(R.id.fab_add_delete);
        fabAddDelete.hide();

        tvResult = findViewById(R.id.tv_result);

        err = findViewById(R.id.err);

        RequestQueue requestQueue = Volley.newRequestQueue(ResultActivity.this);
        String locationUrl = MyView.REST_API;

        locationUrl += "weatherSearch?locationSearch=" + cityParam.getLocation();
        Log.d("URL",locationUrl);

        JsonObjectRequest locationRequest = new JsonObjectRequest(locationUrl, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("TAG", "get the data");
                City city = setCityData(response);
                progressBar.setVisibility(View.GONE);
                tvResult.setVisibility(View.VISIBLE);

                if (city.isValid()){
                    load(city);
                }else {
                    err.setVisibility(View.VISIBLE);
                }




            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(locationRequest);


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {// app icon in action bar clicked; goto parent activity.
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void load(final City city){

        TextView tvTemperature = findViewById(R.id.tv_temperature);//获取该View对象的TextView实例
        String temperatureStr = city.getTemperature()+"°F";
        tvTemperature.setText(temperatureStr);

        TextView tvSummary = findViewById(R.id.tv_summary);
        tvSummary.setText(city.getSummary());

        TextView tvLocation = findViewById(R.id.tv_location);

        tvLocation.setText(city.getLocation());

        ImageView ivIcon = findViewById(R.id.iv_icon);

        for (int i = 0; i< Match.iconText.length; i++){
            if (city.getIcon().equals(Match.iconText[i])){
                ivIcon.setImageResource(Match.iconId[i]);
            }
        }


        String[]iconTitle = {"Humidity","Wind Speed","Visibility","Pressure"};
        int []icon = {R.drawable.water_percent,R.drawable.weather_windy,R.drawable.eye_outline,
                R.drawable.gauge};

        ArrayList<String>values = new ArrayList<>();
        values.add(city.getHumidity()+"%");
        values.add(city.getWindSpeed()+" mph");
        values.add(city.getVisibility()+" km");
        values.add(city.getPressure()+" mb");

        RecyclerView recyclerViewCurrent = findViewById(R.id.rv_current);

        AdapterCurrent adapterCurrent = new AdapterCurrent(ResultActivity.this, icon,values,iconTitle);
        recyclerViewCurrent.setAdapter(adapterCurrent);

        recyclerViewCurrent.setLayoutManager(new GridLayoutManager(ResultActivity.this, 4, GridLayoutManager.VERTICAL, false));


        RecyclerView recyclerViewWeekly = findViewById(R.id.rv_weekly);
        AdapterWeekly adapterWeekly = new AdapterWeekly(ResultActivity.this, city);
        recyclerViewWeekly.setAdapter(adapterWeekly);

        recyclerViewWeekly.setLayoutManager(new LinearLayoutManager(ResultActivity.this, LinearLayoutManager.VERTICAL,false));


        View cvDetail = findViewById(R.id.cv_detail);
        cvDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CityTemp.setCity(city);
                Intent intent = new Intent(ResultActivity.this, DetailActivity.class);
                startActivity(intent);
            }
        });


        fabAddDelete.show();

        if (!CityList.isFirstCity(city)) {
            if (CityList.isCityExist(city)) {
                fabAddDelete.setImageResource(R.drawable.map_marker_minus);

            } else {
                fabAddDelete.setImageResource(R.drawable.map_marker_plus);
            }
        } else {
            fabAddDelete.hide();
        }

        fabAddDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!CityList.isCityExist(city)) {
                    Toast.makeText(ResultActivity.this, city.getLocation()+" was added to favorites", Toast.LENGTH_SHORT).show();
                    CityList.addCity(city);
                    addPage(city,ResultActivity.this);
                    Log.d("TAGlistAdd",CityList.getCities().toString());
                    writeFile(CityList.getCities());
                    fabAddDelete.setImageResource(R.drawable.map_marker_minus);
                } else {
                    Toast.makeText(ResultActivity.this, city.getLocation()+" was removed from favorites", Toast.LENGTH_SHORT).show();
                    delPage(city);
                    CityList.removeCity(city);
                    Log.d("TAGlistDelete",CityList.getCities().toString());
                    writeFile(CityList.getCities());
                    fabAddDelete.setImageResource(R.drawable.map_marker_plus);
                }
            }
        });

    }

    private City setCityData(JSONObject weatherResponse) {

        City city = new City();

        String status = weatherResponse.optString("status");
        if (status.equals("ZERO_RESULTS")){
            city.setValid(false);
            return city;
        }

        DecimalFormat df = new DecimalFormat("0.00");
        JSONObject currently = weatherResponse.optJSONObject("currently");
        String summary = currently.optString("summary");
        String icon = currently.optString("icon");
        double temperature = currently.optDouble("temperature");

        double humidity = currently.optDouble("humidity");
        double windSpeed = currently.optDouble("windSpeed");
        double visibility = currently.optDouble("visibility");
        double pressure = currently.optDouble("pressure");

        double precipIntensity = currently.optDouble("precipIntensity");
        double cloudCover = currently.optDouble("cloudCover");
        double ozone = currently.optDouble("ozone");
        String timeZone = weatherResponse.optString("timezone");


        city.setName(cityParam.getName());
        city.setTimezone(timeZone);
        city.setLocation(cityParam.getLocation());
        city.setSummary(summary);
        city.setTemperature((int) Math.round(temperature));
        city.setIcon(icon);
        city.setHumidity((int) Math.round(humidity * 100));

        city.setWindSpeed(df.format(windSpeed));
        city.setVisibility(df.format(visibility));
        city.setPressure(df.format(pressure));

        city.setPrecipIntensity(df.format(precipIntensity));
        city.setCloudCover((int) Math.round(cloudCover * 100));
        city.setOzone(df.format(ozone));

        JSONObject daily = weatherResponse.optJSONObject("daily");
        assert daily != null;

        String summaryDaily = daily.optString("summary");
        String iconDaily = daily.optString("icon");
        city.setSummaryDaily(summaryDaily);
        city.setIconDaily(iconDaily);

        JSONArray jsonArray = daily.optJSONArray("data");

        ArrayList<Weekly> weeklies = new ArrayList<>();
        assert jsonArray != null;
        for (int i = 0; i < jsonArray.length(); i++) {
            Weekly weekly = new Weekly();
            JSONObject object = jsonArray.optJSONObject(i);
            weekly.setTemperatureHigh((int) Math.round(object.optDouble("temperatureHigh")));
            weekly.setTemperatureLow((int) Math.round(object.optDouble("temperatureLow")));
            weekly.setIcon(object.optString("icon"));
            weekly.setTime(object.optLong("time"));
            weeklies.add(weekly);
        }
        city.setList(weeklies);

        JSONArray imgArr = weatherResponse.optJSONArray("imgArr");
        for (int i = 0; i < imgArr.length(); i++) {
            city.addImgList(imgArr.optString(i));
        }

        return city;
    }

    private void delPage(City cityOne) {
        Log.d("TAG","city name"+cityOne.getName());
        int position = CityList.getCityPosition(cityOne);
        Log.d("TAG","position"+position);
        MyView.viewList.remove(position);//删除一项数据源中的数据
        MyView.myPagerAdapter.notifyDataSetChanged();//通知UI更新

    }

    public void addPage(final City city, final Context context) {
        Log.d("TAG",city.toString());
        LayoutInflater inflater = LayoutInflater.from(this);//获取LayoutInflater的实例
        View view = inflater.inflate(R.layout.one_pager_main, null);//调用LayoutInflater实例的inflate()方法来加载页面的布局
////////////////////////////////////
        TextView tvTemperature = view.findViewById(R.id.tv_temperature);//获取该View对象的TextView实例
        String temperatureStr = city.getTemperature()+"°F";
        tvTemperature.setText(temperatureStr);

        TextView tvSummary = view.findViewById(R.id.tv_summary);
        tvSummary.setText(city.getSummary());

        TextView tvLocation = view.findViewById(R.id.tv_location);

        tvLocation.setText(city.getLocation());

        ImageView ivIcon = view.findViewById(R.id.iv_icon);

        for (int i = 0; i< Match.iconText.length; i++){
            if (city.getIcon().equals(Match.iconText[i])){
                ivIcon.setImageResource(Match.iconId[i]);
            }
        }

        String[]iconTitle = {"Humidity","Wind Speed","Visibility","Pressure"};
        int []icon = {R.drawable.water_percent,R.drawable.weather_windy,R.drawable.eye_outline,
                R.drawable.gauge};

        ArrayList<String>values = new ArrayList<>();
        values.add(city.getHumidity()+"%");
        values.add(city.getWindSpeed()+" mph");
        values.add(city.getVisibility()+" km");
        values.add(city.getPressure()+" mb");

        RecyclerView recyclerViewCurrent = view.findViewById(R.id.rv_current);

        AdapterCurrent adapterCurrent = new AdapterCurrent(ResultActivity.this, icon,values,iconTitle);
        recyclerViewCurrent.setAdapter(adapterCurrent);

        recyclerViewCurrent.setLayoutManager(new GridLayoutManager(ResultActivity.this, 4, GridLayoutManager.VERTICAL, false));


        RecyclerView recyclerViewWeekly = view.findViewById(R.id.rv_weekly);
        AdapterWeekly adapterWeekly = new AdapterWeekly(context, city);
        recyclerViewWeekly.setAdapter(adapterWeekly);

        recyclerViewWeekly.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false));


        View cvDetail = view.findViewById(R.id.cv_detail);
        cvDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CityTemp.setCity(city);
                Intent intent = new Intent(context, DetailActivity.class);

                startActivity(intent);
            }
        });

        final FloatingActionButton fabAddDelete = view.findViewById(R.id.fab_add_delete);

        if (!CityList.isFirstCity(city)) {
            if (CityList.isCityExist(city)) {
                fabAddDelete.setImageResource(R.drawable.map_marker_minus);
            } else {
                fabAddDelete.setImageResource(R.drawable.map_marker_plus);
            }
        } else {
            fabAddDelete.hide();
        }

        fabAddDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!CityList.isCityExist(city)) {
                    Toast.makeText(context, city.getLocation()+" was added to favorites", Toast.LENGTH_SHORT).show();
                    CityList.addCity(city);
                    Log.d("TAGlistAdd",CityList.getCities().toString());
                    addPage(city,context);
                    writeFile(CityList.getCities());
                    fabAddDelete.setImageResource(R.drawable.map_marker_minus);
                } else {
                    Toast.makeText(context, city.getLocation()+" was removed from favorites", Toast.LENGTH_SHORT).show();
                    delPage(city);
                    CityList.removeCity(city);
                    Log.d("TAGlistDelete",CityList.getCities().toString());
                    writeFile(CityList.getCities());
                    fabAddDelete.setImageResource(R.drawable.map_marker_plus);
                }
            }
        });

/////////////////////////////////////////////////////////
        MyView.viewList.add(view);//为数据源添加一项数据
        MyView.myPagerAdapter.notifyDataSetChanged();//通知UI更新

    }


    public void writeFile(ArrayList<City>cities){
        SharedPreferences.Editor prefsEditor = getSharedPreferences("favorite", 0).edit();
        Gson gson = new Gson();
        String json = gson.toJson(cities); // myObject - instance of MyObject
        prefsEditor.putString("cities", json);
        prefsEditor.apply();
    }


}
