package com.app.inclass09;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    public static final String PREFS_NAME = "Token_pref";
    public static final String TOKEN_KEY = "token";
    public static final String EMAIL_KEY = "emailId";
    String token="";
    String userEmail ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        SharedPreferences sp = MainActivity.this.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        if(sp != null && sp.contains(TOKEN_KEY) && sp.contains(EMAIL_KEY) ){
            //chat screen
            Log.d("demo", sp.getString(TOKEN_KEY, ""));
            Log.d("demo", sp.getString(EMAIL_KEY, ""));
            Intent intent = new Intent(MainActivity.this, ChitChatActivity.class);
            intent.putExtra(TOKEN_KEY,sp.getString(TOKEN_KEY, ""));
            intent.putExtra(EMAIL_KEY,sp.getString(EMAIL_KEY, "") );
            startActivity(intent);

        }else{
            //load login/signup screen
            findViewById(R.id.buttonLogin).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EditText et_email = (EditText) findViewById(R.id.editTextEmailValue);
                    userEmail= String.valueOf(et_email.getText());
                    EditText et_password = (EditText) findViewById(R.id.editTextPasswordValue);
                    String password = String.valueOf(et_password.getText());
                    new DoAsyncLogin(MainActivity.this).execute(userEmail,password);
                }
            });

            findViewById(R.id.buttonSignUp).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EditText et_fname = (EditText) findViewById(R.id.editTextFirstNameValue);
                    String fname= String.valueOf(et_fname.getText());
                    EditText et_lname = (EditText) findViewById(R.id.editTextLastNameValue);
                    String lname = String.valueOf(et_lname.getText());
                    EditText et_email = (EditText) findViewById(R.id.editTextEmailValue1);
                     userEmail= String.valueOf(et_email.getText());
                    EditText et_password = (EditText) findViewById(R.id.editTextPassword1Value);
                    String password = String.valueOf(et_password.getText());

                    if(fname != null && lname != null && userEmail!= null && password != null) {
                        //new DoAsyncSignUp(MainActivity.this).execute(email, password, fname,lname);
                        OkHttpClient client = new OkHttpClient();
                        RequestBody formBody = new FormBody.Builder()
                                .add("email", userEmail)
                                .add("password", password)
                                .add("fname", fname)
                                .add("lname", lname)
                                .build();
                        Request request = new Request.Builder()
                                .url("http://52.90.79.130:8080/Groups/api/signUp")
                                .post(formBody)
                                .build();


                        Response response = null;
                        try {
                            response = client.newCall(request).execute();
                            if (!response.isSuccessful())
                                throw new IOException("Unexpected code" + response);

                            String data = response.body().string();
                            JSONObject root = new JSONObject(data);
                            String status = root.getString("status");
                            if (status.equals("1")) {
                                token = root.getString("data");
                                Log.d("demo", "new " +token);
                                setUpAfterSignup(status, token);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else{
                        Toast.makeText(MainActivity.this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
                    }


                }
            });
        }

    }

    public void setUpData(String status, String token){
        Log.d("demo", status);
        Log.d("demo", token);

        if(status.equals("1")){

            SharedPreferences sp = this.getSharedPreferences(MainActivity.PREFS_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor sp_editor = sp.edit();
            sp_editor.putString(MainActivity.TOKEN_KEY,token);
            sp_editor.putString(MainActivity.EMAIL_KEY,userEmail);
            sp_editor.commit();



            Intent i = new Intent(MainActivity.this, ChitChatActivity.class);
            i.putExtra("token", token );
            i.putExtra(EMAIL_KEY, userEmail);
           // i.putExtra("user_name",fname +lname);
            startActivity(i);
        }
        else{
            Toast.makeText(MainActivity.this, "Wrong Credentials", Toast.LENGTH_SHORT).show();
        }


    }

    public void setUpAfterSignup(String status, String token){
        Log.d("demo", status);
        Log.d("demo", token);
        if(status.equals("1")){
            SharedPreferences sp = this.getSharedPreferences(MainActivity.PREFS_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor sp_editor = sp.edit();
            sp_editor.putString(MainActivity.TOKEN_KEY,token);
            sp_editor.commit();

            Intent i3 = new Intent(MainActivity.this, ChitChatActivity.class);
            i3.putExtra("token", token );
            i3.putExtra(EMAIL_KEY, userEmail);
            startActivity(i3);
        }
        else{
            Toast.makeText(MainActivity.this, "User already exists", Toast.LENGTH_SHORT).show();
        }

    }

}
