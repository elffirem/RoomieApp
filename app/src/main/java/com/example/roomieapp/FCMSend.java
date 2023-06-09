package com.example.roomieapp;

import android.content.Context;
import android.os.StrictMode;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class FCMSend {
    private static String BASE_URL="https://fcm.googleapis.com/fcm/send";
    private static String SERVER_KEY="key=AAAAzFwjRio:APA91bECQPcDIbC356b4JSWJ_tKdkQXl-ozpmG5F9MyX9jk-P10zMk0eTNWLhBDTI4z3W9rfUQN4aeDJQ5N_wEShw4t-dePJ7RmRQn0KJ1_TnBE_nPvFvnFpED95Zds5kYWgvFakiXpY";

    public static void pushNotification(Context context,String token,String title,String message, String senderEmail){
        StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        RequestQueue queue = Volley.newRequestQueue(context);

        try{
            JSONObject json=new JSONObject();
            json.put("to",token);

            JSONObject notification=new JSONObject();
            notification.put("title",title);

            // bildirim mesajını ve senderEmail'i birleştir
            String fullMessage = message + "\nFrom: " + senderEmail;
            notification.put("body",fullMessage);

            json.put("notification",notification);

            JSONObject data=new JSONObject();
            data.put("senderEmail", senderEmail);
            json.put("data",data);

             JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.POST,BASE_URL,json, new Response.Listener<JSONObject>() {
                 @Override
                 public void onResponse(JSONObject response) {

                 }
             }, new Response.ErrorListener(){

                 @Override
                 public void onErrorResponse(VolleyError error){

                 }
             }
             ){
                 @Override
                 public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String,String> params =new HashMap<>();
                    params.put("Content-Type","application/json");
                    params.put("Authorization",SERVER_KEY);
                    return params;
                 }
             };
             queue.add(jsonObjectRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
}
