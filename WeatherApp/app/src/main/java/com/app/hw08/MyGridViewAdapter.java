package com.app.hw08;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/* Group 02 - Lakshmi Sridhar, Swetha Adla
   Homework 08
   MyGridViewAdapter.java
 */

public class MyGridViewAdapter extends RecyclerView.Adapter<MyGridViewAdapter.ViewHolder> {
    public FiveDayForecastActivity activity;
    //public Context mContext;
    private ArrayList<FiveDayForecast> mDataset = new ArrayList<>();


    public MyGridViewAdapter(ArrayList<FiveDayForecast> mDataset, FiveDayForecastActivity activity) {
        this.mDataset = mDataset;
        this.activity = activity;
        //this.mContext = mContext

    }

    public static class ViewHolder extends RecyclerView.ViewHolder  {
        public TextView mTvDate;
        public ImageView mIvDayIcon;


        public ViewHolder(View itemView) {
            super(itemView);
            this.mTvDate = (TextView) itemView.findViewById(R.id.textViewForDate);
            this.mIvDayIcon = (ImageView) itemView.findViewById(R.id.imageViewGridDayIcon);
        }

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v =  LayoutInflater.from(parent.getContext())
                .inflate(R.layout.grid_view, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final FiveDayForecast forecast = mDataset.get(position);

        TextView tv = holder.mTvDate;
        tv.setText(formatDate(forecast.getForecastDate()));

        ImageView iv = holder.mIvDayIcon;
        String newWeatherIconUrl ="";
        if((Integer.parseInt(forecast.getDayIcon()))<10){
            newWeatherIconUrl = "0"+forecast.getDayIcon();
        }else{
            newWeatherIconUrl = forecast.getDayIcon();
        }
        Picasso.with(activity).load("http://developer.accuweather.com/sites/default/files/"+newWeatherIconUrl+"-s.png").into(iv);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.setUpDetailWeatherView(forecast);
            }
        });
    }
    @Override
    public int getItemCount() {

        return mDataset.size();
    }

    public static String formatDate(String s){
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        Date date = null;
        String formatedDate ="";
        try {
            date = (Date)formatter.parse(s);
            DateFormat destDf = new SimpleDateFormat("dd MMM yy");

            formatedDate = destDf.format(date);

            return formatedDate;

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return formatedDate;
    }

}
