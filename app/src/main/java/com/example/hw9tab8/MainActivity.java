package com.example.hw9tab8;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.hw9tab8.activity.DetailActivity;
import com.example.hw9tab8.activity.ResultActivity;
import com.example.hw9tab8.activity.SplashActivity;
import com.example.hw9tab8.adapter.main.ApiCall;
import com.example.hw9tab8.adapter.main.AutoSuggestAdapter;
import com.example.hw9tab8.adapter.main.MainPagerAdapter;
import com.example.hw9tab8.adapter.main.AdapterCurrent;
import com.example.hw9tab8.adapter.main.AdapterWeekly;
import com.example.hw9tab8.bean.City;
import com.example.hw9tab8.bean.CityList;
import com.example.hw9tab8.bean.CityTemp;
import com.example.hw9tab8.bean.Match;
import com.example.hw9tab8.bean.MyView;

import com.example.hw9tab8.bean.Weekly;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity {
    //    private List<View> viewList = new ArrayList<>();//ViewPager数据源
//    private MainPagerAdapter myPagerAdapter;//适配器
    private ViewPager viewPager;
    private TabLayout tabLayout;

    private Handler handler;
    private AutoSuggestAdapter autoSuggestAdapter;
    private static final int TRIGGER_AUTO_COMPLETE = 100;
    private static final long AUTO_COMPLETE_DELAY = 300;

    //    AppCompatAutoCompleteTextView autoCompleteTextView;
    SearchView.SearchAutoComplete searchAutoComplete;

    private int count = 0;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MyView.myPagerAdapter = new MainPagerAdapter(MyView.viewList);//创建适配器实例
        viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(MyView.myPagerAdapter);//为ViewPager设置适配器

        tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        final View pb = findViewById(R.id.pb);
        pb.setVisibility(View.VISIBLE);

        final ArrayList<City>citiesOld = readFile();
        if (citiesOld.size()==0){
            CityList.initCities(1);
        } else {
            CityList.initCities(citiesOld.size());
        }


        requestQueue = Volley.newRequestQueue(MainActivity.this);

        String ipApiURL = "http://ip-api.com/json";
        JsonObjectRequest ipRequest = new JsonObjectRequest(ipApiURL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject ipResponse) {

                String countryCode = ipResponse.optString("countryCode");
                String region = ipResponse.optString("region");
                String name = ipResponse.optString("city");

                String location = name+","+region+","+countryCode;

                String timezone = ipResponse.optString("timezone");

                double lat = ipResponse.optDouble("lat");
                double lng = ipResponse.optDouble("lon");

                final City cityParam = new City();
                cityParam.setName(name);
                cityParam.setLocation(location);
                cityParam.setTimezone(timezone);

                String weatherUrl = MyView.REST_API+ "weatherSearch?latitude=" + lat + "&longitude=" + lng + "&location=" + name;
                Log.d("URL",weatherUrl);
                JsonObjectRequest weatherRequest = new JsonObjectRequest(weatherUrl, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject weatherResponse) {
                        City city = setCityData(weatherResponse,cityParam);
                        CityList.setCityIndex(0,city);
//                        CityList.addCity(city);
                        CityTemp.setCity(city);
                        if (citiesOld.size()==0 || citiesOld.size()==1){
                            loadPage();
                            pb.setVisibility(View.GONE);
                        } else {
                            loadOldData(citiesOld, pb);
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

                requestQueue.add(weatherRequest);


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("TAG", error.toString());
            }
        });
        requestQueue.add(ipRequest);





    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);

        // Get the search menu.
        MenuItem searchMenu = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) searchMenu.getActionView();
        searchAutoComplete = searchView.findViewById(androidx.appcompat.R.id.search_src_text);
        searchAutoComplete.setBackgroundColor(getResources().getColor(R.color.gray));
        searchAutoComplete.setTextColor(Color.WHITE);
        searchAutoComplete.setDropDownBackgroundResource(android.R.color.white);
        autoSuggestAdapter = new AutoSuggestAdapter(this, android.R.layout.simple_dropdown_item_1line);

        searchAutoComplete.setAdapter(autoSuggestAdapter);

