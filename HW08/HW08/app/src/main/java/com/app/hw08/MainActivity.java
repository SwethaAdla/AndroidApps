package com.app.hw08;

/* Group 02 - Lakshmi Sridhar, Swetha Adla
   Homework 08
   MainActivity.java
 */


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.StrictMode;
import android.preference.ListPreference;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.app.hw08.R.id.activity_main;

public class MainActivity extends AppCompatActivity implements BlankFragment.OnFragmentInteractionListener {
    OkHttpClient client;
    SharedPreferences sp;
    public static final String PREFS_NAME = "CURRENT_CITY_PREF";
    public static final String CURRENT_CITY_KEY = "CURRENT_CITY";
    public CurrentConditions currentCityConditions;
    public CityInfo cityInfo;
    public static ArrayList<CityInfo> savedCities = new ArrayList<CityInfo>();
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref = database.getReference("Cities");
    RecyclerView recyclerView;
    ListPreference mListPref;

    public ArrayList<CityInfo> getSavedCities() {
        return savedCities;
    }

    public void setSavedCities(ArrayList<CityInfo> savedCities) {
        this.savedCities = savedCities;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setLogo(R.drawable.logo_ldpi);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);


        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        recyclerView = (RecyclerView) findViewById(R.id.MyRecyclerView);

        sp = MainActivity.this.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        if(sp != null && sp.contains(CURRENT_CITY_KEY)){
            Gson gson = new Gson();
            String json = sp.getString(CURRENT_CITY_KEY, "");
            cityInfo = gson.fromJson(json, CityInfo.class);
            Log.d("demo", cityInfo.toString());
            //get current condition and display
            displayCurrntCondtns();

        }else {
            //set display accordingly

        }

