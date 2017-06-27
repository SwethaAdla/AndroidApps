package com.example.sweth.hw07;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.wifi.p2p.WifiP2pManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<Episode> episodesList = new ArrayList<Episode>();
    String layoutType ="";
    public static final String LIST_VIEW_TYPE = "LIST_VIEW_TYPE";
    public static final String GRID_VIEW_TYPE = "GRID_VIEW_TYPE";
    public boolean isPlaying = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setLogo(R.drawable.app_logo);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        new GetEpisodesListAsyncTask(MainActivity.this).execute("http://www.npr.org/rss/podcast.php?id=510298");

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
        if (id == R.id.action_refresh) {
            if(layoutType.equals(LIST_VIEW_TYPE)){
                layoutType = GRID_VIEW_TYPE;
               setUpView(episodesList, layoutType);


            }else if(layoutType.equals(GRID_VIEW_TYPE)) {
                layoutType =LIST_VIEW_TYPE;
                setUpView(episodesList, layoutType);

            }
        }
        return super.onOptionsItemSelected(item);
    }

    public void showEpisodeslist(ArrayList<Episode> episodes) {
        layoutType = LIST_VIEW_TYPE;
        episodesList = episodes;
        Collections.sort(episodesList, new Comparator<Episode>() {
            @Override
            public int compare(Episode o1, Episode o2) {
                return o1.getPublishedDate().compareTo(o2.getPublishedDate());
            }
        });
        Collections.reverse(episodesList);
        Log.d("demo","episodes "+episodesList.size());
        setUpView(episodesList, layoutType);

    }

    public void setUpView(ArrayList<Episode> episodeArrayList, String viewType){
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        if(viewType.equals(LIST_VIEW_TYPE)) {
            mLayoutManager = new LinearLayoutManager(this);
            MyAdapter mAdapter = new MyAdapter(episodeArrayList, MainActivity.this);
            mRecyclerView.setAdapter(mAdapter);

        }else if(viewType.equals(GRID_VIEW_TYPE)) {
            mLayoutManager = new GridLayoutManager(this, 2);
            MyGridAdapter myGridAdapter = new MyGridAdapter(episodeArrayList, MainActivity.this);
            mRecyclerView.setAdapter(myGridAdapter);
        }
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

}