//        searchAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
////                Toast.makeText(MainActivity.this, autoSuggestAdapter.getObject(position), Toast.LENGTH_SHORT).show();
//                String location = autoSuggestAdapter.getObject(position);
//                String[] locationArr = location.split(",");
//                String name = locationArr[0];
//
//                City city = new City();
//                city.setName(name);
//                city.setLocation(location);
//                CityTemp.setCity(city);
//
//                Intent intent = new Intent(MainActivity.this, ResultActivity.class);
//                startActivity(intent);
//
//            }
//        });

        searchAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String queryString = (String) parent.getItemAtPosition(position);
                searchAutoComplete.setText(String.valueOf(queryString));
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                String[] locationArr = query.split(",");
                String name = locationArr[0];

                City city = new City();
                city.setName(name);
                city.setLocation(query);
                CityTemp.setCity(city);

                Intent intent = new Intent(MainActivity.this, ResultActivity.class);
                startActivity(intent);

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        searchAutoComplete.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                handler.removeMessages(TRIGGER_AUTO_COMPLETE);
                handler.sendEmptyMessageDelayed(TRIGGER_AUTO_COMPLETE, AUTO_COMPLETE_DELAY);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                if (msg.what == TRIGGER_AUTO_COMPLETE) {
                    if (!TextUtils.isEmpty(searchAutoComplete.getText())) {
                        makeApiCall(searchAutoComplete.getText().toString());
                    }
                }
                return false;
            }
        });

        return true;
    }


    private void makeApiCall(String text) {
        ApiCall.make(this, text, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d("TAG", response.toString());
                ArrayList<String> list = new ArrayList<>();
                for (int i = 0; i < response.length(); i++) {
                    list.add(response.optString(i));
                }
                autoSuggestAdapter.setList(list);
                autoSuggestAdapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
    }

    private void delPage(City cityOne) {
//        int position = viewPager.getCurrentItem();//获取当前页面位置
        int position = CityList.getCityPosition(cityOne);
        MyView.viewList.remove(position);//删除一项数据源中的数据
        MyView.myPagerAdapter.notifyDataSetChanged();//通知UI更新

    }

    private void addPage(final City city) {
        Log.d("TAG", city.toString());
        LayoutInflater inflater = LayoutInflater.from(this);//获取LayoutInflater的实例
        View view = inflater.inflate(R.layout.one_pager_main, null);//调用LayoutInflater实例的inflate()方法来加载页面的布局
////////////////////////////////////

        TextView tvTemperature = view.findViewById(R.id.tv_temperature);//获取该View对象的TextView实例
        String temperatureStr = city.getTemperature() + "°F";
        tvTemperature.setText(temperatureStr);

        TextView tvSummary = view.findViewById(R.id.tv_summary);
        tvSummary.setText(city.getSummary());

        TextView tvLocation = view.findViewById(R.id.tv_location);

        tvLocation.setText(city.getLocation());


        ImageView ivIcon = view.findViewById(R.id.iv_icon);

        for (int i = 0; i < Match.iconText.length; i++) {
            if (city.getIcon().equals(Match.iconText[i])) {
                ivIcon.setImageResource(Match.iconId[i]);
            }
        }


        String[] iconTitle = {"Humidity", "Wind Speed", "Visibility", "Pressure"};
        int[] icon = {R.drawable.water_percent, R.drawable.weather_windy, R.drawable.eye_outline,
                R.drawable.gauge};

        ArrayList<String> values = new ArrayList<>();
        values.add(city.getHumidity() + "%");
        values.add(city.getWindSpeed() + " mph");
        values.add(city.getVisibility() + " km");
        values.add(city.getPressure() + " mb");

        RecyclerView recyclerViewCurrent = view.findViewById(R.id.rv_current);

        AdapterCurrent adapterCurrent = new AdapterCurrent(MainActivity.this, icon, values, iconTitle);
        recyclerViewCurrent.setAdapter(adapterCurrent);

        recyclerViewCurrent.setLayoutManager(new GridLayoutManager(MainActivity.this, 4, GridLayoutManager.VERTICAL, false));

        RecyclerView recyclerViewWeekly = view.findViewById(R.id.rv_weekly);
        AdapterWeekly adapterWeekly = new AdapterWeekly(MainActivity.this, city);
        recyclerViewWeekly.setAdapter(adapterWeekly);

        recyclerViewWeekly.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false));


        View cvDetail = view.findViewById(R.id.cv_detail);
        cvDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CityTemp.setCity(city);
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                startActivity(intent);
            }
        });

        final FloatingActionButton fabAddDelete = view.findViewById(R.id.fab_add_delete);

        if (!CityList.isFirstCity(city)) {
            if (CityList.isCityExist(city)) {
//                fabAddDelete.setBackgroundResource(R.drawable.map_marker_minus);
                fabAddDelete.setImageResource(R.drawable.map_marker_minus);
            } else {
//                fabAddDelete.setBackgroundResource(R.drawable.map_marker_plus);
                fabAddDelete.setImageResource(R.drawable.map_marker_plus);
            }
        } else {
            fabAddDelete.hide();
        }

        fabAddDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!CityList.isCityExist(city)) {
                    Toast.makeText(MainActivity.this, city.getLocation() + " was added to favorites", Toast.LENGTH_SHORT).show();
                    CityList.addCity(city);
                    addPage(city);
                    writeFile(CityList.getCities());
                    fabAddDelete.setBackgroundResource(R.drawable.map_marker_minus);
                } else {
                    Toast.makeText(MainActivity.this, city.getLocation() + " was removed from favorites", Toast.LENGTH_SHORT).show();
                    delPage(city);
                    CityList.removeCity(city);
                    writeFile(CityList.getCities());
                    fabAddDelete.setBackgroundResource(R.drawable.map_marker_plus);
                }
            }
        });


