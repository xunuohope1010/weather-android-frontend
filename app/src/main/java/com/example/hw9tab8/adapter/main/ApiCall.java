package com.example.hw9tab8.adapter.main;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.hw9tab8.bean.MyView;

import org.json.JSONArray;

public class ApiCall {
    private static ApiCall apiCall;
    private RequestQueue requestQueue;
    private Context context;

    public ApiCall(Context context) {
        this.context = context;
        this.requestQueue = getRequestQueue();
    }


    private RequestQueue getRequestQueue() {
        if (requestQueue==null){
            requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
        return requestQueue;
    }
    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

    public static void make(Context context, String query, Response.Listener<JSONArray>listener, Response.ErrorListener errorListener) {
        String url = MyView.REST_API +"weatherSearch?keywords=" + query;
        Log.d("URL",url);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url,listener,errorListener);
        ApiCall.getInstance(context).addToRequestQueue(jsonArrayRequest);
    }

    private static ApiCall getInstance(Context context) {
        if (apiCall==null){
            apiCall = new ApiCall(context);
        }
        return apiCall;
    }
}
