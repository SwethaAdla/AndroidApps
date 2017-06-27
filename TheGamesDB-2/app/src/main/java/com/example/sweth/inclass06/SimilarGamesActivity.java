package com.example.sweth.inclass06;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class SimilarGamesActivity extends AppCompatActivity {

    ArrayList<Game> simGamesList = new ArrayList<Game>();
    public static final String GAME_ID_Key = "GAME_ID_Key";
    public static  final String SIM_FLAG_Key = "SIM_FLAG_Key";
    ArrayList<String> similarGames;
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_similar_games);
        listView = (ListView) findViewById(R.id.similarListView);
        if(getIntent().getExtras()!=null){
            String selectedGameName = getIntent().getExtras().getString("GAME_NAME_Key");
            similarGames = getIntent().getExtras().getStringArrayList("GAME_SIMILAR_Key");

            TextView name = (TextView) findViewById(R.id.selGame);
            name.setText(selectedGameName);

            //Log.d("demo","the selectedGameName in similar games activity is "+selectedGameName);

            for(int i=0;i<similarGames.size();i++) {
                // Log.d("demo","calling get similar game async task for game id "+similarGames.get(i));
                RequestParams params = new RequestParams("http://thegamesdb.net/api/GetGame.php", "GET");
                params.addParams("id", similarGames.get(i));
                new GetSimilarGameAsyncTask(SimilarGamesActivity.this).execute(params);
            }

            findViewById(R.id.finishBtn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });

        }
    }

    public void ShowSimilarGames(Game game) {

        simGamesList.add(game);
        Log.d("demo","size of ids passed is **********"+similarGames.size());
        Log.d("demo","size of the list after async task *****"+simGamesList.size());
        if(simGamesList.size()==similarGames.size()){
            Log.d("demo","inside the if condition. both lists sizez are equalxx");
            GameAdapter adapter = new GameAdapter(this,R.layout.row_layout,simGamesList);
            listView.setAdapter(adapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Game gameSelected=simGamesList.get(position);
                    String selectedGameId = gameSelected.getId();
                    Intent intent = new Intent(SimilarGamesActivity.this,GetGameActivity.class);
                    intent.putExtra(GAME_ID_Key,selectedGameId);
                    intent.putExtra(SIM_FLAG_Key,"isSimilar");
                    startActivity(intent);
                }
            });
        }

    }
}
