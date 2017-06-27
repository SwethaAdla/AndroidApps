package com.app.hw08;

/* Group 02 - Lakshmi Sridhar, Swetha Adla
   Homework 08
   FiveDayForecastActivity.java
 */

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class FiveDayForecastActivity extends AppCompatActivity {
    public CityInfo cityInfo;
    public String headline;
    public String extendedForecastUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_five_day_forecast);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setLogo(R.drawable.logo_ldpi);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        if(getIntent().getExtras() != null){
            cityInfo = (CityInfo) getIntent().getExtras().get("CITY_INFO_KEY");
            getFiveDayForecast(cityInfo);
        }
    }

    public void getFiveDayForecast(CityInfo cityObj){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://dataservice.accuweather.com/forecasts/v1/daily/5day/"+cityObj.getCityKey()+"?apikey=C7V4bfe4RY9NEB50BGVFI4FlxP5aORFC")
                .build();
        try {
            Response response = client.newCall(request).execute();
            Log.d("demo", response.toString());
            if (!response.isSuccessful())
                throw new IOException("Unexpected code " + response);
            ArrayList<FiveDayForecast> forecastArrayList = new ArrayList<>();
            JSONObject root = new JSONObject(response.body().string());
            headline = root.getJSONObject("Headline").getString("Text");
            extendedForecastUrl = root.getJSONObject("Headline").getString("MobileLink");
            JSONArray forecastArr = root.getJSONArray("DailyForecasts");
            for(int i=0; i<forecastArr.length(); i++){
                JSONObject forecastObj = forecastArr.getJSONObject(i);
                FiveDayForecast fiveDayForecast = new FiveDayForecast();
                fiveDayForecast.setForecastDate(forecastObj.getString("Date"));
                fiveDayForecast.setMinTemp(forecastObj.getJSONObject("Temperature").getJSONObject("Minimum").getString("Value"));
                fiveDayForecast.setMaxTemp(forecastObj.getJSONObject("Temperature").getJSONObject("Maximum").getString("Value"));
                fiveDayForecast.setDayPhrase(forecastObj.getJSONObject("Day").getString("IconPhrase"));
                fiveDayForecast.setDayIcon(forecastObj.getJSONObject("Day").getString("Icon"));
                fiveDayForecast.setNightPhrase(forecastObj.getJSONObject("Night").getString("IconPhrase"));
                fiveDayForecast.setNightIcon(forecastObj.getJSONObject("Night").getString("Icon"));
                fiveDayForecast.setDetaliUrl(forecastObj.getString("MobileLink"));
                forecastArrayList.add(fiveDayForecast);
            }
            Log.d("demo", forecastArrayList.size()+forecastArrayList.toString());
            setUpDisplay(forecastArrayList);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void setUpDisplay(ArrayList<FiveDayForecast> forecastList){

        TextView tvTitle = (TextView) findViewById(R.id.textViewTitle);
        tvTitle.setPaintFlags(tvTitle.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        tvTitle.setText("Daily forecast for "+cityInfo.getCityName()+", "+cityInfo.getCountry());

        // setting first day forecast in the view
        setUpDetailWeatherView(forecastList.get(0));

        TextView textView = (TextView) findViewById(R.id.textViewExtededForecast);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(extendedForecastUrl));
                startActivity(browserIntent);
            }
        });

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerViewFiveDay4cast);
       GridLayoutManager mLayoutManager = new GridLayoutManager(this, 1, GridLayoutManager.HORIZONTAL, false);
       /*GridLayoutManager mLayoutManager = new GridLayoutManager(this, 3);
       mLayoutManager.setOrientation(GridLayoutManager.HORIZONTAL);*/
        MyGridViewAdapter mAdapter = new MyGridViewAdapter(forecastList, FiveDayForecastActivity.this);
        mAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setHasFixedSize(false);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                mLayoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);


    }
    public void setUpDetailWeatherView(final FiveDayForecast forecast){
        final String DEGREE  = "\u00b0";
        Log.d("demo","degree symbol is "+DEGREE);
        TextView tvHeadline = (TextView) findViewById(R.id.textViewHeadline);
        tvHeadline.setText(headline);
        TextView tvDate = (TextView) findViewById(R.id.textViewForecastDate);
        tvDate.setText("Forecast on "+formatDate(forecast.getForecastDate()));
        TextView tvTemp = (TextView) findViewById(R.id.textViewTemp);
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(FiveDayForecastActivity.this);
        String tempUnit = sharedPref.getString("pref_tempUnit","");
        Log.d("demo","the saved temperature preference is "+tempUnit);
        if(tempUnit.equalsIgnoreCase("Celcius")){
            double dmax = ((Double.parseDouble(forecast.getMaxTemp()))-32)/1.8;
            double dmin = ((Double.parseDouble(forecast.getMinTemp()))-32)/1.8;
            DecimalFormat numberFormat = new DecimalFormat("#.0");
            tvTemp.setText("Temperature : "+numberFormat.format(dmax)+DEGREE+" C / "+numberFormat.format(dmin)+DEGREE+" C");
        }else if(tempUnit.equalsIgnoreCase("Fahrenheit")){
            tvTemp.setText("Temperature : "+forecast.getMaxTemp()+DEGREE+" F / "+forecast.getMinTemp()+DEGREE+" F");
        }

        TextView tvDayPhrase = (TextView) findViewById(R.id.textViewDayPhrase);
        tvDayPhrase.setText(forecast.getDayPhrase());
        TextView tvNightPhrase = (TextView) findViewById(R.id.textViewNightPhrase);
        tvNightPhrase.setText(forecast.getNightPhrase());

        String newWeatherIconUrl ="";
        ImageView ivDayIcon = (ImageView) findViewById(R.id.imageViewDay);

        if((Integer.parseInt(forecast.getDayIcon()))<10){
            newWeatherIconUrl = "0"+forecast.getDayIcon();
        }else{
            newWeatherIconUrl = forecast.getDayIcon();
        }
        Picasso.with(this).load("http://developer.accuweather.com/sites/default/files/"+newWeatherIconUrl+"-s.png").into(ivDayIcon);

        ImageView ivNightIcon = (ImageView) findViewById(R.id.imageViewNight);
        String icon ="";
        if((Integer.parseInt(forecast.getNightIcon()))<10){
            icon = "0"+forecast.getNightIcon();
        }else{
            icon = forecast.getNightIcon();
        }
        Picasso.with(this).load("http://developer.accuweather.com/sites/default/files/"+icon+"-s.png").into(ivNightIcon);

        TextView tvMoreDetails = (TextView) findViewById(R.id.textViewDetails);
        tvMoreDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(forecast.getDetaliUrl()));
                startActivity(browserIntent);
            }
        });

    }

    public static String formatDate(String s){
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        Date date = null;
        String formatedDate ="";
        try {
            date = (Date)formatter.parse(s);
            DateFormat destDf = new SimpleDateFormat("MMMM dd, yyyy");
            formatedDate = destDf.format(date);
            return formatedDate;

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return formatedDate;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        int flag = 0;

        if (id == R.id.saveCity) {
            //save in Firebase
            CurrentConditions cityToSaveConditions = MainActivity.getCurrentCondition(cityInfo);
            cityInfo.setTemperature(cityToSaveConditions.getTempCel());
            cityInfo.setTempFaren(cityToSaveConditions.getTempFarh());
            cityInfo.setCurrentTime(cityToSaveConditions.getLocalObsDateTime());
            cityInfo.setFavourite(false);
            Log.d("demo","size of saved cities five day forecast activity "+MainActivity.savedCities.size());
            for(int i=0;i<MainActivity.savedCities.size();i++){
                if(cityInfo.getCityName().equalsIgnoreCase(MainActivity.savedCities.get(i).getCityName())){
                    Toast.makeText(FiveDayForecastActivity.this,"City is already Saved",Toast.LENGTH_SHORT).show();
                    flag++;
                }else{
                    continue;
                }
            }
            if(flag==0) {
                Toast.makeText(FiveDayForecastActivity.this, "City Saved", Toast.LENGTH_SHORT).show();
                Firebase.setAndroidContext(getApplicationContext());
                final Firebase firebase = new Firebase("https://hw08-ff5a4.firebaseio.com/");
                String key = firebase.child("Cities").push().getKey();
                cityInfo.setKey(key);
                Log.d("demo", key);
                firebase.child("Cities").child(key).setValue(cityInfo);
            }

        }else if (id == R.id.setCurrentCity) {
            // save in shared prefrence
            SharedPreferences sp = FiveDayForecastActivity.this.getSharedPreferences(MainActivity.PREFS_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor prefsEditor = sp.edit();
            Gson gson = new Gson();
            String json = gson.toJson(cityInfo);
            prefsEditor.putString(MainActivity.CURRENT_CITY_KEY, json);
            prefsEditor.commit();
            Toast.makeText(FiveDayForecastActivity.this, "Current City Saved.", Toast.LENGTH_SHORT).show();

        }else  if (id == R.id.settings) {
            //Goto Preference Activity
            Intent i = new Intent(FiveDayForecastActivity.this, PreferenceActivity.class);
            startActivity(i);

        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(FiveDayForecastActivity.this,MainActivity.class);
        startActivity(i);
    }
}
