package com.app.thegamesdb;
/* Homework 05
   GetGameActivity.java
   Group 02- Lakshmi Sridhar, Swetha Adla */

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class GetGameActivity extends AppCompatActivity {
    public static final String GAME_NAME_Key = "GAME_NAME_Key";
    public static final String GAME_SIMILAR_Key = "GAME_SIMILAR_Key";
    public static  final String TRAILER_LINK_KEY = "TRAILER_LINK_KEY";
    String selectedGameName ="";
    ArrayList<Game> similarGames = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_game);

        if(getIntent().getExtras()!=null){
            String selectedGameId = getIntent().getExtras().getString("GAME_ID_Key");
            //Log.d("demo","the radio button id selected is "+selectedGameId);
            RequestParams params = new RequestParams("http://thegamesdb.net/api/GetGame.php","GET");
            params.addParams("id", selectedGameId);
            new GetGameAsyncTask(GetGameActivity.this).execute(params);

        }
    }

    public void showGame(final Game game){
        LinearLayout ll = (LinearLayout) findViewById(R.id.OVLinearLayout);
        TextView gameName = (TextView) findViewById(R.id.GameName);
        gameName.setText(game.getGameTitle().toString());
        selectedGameName = game.getGameTitle().toString();
        if(game.getBaseImgUrl()!=null){
            new GetImageAsyncTask(GetGameActivity.this).execute(game.getBaseImgUrl());
        }

        Log.d("demo","the youtube link is "+game.getTrailerLink());

        TextView overview = (TextView) findViewById(R.id.OverviewTextView);
        overview.setTextColor(getResources().getColor(R.color.black));
        if(game.getOverview()!=null) {
            overview.setText(game.getOverview().toString());
        }else{
            overview.setText("No overview exists for this game.");
        }
        TextView genre = (TextView) findViewById(R.id.genreValue);
        if(game.getGenre()!=null) {
            genre.setText(game.getGenre());
        }else{
            genre.setText("No genre found");
        }
        TextView publisher = (TextView) findViewById(R.id.PublisherValue);
        if(game.getPublisher()!=null) {
            publisher.setText(" " + game.getPublisher());
        }else{
            publisher.setText("No Plublisher found");
        }

        if(game.getSimilarGames()!=null||game.getSimilarGames().size()!=0){
            similarGames = game.getSimilarGames();
        }

        findViewById(R.id.FinishBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                /*Intent intent = new Intent(GetGameActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);*/
            }
        });

        findViewById(R.id.PlayTrailerBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(game.getTrailerLink() == null || game.getTrailerLink().equals("")){
                    Toast.makeText(getApplicationContext(), "Trailer not available", Toast.LENGTH_SHORT).show();
                }else {
                    Intent intent = new Intent(GetGameActivity.this, WebViewActivity.class);
                    intent.putExtra(TRAILER_LINK_KEY, game.getTrailerLink());
                    intent.putExtra(GAME_NAME_Key, selectedGameName);
                    startActivity(intent);
                }
            }
        });

        findViewById(R.id.SimilarGamesBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> simIds = new ArrayList<String>();
                for(Game g:similarGames){
                    simIds.add(g.getId());
                }
                simIds.remove(0);
                Log.d("demo","size of similar movies array after removing first element: "+simIds.size());
                if(simIds != null && !simIds.isEmpty()) {
                    Intent intent = new Intent(GetGameActivity.this, SimilargamesActivity.class);
                    intent.putExtra(GAME_NAME_Key, selectedGameName);
                    intent.putExtra(GAME_SIMILAR_Key, simIds);
                    startActivity(intent);
                }else{
                    Toast.makeText(getApplicationContext(), "Similar games not available", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
