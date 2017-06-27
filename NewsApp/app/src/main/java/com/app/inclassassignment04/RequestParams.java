package com.app.inclassassignment04;

import android.util.Log;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;

import javax.net.ssl.HttpsURLConnection;



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

    public HttpsURLConnection setUpConnection() throws IOException {
        if (method.equals("GET")) {

            URL url = new URL(getEncodedUrl());
            HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
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
