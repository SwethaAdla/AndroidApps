package com.app.inclass09;

import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class DoAsyncSignUp extends AsyncTask<String, Void, String> {
    OkHttpClient client;
    String token="";
    String status="";

    public DoAsyncSignUp(MainActivity activity) {
        this.activity = activity;
    }

    MainActivity activity;

    @Override
    protected String doInBackground(String... params) {
        client = new OkHttpClient();
        RequestBody formBody = new FormBody.Builder()
                .add("email", params[0])
                .add("password", params[1])
                .add("fname", params[2])
                .add("lname", params[3])
                .build();
        Request request = new Request.Builder()
                .url("http://52.90.79.130:8080/Groups/api/signUp")
                .post(formBody)
                .build();


        Response response = null;
        try {
            response = client.newCall(request).execute();
            if (!response.isSuccessful()) throw new IOException("Unexpected code" + response);

            String data = response.body().string();
            JSONObject root = new JSONObject(data);
            String status = root.getString("status");


            if(status.equals("1")){
                token = root.getString("data");
            }
            return status;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "error";
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        activity.setUpAfterSignup(s, token);
    }

}
