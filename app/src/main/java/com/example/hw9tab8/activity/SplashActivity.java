package com.example.hw9tab8.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.hw9tab8.MainActivity;
import com.example.hw9tab8.R;
import com.example.hw9tab8.bean.City;
import com.example.hw9tab8.bean.CityList;
import com.example.hw9tab8.bean.CityTemp;
import com.example.hw9tab8.bean.MyView;
import com.example.hw9tab8.bean.Weekly;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

public class SplashActivity extends AppCompatActivity {

    /** Duration of wait **/
    private final int SPLASH_DISPLAY_LENGTH = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        getSupportActionBar().hide();

        ImageView ivDarkSky = findViewById(R.id.iv_dark_sky);

        Picasso.with(SplashActivity.this).load("https://darksky.net/dev/img/attribution/poweredby-darkbackground.png").into(ivDarkSky);



        /* New Handler to start the Menu-Activity
         * and close this Splash-Screen after some seconds.*/
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                Intent mainIntent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(mainIntent);
                finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }






}
