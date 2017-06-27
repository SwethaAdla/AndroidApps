package com.example.libraryguest2.myapplication;

import android.util.Log;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;

import javax.net.ssl.HttpsURLConnection;

/* Homework 04
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

    public HttpURLConnection setUpConnection() throws IOException {

        if (method.equals("GET")) {
            URL url = new URL(baseURL);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            return con;

        } else  {
            URL url = new URL(this.baseURL);
            HttpURLConnection con = null;
            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setDoOutput(true);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(con.getOutputStream());
            //outputStreamWriter.write(getEncodedParams());
            outputStreamWriter.flush();
            return con;
        }
    }
}
