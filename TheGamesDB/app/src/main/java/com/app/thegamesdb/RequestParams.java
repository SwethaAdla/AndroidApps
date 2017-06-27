package com.app.thegamesdb;
/* Homework 05
   RequestParams.java
   Group 02- Lakshmi Sridhar, Swetha Adla */

import android.util.Log;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;

import javax.net.ssl.HttpsURLConnection;

/* Homework 05
   RequestParams.java
   Group 02- Lakshmi Sridhar, Swetha Adla
   */

public class RequestParams {

    String baseURL, method;
    HashMap<String, String> params = new HashMap<String, String>();

    public RequestParams(String baseURL, String method) {
        this.baseURL = baseURL;
        this.method = method;
    }

    public void addParams(String key, String value){
        params.put(key, value);

    }

    public String getEncodedParams(){
        StringBuilder sb = new StringBuilder();
        for(String key:params.keySet()){
            try {
                String value =  URLEncoder.encode(params.get(key), "UTF-8");
                if(sb.length() > 0){
                    sb.append("&");
                }
                sb.append(key+"="+value);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        return sb.toString();
    }

    public String getEncodedUrl(){
        Log.d("Encoded URL",(baseURL + "?" + getEncodedParams()));
        return baseURL + "?" + getEncodedParams();
    }

    public HttpURLConnection setUpConnection() throws IOException {
        if (method.equals("GET")) {

            URL url = new URL(getEncodedUrl());
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            return con;

        } else  {
            URL url = new URL(this.baseURL);
            HttpsURLConnection con = null;
            con = (HttpsURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setDoOutput(true);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(con.getOutputStream());
            outputStreamWriter.write(getEncodedParams());
            outputStreamWriter.flush();
            return con;
        }
    }
}
