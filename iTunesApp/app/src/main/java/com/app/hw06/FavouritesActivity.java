package com.app.hw06;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class FavouritesActivity extends AppCompatActivity {
    ListView lv;
    ArrayList<App> appList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites);
        if (getIntent().getExtras() !=null) {
            appList = (ArrayList<App>) getIntent().getExtras().get(MainActivity.MY_FAVOURITES);
            loadFavList(appList);

            /*SharedPreferences appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());
            Gson gson = new Gson();
            String json = appSharedPrefs.getString(MainActivity.MY_FAVOURITES, "");
            if (!json.isEmpty()) {
                Type type = new TypeToken<List<App>>() {}.getType();
                appList = gson.fromJson(json, type);
                loadFavList(appList); */

        }else{
            Toast.makeText(this, "No Favourites to display",Toast.LENGTH_SHORT).show();
        }


    }

    private void updateSharedPreference(ArrayList<App> appL){
        SharedPreferences appSharedPrefs = getSharedPreferences(MainActivity.MY_FAVOURITES_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(appL);
        prefsEditor.putString(MainActivity.MY_FAVOURITES, json);
        //prefsEditor.commit();
        prefsEditor.apply();
    }

    public void alertDailogBuilder(final App app) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add to Favourites")
                .setMessage("Are you sure that you want to remove this App from favourites")
                .setPositiveButton(MainActivity.YES, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        int index = -2;
                        // continue with delete
                        app.setFavourite(false);
                        for(App a: appList){
                            if(a.getAppName().equals(app.getAppName())
                                    &&(a.getAppPrice().equals(app.getAppPrice()))
                                    &&(a.getImageUrl().equals(app.getImageUrl()))){
                                index = appList.indexOf(a);
                            }
                        }
                        appList.remove(index);
                        Log.d("demo", String.valueOf(appList.size()));
                        //appList.remove(app);
                        updateSharedPreference(appList);
                        loadFavList(appList);


                    }
                })
                .setNegativeButton(MainActivity.NO, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .show();
    }

    private void loadFavList(ArrayList<App> appArrayList){
        lv = (ListView) findViewById(R.id.listViewFav);
        AppAdapter appAdapter = new AppAdapter(this,R.layout.row_view,appArrayList);
        appAdapter.setNotifyOnChange(true);
        lv.setAdapter(appAdapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                App appSelctd = appList.get(position);
                alertDailogBuilder(appSelctd);

            }
        });


    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, MainActivity.class));
    }
}
