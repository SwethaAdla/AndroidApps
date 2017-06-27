package com.app.thegamesdb;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;

import java.util.ArrayList;

/* Homework 05
   MainActivity.java
   Group 02- Lakshmi Sridhar, Swetha Adla */

public class MainActivity extends AppCompatActivity {
public static final String GAME_ID_Key = "GAME_ID_Key";
    String searchString ="";
    String selectedGameId ="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.buttonSearch).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText searchtextView = (EditText) findViewById(R.id.editTextSearch);
                searchString = String.valueOf(searchtextView.getText());

                RequestParams params = new RequestParams("http://thegamesdb.net/api/GetGamesList.php","GET");
                params.addParams("name", searchString);
                new GetGamesListAsyncTask(MainActivity.this).execute(params);


            }
        });
    }

    public void showGameslist(final ArrayList<Game> gameArrayList){
        final Button goBtn = (Button) findViewById(R.id.buttonGo);
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linearLayout);
        linearLayout.removeAllViews();
       final RadioGroup radioGroup = new RadioGroup(MainActivity.this);
        int rbNo = gameArrayList.size();
        for(int i=0; i<rbNo; i++){
            Game game = gameArrayList.get(i);
            RadioButton rb = new RadioButton(MainActivity.this);
            rb.setId(i+101);
            String newDate;
            if(game.getReleaseDate().length()<6 && game.getReleaseDate()!=null){
                newDate = game.getReleaseDate();
            }else{
                newDate = formatYear(game.getReleaseDate());
            }
            rb.setText(game.getGameTitle()+". Released in "+newDate+". Platform: "+game.getPlatform()+".");
            radioGroup.addView(rb);
        }
        linearLayout.addView(radioGroup);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                goBtn.setEnabled(true);
                selectedGameId = gameArrayList.get((checkedId-101)).getId();
                Log.d("demo", selectedGameId);
            }
        });
        findViewById(R.id.buttonGo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, GetGameActivity.class);
                intent.putExtra(GAME_ID_Key, selectedGameId);
                startActivity(intent);
            }
        });
    }

    public static String formatYear(String s){
        if(s != null && s.length() != 0){
            return s.substring(6);
        }else if(s == null ){
            return "";
        }else{
            return "";
        }
    }
}
