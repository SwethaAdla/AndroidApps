package com.app.hw06;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;



public class AppAdapter extends ArrayAdapter<App> {
    ArrayList<App> mData;
    Context mContext;
    int mResource;

    public AppAdapter(Context context, int resource, ArrayList<App> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.mData = objects;
        this.mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView==null){
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(mResource,parent,false);
        }

        App app = mData.get(position);

        TextView appDetails = (TextView) convertView.findViewById(R.id.appDetailsTextView);
        appDetails.setText(app.appName+"\n"+"Price: USD "+app.getAppPrice());

        ImageView iv = (ImageView) convertView.findViewById(R.id.appIconImg);
        Picasso.with(mContext).load(app.getImageUrl()).into(iv);
        ImageView favImg = (ImageView) convertView.findViewById(R.id.imageStar);
        if(app.isFavourite()){
            favImg.setImageResource(R.drawable.black_star);
        }else{
            favImg.setImageResource(R.drawable.white_star);
        }

        return convertView;
    }
}
