package com.app.thegamesdb;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by sweth on 2/17/2017.
 */

public class SimilargamesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_similar_games);

        if(getIntent().getExtras()!=null){
            String selectedGameName = getIntent().getExtras().getString("GAME_NAME_Key");
            ArrayList<String> similarGames = getIntent().getExtras().getStringArrayList("GAME_SIMILAR_Key");

            TextView name = (TextView) findViewById(R.id.selGame);
            name.setText(selectedGameName);

            //Log.d("demo","the selectedGameName in similar games activity is "+selectedGameName);

            for(int i=1;i<similarGames.size();i++) {
               // Log.d("demo","calling get similar game async task for game id "+similarGames.get(i));
                RequestParams params = new RequestParams("http://thegamesdb.net/api/GetGame.php", "GET");
                params.addParams("id", similarGames.get(i));
                new GetSimilarGameAsyncTask(SimilargamesActivity.this).execute(params);
            }

            findViewById(R.id.finishBtn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });

        }

    }
    public void ShowSimilarGames(Game game){
        LinearLayout ll = (LinearLayout) findViewById(R.id.SimLinearLayout);
        TextView tv = new TextView(SimilargamesActivity.this);
        tv.setTextColor(getResources().getColor(R.color.black));
        if(game!=null) {
            tv.setText(game.getGameTitle() + ". Released in " + MainActivity.formatYear(game.getReleaseDate())+ ". Platform: " + game.getPlatform());
            //Log.d("demo", "similar game from text view is " + tv.getText());
        }else{
            tv.setText("No similar games");
        }
        ll.addView(tv);
    }
}
