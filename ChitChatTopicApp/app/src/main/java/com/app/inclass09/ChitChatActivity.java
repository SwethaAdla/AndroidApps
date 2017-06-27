package com.app.inclass09;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ChitChatActivity extends AppCompatActivity {
    String token = "";
    String userEmail ="";
ListView listView;

    public ArrayList<App> getSubscripList() {
        return subscripList;
    }

    public void setSubscripList(ArrayList<App> subscripList) {
        this.subscripList = subscripList;
    }

    ArrayList<App> subscripList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chit_chat);

        listView = (ListView) findViewById(R.id.MyListView);
        if (getIntent().getExtras() != null) {
            token=getIntent().getStringExtra("token");
            userEmail = getIntent().getStringExtra(MainActivity.EMAIL_KEY);
            Log.d("demo", "Chitchatactivitity token "+token);
           OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url("http://52.90.79.130:8080/Groups/api/get/subscriptions")
                    .header("Authorization","BEARER "+token )
                    .build();
            Response response = null;
            try {
                response = client.newCall(request).execute();

                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

               JSONObject root= new JSONObject(response.body().string());

                JSONArray jsonArray = root.getJSONArray("data");
                //ArrayList<App> datalist = new ArrayList<App>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObj = jsonArray.getJSONObject(i);
                    JSONObject chanlObj = jsonObj.getJSONObject("channel");
                    App app = new App();
                    app.setId(chanlObj.getString("channel_id"));
                    app.setName(chanlObj.getString("channel_name"));
                    app.setSubscribed(true);
                    subscripList.add(app);
                    //datalist.add(app);
                }
                AppAdapter adapter = new AppAdapter(this, R.layout.row_view, subscripList, ChitChatActivity.this);
                adapter.setNotifyOnChange(true);
                listView.setAdapter(adapter);
            } catch (IOException e) {
                e.printStackTrace();
            }  catch (JSONException e) {
                e.printStackTrace();
            }
            findViewById(R.id.buttonAddMore).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    OkHttpClient client = new OkHttpClient();

                    Request request = new Request.Builder()
                            .url("http://52.90.79.130:8080/Groups/api/get/channels")
                            .header("Authorization", "BEARER "+token)
                            .build();
                    Response response = null;
                    try {
                        response = client.newCall(request).execute();

                        if (!response.isSuccessful())
                            throw new IOException("Unexpected code " + response);

                        JSONObject root = new JSONObject(response.body().string());
                        JSONArray jsonArray = root.getJSONArray("data");
                        ArrayList<App> datalist = new ArrayList<App>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject msg = jsonArray.getJSONObject(i);

                            App app = new App();
                            app.setName(msg.getString("channel_name"));
                            app.setId(msg.getString("channel_id"));

                            datalist.add(app);
                        }
                        for (App app : datalist) {
                            for (App subApp : subscripList) {
                                if (app.getName().equals(subApp.getName())
                                        && (app.getId().equalsIgnoreCase(subApp.getId()))
                                        &&(app.isSubscribed != subApp.isSubscribed)) {
                                    app.setSubscribed(true);

                                }
                            }}
                        AppAdapter adapter = new AppAdapter(ChitChatActivity.this, R.layout.row_view, datalist, ChitChatActivity.this);
                        adapter.setNotifyOnChange(true);
                        listView.setAdapter(adapter);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                });



        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_logout) {
            SharedPreferences mySPrefs =this.getSharedPreferences(MainActivity.PREFS_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = mySPrefs.edit();
            editor.remove(MainActivity.TOKEN_KEY);
            editor.remove(MainActivity.EMAIL_KEY);
            editor.apply();
            Toast.makeText(this, "Session cleared and ended", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(ChitChatActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}