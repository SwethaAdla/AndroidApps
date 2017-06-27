package com.example.sweth.inclass06;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    public static final String GAME_ID_Key = "GAME_ID_Key";
    String searchString ="";
    String selectedGameId ="";
    ArrayList<Game> gamesList = new ArrayList<Game>();

    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView) findViewById(R.id.listView1);
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

    public void getGameslist(ArrayList<Game> games) {
        for(int i=0;i<10;i++){
            gamesList.add(games.get(i));

        }
        Log.d("demo","in getgames list method"+gamesList.size());
        GameAdapter adapter = new GameAdapter(this,R.layout.row_layout,gamesList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Game gameSelected=gamesList.get(position);
                String selectedGameId = gameSelected.getId();
                Intent intent = new Intent(MainActivity.this,GetGameActivity.class);
                intent.putExtra(GAME_ID_Key,selectedGameId);
                startActivity(intent);
            }
        });

    }



}