/////////////////////////////////////////////////////////
        MyView.viewList.add(view);//为数据源添加一项数据
        MyView.myPagerAdapter.notifyDataSetChanged();//通知UI更新

    }

    public void writeFile(ArrayList<City> cities) {
        SharedPreferences.Editor prefsEditor = getSharedPreferences("favorite", 0).edit();
        Gson gson = new Gson();
        String json = gson.toJson(cities); // myObject - instance of MyObject
        prefsEditor.putString("cities", json);
        prefsEditor.apply();
    }


    public ArrayList<City> readFile(){
        ArrayList<City>cities = new ArrayList<>();
        Gson gson = new Gson();
        SharedPreferences prefs = getSharedPreferences("favorite", 0);
        String json = prefs.getString("cities", null);
        if (json != null) {
            Type cityListType = new TypeToken<ArrayList<City>>() {
            }.getType();
            cities = gson.fromJson(json, cityListType);
            Log.d("TAGread",cities.toString());
        }

        return cities;
    }


    private City setCityData(JSONObject weatherResponse, City cityParam) {

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

        City city = new City();
        city.setName(cityParam.getName());
        city.setTimezone(cityParam.getTimezone());

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
            Log.d("TAG",String.valueOf(object.optLong("time")));
            weeklies.add(weekly);
        }
        city.setList(weeklies);

        JSONArray imgArr = weatherResponse.optJSONArray("imgArr");
        for (int i = 0; i < imgArr.length(); i++) {
            city.addImgList(imgArr.optString(i));
        }

        return city;
    }


    public void loadPage(){
        City city = CityTemp.getCity();
        addPage(city);

        ArrayList<City> cities = CityList.getCities();

        Log.d("TAGLIST", CityList.getCities().toString());

        for (int i = 1; i < cities.size(); i++) {
            addPage(cities.get(i));
        }
    }

    public void loadOldData(final ArrayList<City>citiesOld, final View pb){

        Log.d("TAGold", citiesOld.toString());
        for (int i=1;i<citiesOld.size();i++){
            String locationUrl = MyView.REST_API;

            City cityStored = citiesOld.get(i);

            locationUrl += "weatherSearch?locationSearch=" + cityStored.getLocation();
            final City cityParam = new City();
            cityParam.setName(cityStored.getName());
            cityParam.setLocation(cityStored.getLocation());
            cityParam.setTimezone(cityStored.getTimezone());
            Log.d("TAG before request", cityParam.getName());
            final int finalI = i;
            JsonObjectRequest locationRequest = new JsonObjectRequest(locationUrl, null, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    count++;
                    Log.d("TAG after request", cityParam.getName());
                    City city = setCityData(response, cityParam);
//                    CityList.addCity(city);
                    CityList.setCityIndex(finalI,city);

                    if (count==citiesOld.size()-1){
                        Log.d("TAG","loadpage");
                        loadPage();
                        pb.setVisibility(View.GONE);

                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
            requestQueue.add(locationRequest);
        }

    }


}

