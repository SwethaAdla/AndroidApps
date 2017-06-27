package com.app.hw08;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/* Group 02 - Lakshmi Sridhar, Swetha Adla
   Homework 08
   PreferenceFragment.java
 */

public class PreferenceFragment extends android.preference.PreferenceFragment {
    ListPreference mListPref;
    Preference mCurrentCityPref;
    SharedPreferences sp;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);

        //Temperature preference
        mListPref = (ListPreference)  getPreferenceManager().findPreference("pref_tempUnit");

        mListPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                String newVal = newValue.toString();
                String currentVal = mListPref.getValue();
                Toast.makeText(getActivity(), "Temperature Unit has been changed from "+currentVal+" to "+newVal, Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        //Current City Preference
        mCurrentCityPref = (Preference) getPreferenceManager().findPreference("pref_currentCity");
       // mCurrentCityPref.setDialogLayoutResource(R.layout.alert_textfields_layout);
        mCurrentCityPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                sp = getActivity().getSharedPreferences(MainActivity.PREFS_NAME, Context.MODE_PRIVATE);
                if(sp != null && sp.contains(MainActivity.CURRENT_CITY_KEY)){
                    Gson gson = new Gson();
                    String json = sp.getString(MainActivity.CURRENT_CITY_KEY, "");
                    CityInfo cityInfo = gson.fromJson(json, CityInfo.class);
                    Log.d("demo", cityInfo.toString());

                    showAlertDialog("Update city details", cityInfo.getCityName(), cityInfo.getCountry());

                }else {
                    //set display accordingly
                    showAlertDialog("Enter city details", null, null);

                }
                return false;
            }
        });

    }

    /*@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return super.onCreateView(inflater, container, savedInstanceState);
    }*/

    public void showAlertDialog(String title, String city, String country){
        LayoutInflater factory = LayoutInflater.from(getActivity());
        final View textEntryView = factory.inflate(R.layout.alert_textfields_layout, null);

        final EditText input1 = (EditText) textEntryView.findViewById(R.id.editTextCity);
        if(city !=null){
            input1.setText(city);
        }
        final EditText input2 = (EditText) textEntryView.findViewById(R.id.editTextCountry);
        if(country !=null){
            input2.setText(country);
        }

        final AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        alert.setTitle(title).setView(textEntryView)
                .setPositiveButton("Set", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        Log.d("AlertDialog","TextEntry 1 Entered "+input1.getText().toString());
                        String city = input1.getText().toString();
                        Log.d("AlertDialog","TextEntry 2 Entered "+input2.getText().toString());
                        String country = input2.getText().toString();
                        CityInfo cityInfo = doLocationApiCall(country, city);
                        if(cityInfo != null) {
                            //save in shared pref
                            sp = getActivity().getSharedPreferences(MainActivity.PREFS_NAME, Context.MODE_PRIVATE);
                            SharedPreferences.Editor prefsEditor = sp.edit();
                            Gson gson = new Gson();
                            String json = gson.toJson(cityInfo);
                            prefsEditor.putString(MainActivity.CURRENT_CITY_KEY, json);
                            prefsEditor.commit();
                            Toast.makeText(getActivity(), "Current City details saved.", Toast.LENGTH_SHORT).show();
                            //getCurrentCondition(cityInfo);
                        }else{
                            Toast.makeText(getActivity(), "City not found.", Toast.LENGTH_SHORT).show();
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
        OkHttpClient client = new OkHttpClient();
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

}