        //setting the recycler view
        FirebaseDatabase.getInstance().getReference("Cities").addValueEventListener(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
                savedCities.clear();
                for (com.google.firebase.database.DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    Log.d("demo", snapshot.getValue(CityInfo.class).toString());
                    savedCities.add(snapshot.getValue(CityInfo.class));
                }
                if(savedCities.isEmpty()){
                    recyclerView.setVisibility(View.INVISIBLE);

                }
                else {
                    Log.d("demo","Saved cities list size: "+savedCities.size());
                    Collections.sort(savedCities, new Comparator<CityInfo>() {
                        @Override
                        public int compare(CityInfo o1, CityInfo o2) {
                            return Boolean.compare(o1.isFavourite, o2.isFavourite);
                        }
                    });
                    Collections.reverse(savedCities);
                    loadSavedCities(savedCities);
                    recyclerView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        findViewById(R.id.buttonSetCurrCity).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertDialog();
            }
        });

        findViewById(R.id.buttonSearchCity).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText etCity = (EditText) findViewById(R.id.editTextSearchCityName);
                String city = etCity.getText().toString();
                EditText etCountry = (EditText) findViewById(R.id.editTextSearchCountryName);
                String cntry = etCountry.getText().toString();
                if(city == null || city.equals("") || cntry == null || cntry.equals("") ){
                    Toast.makeText(getApplicationContext(), "Enter City and Country to search", Toast.LENGTH_SHORT).show();
                }else {
                    CityInfo cityInfo1 = doLocationApiCall(cntry,city);
                    if(cityInfo1 != null) {
                        Intent intent = new Intent(MainActivity.this, FiveDayForecastActivity.class);
                        intent.putExtra("CITY_INFO_KEY", cityInfo1);
                        startActivity(intent);
                    }else {
                        Toast.makeText(MainActivity.this, "City not found.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        }

    public void showAlertDialog(){
        LayoutInflater factory = LayoutInflater.from(this);
        final View textEntryView = factory.inflate(R.layout.alert_textfields_layout, null);

        final EditText input1 = (EditText) textEntryView.findViewById(R.id.editTextCity);
        final EditText input2 = (EditText) textEntryView.findViewById(R.id.editTextCountry);

        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Enter City Details").setView(textEntryView)
                .setPositiveButton("Set", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        Log.d("AlertDialog","TextEntry 1 Entered "+input1.getText().toString());
                        String city = input1.getText().toString();
                        Log.d("AlertDialog","TextEntry 2 Entered "+input2.getText().toString());
                        String country = input2.getText().toString();
                        cityInfo = doLocationApiCall(country, city);
                        if(cityInfo != null) {
                            //save in shared pref
                            sp = MainActivity.this.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
                            SharedPreferences.Editor prefsEditor = sp.edit();
                            Gson gson = new Gson();
                            String json = gson.toJson(cityInfo); // myObject - instance of MyObject
                            prefsEditor.putString(CURRENT_CITY_KEY, json);
                            prefsEditor.commit();
                            Toast.makeText(MainActivity.this, "Current City details saved.", Toast.LENGTH_SHORT).show();
                            displayCurrntCondtns();
                        }else{
                            Toast.makeText(MainActivity.this, "City not found.", Toast.LENGTH_SHORT).show();
                        }

                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        //do nothing
                    }
                });
        alert.show();

    }

    public CityInfo doLocationApiCall(String country, String city) {
        client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://dataservice.accuweather.com/locations/v1/" + country + "/search?apikey=C7V4bfe4RY9NEB50BGVFI4FlxP5aORFC&q=" + city + "")
                .build();
        try {
            Response response = client.newCall(request).execute();
            Log.d("demo", response.toString());
            if (!response.isSuccessful())
                throw new IOException("Unexpected code " + response);

            JSONArray root = new JSONArray(response.body().string());
            JSONObject msg = root.getJSONObject(0);
            CityInfo cityInfo = new CityInfo();
            cityInfo.setCityKey(msg.getString("Key"));
            cityInfo.setCityName(msg.getString("LocalizedName"));
            cityInfo.setCountry(msg.getJSONObject("Country").getString("ID"));
            Log.d("demo", cityInfo.toString());
            return cityInfo;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static CurrentConditions getCurrentCondition(CityInfo cityInfo){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://dataservice.accuweather.com/currentconditions/v1/"+cityInfo.getCityKey()+"?apikey=C7V4bfe4RY9NEB50BGVFI4FlxP5aORFC")
                .build();
        try {
            Response response = client.newCall(request).execute();
            Log.d("demo", response.toString());
            if (!response.isSuccessful())
                throw new IOException("Unexpected code " + response);

            JSONArray root = new JSONArray(response.body().string());
            JSONObject msg = root.getJSONObject(0);
            CurrentConditions currentConditions = new CurrentConditions();
            currentConditions.setLocalObsDateTime(msg.getString("LocalObservationDateTime"));
            currentConditions.setWeatherText(msg.getString("WeatherText"));
            currentConditions.setWeatherIconUrl(msg.getString("WeatherIcon"));
            currentConditions.setTempCel(msg.getJSONObject("Temperature").getJSONObject("Metric").getString("Value"));
            currentConditions.setTempFarh(msg.getJSONObject("Temperature").getJSONObject("Imperial").getString("Value"));
            Log.d("demo", currentConditions.toString());
            return currentConditions;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void displayCurrntCondtns(){
        currentCityConditions=getCurrentCondition(cityInfo);

        TextView simpleText = (TextView) findViewById(R.id.textViewSimpleText);
        simpleText.setVisibility(View.INVISIBLE);
        Button setCurCityButton = (Button) findViewById(R.id.buttonSetCurrCity);
        setCurCityButton.setVisibility(View.INVISIBLE);

        getSupportFragmentManager().beginTransaction()
                .add(activity_main,new BlankFragment(),"tag_blankfragment")
                .commit();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.settings) {
            //Goto Preference Activity
            Intent i = new Intent(MainActivity.this, PreferenceActivity.class);
            startActivity(i);

        }
        return super.onOptionsItemSelected(item);

    }

    public  void loadSavedCities( ArrayList<CityInfo> citiesList){
        ArrayList<CityInfo> savedCitiesList = new ArrayList<CityInfo>();
        savedCitiesList = citiesList;

        TextView tv1 = (TextView) findViewById(R.id.textViewSimpleText1);
        tv1.setVisibility(View.INVISIBLE);
        TextView tv2 = (TextView) findViewById(R.id.textViewSimpleText2);
        tv2.setVisibility(View.INVISIBLE);
        TextView tv3 = (TextView) findViewById(R.id.textViewSavedCities);
        tv3.setVisibility(View.VISIBLE);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        MyListAdapter mAdapter = new MyListAdapter(savedCitiesList, MainActivity.this);
        mAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setHasFixedSize(true);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                mLayoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);


    }
}
