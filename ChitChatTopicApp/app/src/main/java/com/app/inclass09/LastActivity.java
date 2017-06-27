package com.app.inclass09;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ocpsoft.prettytime.PrettyTime;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LastActivity extends AppCompatActivity {
String token ="";
    App app=null;
    ListView listView;
    String userEmail="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_last);
        listView = (ListView) findViewById(R.id.MyListView2);
        if(getIntent().getExtras() != null){
            token = (String) getIntent().getExtras().get(MainActivity.TOKEN_KEY);
            userEmail = getIntent().getExtras().getString(MainActivity.EMAIL_KEY);
            app = (App) getIntent().getExtras().get("APP_KEY");

            setUp();
                TextView tv = (TextView) findViewById(R.id.textViewChannelName);
                tv.setText(app.getName());

                findViewById(R.id.buttonSend).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EditText et = (EditText) findViewById(R.id.editTextTexttoSend);
                        String msg2Send = et.getText().toString();
                        PrettyTime p = new PrettyTime();
                        System.out.println(p.format(new Date()));
                        OkHttpClient client = new OkHttpClient();
                        RequestBody formBody = new FormBody.Builder()
                                .add("msg_text", msg2Send)
                                .add("msg_time", String.valueOf(new Date()))
                                .add("channel_id", app.getId())
                                .build();
                        Request request = new Request.Builder()
                                .url("http://52.90.79.130:8080/Groups/api/post/message")
                                .header("Authorization","BEARER "+token )
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
                                String data1 = root.getString("data");
                            }

                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main2, menu);
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
            Intent intent = new Intent(LastActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }else if(id == R.id.action_refresh) {
            setUp();
        }

        return super.onOptionsItemSelected(item);
    }

    public void setUp (){
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("http://52.90.79.130:8080/Groups/api/get/messages?channel_id="+app.getId())
                .header("Authorization","BEARER "+token )
                .build();
        Response response = null;
        try {
            response = client.newCall(request).execute();

            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            JSONObject root= new JSONObject(response.body().string());
            JSONArray jsonArray = root.getJSONArray("data");
            ArrayList<Msg> msgArrayList = new ArrayList<Msg>();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObj = jsonArray.getJSONObject(i);
                Msg msg = new Msg();
                msg.setMsg_id(jsonObj.getString("message_id"));
                msg.setChannel_id(jsonObj.getString("channel_id"));
                msg.setTime(jsonObj.getString("msg_time"));
                msg.setMsg(jsonObj.getString("messages_text"));
                JSONObject userObj = jsonObj.getJSONObject("user");
                msg.setFname(userObj.getString("fname"));
                msg.setLname(userObj.getString("lname"));
                msg.setEmailid(userObj.getString("email"));
                msgArrayList.add(msg);
            }

            MyAdapter adapter = new MyAdapter(this, R.layout.msg_view,msgArrayList , LastActivity.this);
            adapter.setNotifyOnChange(true);
            listView.setAdapter(adapter);
        } catch (IOException e) {
            e.printStackTrace();
        }  catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
