package com.app.hw06;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class MainActivity extends AppCompatActivity
       implements GetAppsAsyncTask.IApp {
    ArrayList<App> appArrayList = null;
    ArrayList<Double> priceList = null;
    ArrayList<App> favList =null;
    public  static final String MY_FAVOURITES = "MY_FAVOURITES";
    public  static final String MY_FAVOURITES_PREFS = "MY_FAVOURITES_PREFS";
    public static final String YES ="Yes";
    public static final String NO ="No";
    int pos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RequestParams requestParams = new RequestParams("https://itunes.apple.com/us/rss/toppaidapplications/limit=25/json", "GET");
        new GetAppsAsyncTask(this).execute(requestParams);


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

        if (id == R.id.nav_refreshList) {
            compareLists(appArrayList);
            loadApps(appArrayList);

        } else if (id == R.id.nav_favourites) {
            Intent intent = new Intent(MainActivity.this, FavouritesActivity.class);
            Log.d("demo", String.valueOf(favList.size()));
            intent.putExtra(MY_FAVOURITES, (Serializable)favList);
            startActivity(intent);

        } else if (id == R.id.nav_sortIncreasingly) {
            sortApps(appArrayList, 0);

        } else if (id == R.id.nav_sortDecreasingly) {
            sortApps(appArrayList, 1);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void loadApps(ArrayList<App> apps) {
        appArrayList = apps;
        ListView listView = (ListView) findViewById(R.id.listView);
        AppAdapter appAdapter = new AppAdapter(this, R.layout.row_view, apps);
        listView.setAdapter(appAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                pos =position;
                App appSelctd = appArrayList.get(position);
                alertDailogBuilder(appSelctd);

            }
        });

    }

    public void sortApps(ArrayList<App> appsList, int sortVal){
        Map<App, Double> priceMap = new HashMap<App, Double>();
        ArrayList<App> sortedList = new ArrayList<App>();
        for (App app : appsList) {
            double price = Double.valueOf(app.getAppPrice().substring(1));
            priceMap.put(app, price);
        }
        if(sortVal == 0){
            Set<Map.Entry<App, Double>> set = priceMap.entrySet();
            List<Map.Entry<App, Double>> list = new ArrayList<Map.Entry<App, Double>>(set);
            Collections.sort( list, new Comparator<Map.Entry<App, Double>>(){
                public int compare( Map.Entry<App, Double> o1, Map.Entry<App, Double> o2){
                    return (o2.getValue()).compareTo( o1.getValue() );
                }
            } );
            Collections.reverse(list);
            for(Map.Entry<App, Double> entry:list){
                sortedList.add(entry.getKey());
            }

        }else if(sortVal == 1){
            Set<Map.Entry<App, Double>> set = priceMap.entrySet();
            List<Map.Entry<App, Double>> list = new ArrayList<Map.Entry<App, Double>>(set);
            Collections.sort( list, new Comparator<Map.Entry<App, Double>>(){
                public int compare( Map.Entry<App, Double> o1, Map.Entry<App, Double> o2){
                    return (o2.getValue()).compareTo( o1.getValue() );
                }
            } );
            for(Map.Entry<App, Double> entry:list){
                sortedList.add(entry.getKey());
            }
        }
        loadApps(sortedList);
    }

    private void getSharedPreference(){
        SharedPreferences appSharedPrefs = getSharedPreferences(MY_FAVOURITES_PREFS, Context.MODE_PRIVATE);

        Gson gson = new Gson();
        String json = appSharedPrefs.getString(MY_FAVOURITES, "");
        if (!json.isEmpty()) {
            Type type = new TypeToken<List<App>>() {}.getType();
            favList = gson.fromJson(json, type);

        }else{
            Log.d("demo","Shared preference is empty");
        }
    }
    private void updateSharedPreference(ArrayList<App> appL){
        SharedPreferences appSharedPrefs = getSharedPreferences(MY_FAVOURITES_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(appL);
        prefsEditor.putString(MainActivity.MY_FAVOURITES, json);
        prefsEditor.commit();
        //prefsEditor.apply();
    }

    public void alertDailogBuilder(final App app) {
        getSharedPreference();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add to Favourites");
        if(app.isFavourite()){
            builder .setMessage("Are you sure that you want to remove this App from favourites")
                    .setPositiveButton(YES, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            int index = -1;
                            app.setFavourite(false);
                            appArrayList.set(pos, app);
                            for(App a: favList){
                                if(a.getAppName().equals(app.getAppName())
                                        &&(a.getAppPrice().equals(app.getAppPrice()))
                                        &&(a.getImageUrl().equals(app.getImageUrl()))){
                                    index = favList.indexOf(a);
                                }
                            }
                            favList.remove(index);
                            Log.d("demo", String.valueOf(favList.size()));
                            updateSharedPreference(favList);
                            loadApps(appArrayList);

                        }
                    })
                    .setNegativeButton(NO, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // do nothing
                        }
                    })
                    .show();
        }else {
            builder .setMessage("Are you sure that you want to add this App to favourites")
                    .setPositiveButton(YES, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            app.setFavourite(true);
                            if(favList != null){
                                favList.add(app);

                            }else if(favList == null){
                                favList = new ArrayList<App>();
                                favList.add(app);
                            }
                            updateSharedPreference(favList);
                            loadApps(appArrayList);

                        }
                    })
                    .setNegativeButton(NO, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // do nothing
                        }
                    })
                    .show();
        }

    }
    public void compareLists(ArrayList<App> appArrylist){
        getSharedPreference();
        if(favList != null){
            for (App app : appArrylist) {
                for (App favApp : favList) {
                    if (app.getAppName().equals(favApp.getAppName())
                            && (app.getAppPrice().equalsIgnoreCase(favApp.getAppPrice()))
                            && (app.getImageUrl().equalsIgnoreCase(favApp.getImageUrl()))
                            &&(app.isFavourite() != favApp.isFavourite())) {
                        app.setFavourite(true);

                    }

                }}}else {
            return;
        }
    }

}
