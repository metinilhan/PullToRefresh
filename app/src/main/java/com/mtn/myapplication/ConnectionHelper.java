package com.mtn.myapplication;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


public class ConnectionHelper {

    private static ConnectionHelper connectionHelper;
    private final RequestQueue queue;

    private ConnectionHelper(Context context) {
        queue = Volley.newRequestQueue(context);
    }

    public static ConnectionHelper getInstance(Context context) {
        if (connectionHelper == null)
            connectionHelper = new ConnectionHelper(context);
        return connectionHelper;
    }


    public void get(ConnectionResponce connectionResponce, boolean isRefresh) {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, connectionResponce.getUrl(),
                response -> connectionResponce.onSuccess(response, isRefresh), error -> connectionResponce.onFail(error.getMessage()));
        queue.add(stringRequest);
    }

    public void get() {
    }
}
