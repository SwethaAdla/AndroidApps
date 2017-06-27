package com.app.hw08;

import android.content.SharedPreferences;
import android.preference.ListPreference;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;

import org.ocpsoft.prettytime.PrettyTime;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/* Group 02 - Lakshmi Sridhar, Swetha Adla
   Homework 08
   MyListAdapter.java
 */

public class MyListAdapter extends RecyclerView.Adapter<MyListAdapter.ViewHolder> {
    public MainActivity activity;
    private ArrayList<CityInfo> mDataset = new ArrayList<CityInfo>();


    public MyListAdapter(ArrayList<CityInfo> mDataset, MainActivity activity) {
        this.mDataset = mDataset;
        this.activity = activity;

    }


    public static class ViewHolder extends RecyclerView.ViewHolder  {
        public TextView mTextViewCityCountry;
        public ImageView mImageViewFavourite;
        public  TextView mTextViewLastUpdated;
        public TextView mTextViewTemperature;


        public ViewHolder(View itemView) {
            super(itemView);
            this.mTextViewCityCountry = (TextView) itemView.findViewById(R.id.textViewCityCountry);
            this.mImageViewFavourite = (ImageView) itemView.findViewById(R.id.imageViewFavourite);
            this.mTextViewLastUpdated = (TextView) itemView.findViewById(R.id.textViewLastUpdated1);
            this.mTextViewTemperature = (TextView) itemView.findViewById(R.id.textViewTemperature);
        }

    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v =  LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_view, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        //ListPreference mListPref=(ListPreference)  getPreferenceManager().findPreference("pref_tempUnit");;
        final String DEGREE  = "\u00b0";
        Log.d("demo","degree symbol is "+DEGREE);
        final CityInfo cityInfo = mDataset.get(position);
        final TextView cityCountry = holder.mTextViewCityCountry;
        cityCountry.setText(cityInfo.getCityName()+", "+cityInfo.getCountry());
        TextView temperature = holder.mTextViewTemperature;
        //get the default temperature unit saved in preferences
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(activity);
        String tempUnit = sharedPref.getString("pref_tempUnit","");
        Log.d("demo","the saved temperature preference is "+tempUnit);
        if(tempUnit.equalsIgnoreCase("Celcius")){
            temperature.setText("Temperature: "+cityInfo.getTemperature()+DEGREE+" C");
        }else if(tempUnit.equalsIgnoreCase("Fahrenheit")){
            temperature.setText("Temperature: "+cityInfo.getTempFaren()+DEGREE+" F");
        }

        Date dt = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        try {
            dt = sdf.parse(cityInfo.getCurrentTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        PrettyTime p = new PrettyTime();
        TextView updateTime = holder.mTextViewLastUpdated;
        updateTime.setText("Last Updated: "+p.format(dt));

        ImageView favImage = holder.mImageViewFavourite;
        if(cityInfo.isFavourite()){
            favImage.setImageResource(R.drawable.ldpi_g);
        }else{
            favImage.setImageResource(R.drawable.ldpi_b);
        }
        favImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean value = false;
                if(cityInfo.isFavourite()){
                    cityInfo.setFavourite(false);
                    value = false;

                }else{
                    cityInfo.setFavourite(true);
                    value = true;
                }
                //Update in firebase
                Firebase.setAndroidContext(activity.getApplicationContext());
                Firebase m_objFireBaseRef = new Firebase("https://hw08-ff5a4.firebaseio.com/");
                Firebase objRef = m_objFireBaseRef.child("Cities");
                Log.d("demo0000", objRef.toString());
                objRef.child(cityInfo.getKey()).child("isFavourite").setValue(value);

            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Firebase.setAndroidContext(activity.getApplicationContext());
                final Firebase firebase = new Firebase("https://hw08-ff5a4.firebaseio.com/");
                firebase.child("Cities").child(cityInfo.getKey()).removeValue();
                mDataset.remove(position);
                //activity.savedCities.remove(cityInfo);
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
