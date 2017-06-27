package com.app.hw06;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

import javax.net.ssl.HttpsURLConnection;


public class RequestParams {
    String baseURL, method;
    HashMap<String, String> params = new HashMap<String, String>();

    public RequestParams(String baseURL, String method) {
        this.baseURL = baseURL;
        this.method = method;
    }

    public HttpsURLConnection setUpConnection() throws IOException {

        if (method.equals("GET")) {
            URL url = new URL(baseURL);
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
            //outputStreamWriter.write(getEncodedParams());
            outputStreamWriter.flush();
            return con;
        }
    }
}
