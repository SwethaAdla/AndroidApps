package com.example.libraryguest2.myapplication;
/* Homework 03
    Words.java
    Group 02- Lakshmi Sridhar, Swetha Adla
*/
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

public class Words extends AppCompatActivity {
    Map<String, Integer> resultMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_words);
       // resultMap = new HashMap<String, Integer>();
        if(getIntent().getExtras()!= null){
            resultMap = (Map<String, Integer>) getIntent().getExtras().get(MainActivity.RESULT_MAP_KEY);

            if(resultMap != null){

                LinearLayout resultLayout = (LinearLayout) findViewById(R.id.resultLayout);
                for (String s:resultMap.keySet()) {
                        TextView tv = new TextView(this);
                        String res = s.concat(" : " + resultMap.get(s).toString());
                        tv.setText(res);
                        tv.setTextColor(Color.BLACK);
                        Log.d("demo", "the  result map, in words class, length " + resultMap.size());
                        resultLayout.addView(tv);
                }
            }else if(resultMap == null){
                Log.d("Words", "result map is null");
            }
        }

        findViewById(R.id.buttonFinish).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
