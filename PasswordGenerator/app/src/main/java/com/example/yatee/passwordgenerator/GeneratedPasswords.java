/*
Assignment # - InClass03
FileName - GeneratePasswords.java
Group Members - Yateen Kedare | Swetha Adla
 */

package com.example.yatee.passwordgenerator;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;

public class GeneratedPasswords extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generated_passwords);
        Bundle extras = getIntent().getExtras();
        ArrayList<String> ThreadPasswords = extras.getStringArrayList("ThreadPasswords");
        ArrayList<String> AsyncPasswords = extras.getStringArrayList("AsyncPasswords");

        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        LinearLayout sv1 = (LinearLayout) findViewById(R.id.LL1);
        for(String s: ThreadPasswords)
        {
            TextView tv1 = new TextView(this);
            tv1.setText(s);
            tv1.setWidth(LinearLayoutCompat.LayoutParams.WRAP_CONTENT);
            tv1.setTextSize(20);
            tv1.setGravity(Gravity.CENTER);
            tv1.setPadding(25,25,0,0);
            sv1.addView(tv1);

        }



        LinearLayout sv = (LinearLayout) findViewById(R.id.LL2);
        for(String v: AsyncPasswords)
        {
            TextView tv1 = new TextView(this);
          tv1.setText(v);
            tv1.setWidth(LinearLayoutCompat.LayoutParams.WRAP_CONTENT);
            tv1.setTextSize(20);
            tv1.setGravity(Gravity.CENTER);
            tv1.setPadding(25,25,0,0);
            sv.addView(tv1);

        }
    }
}
