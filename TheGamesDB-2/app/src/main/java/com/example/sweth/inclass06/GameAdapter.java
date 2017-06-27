package com.example.sweth.inclass06;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import it.sephiroth.android.library.picasso.Picasso;

public class GameAdapter extends ArrayAdapter<Game> {

    ArrayList<Game> mData;
    Context mContext;
    int mResource;

    public GameAdapter(Context context, int resource, ArrayList<Game> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.mData = objects;
        this.mResource = resource;
    }


    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Log.d("demo","in getView method");

        if(convertView==null){
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(mResource,parent,false);
        }

        Game game = mData.get(position);
        //LinearLayout ll = (LinearLayout) convertView.findViewById(R.id.LinearLayout);

        ImageView iv = (ImageView) convertView.findViewById(R.id.imageView);
        //if(game.getBaseImgUrl()!=null && game.getClearLogo()!=null && !game.getBaseImgUrl().isEmpty() && !game.getClearLogo().isEmpty()) {
            Picasso.with(mContext).load("http://thegamesdb.net/banners/clearlogo/"+game.getId()+".png").into(iv);
        //}
        //new GetImageAsyncTask().execute(game.getBaseImgUrl());

        TextView tv = (TextView) convertView.findViewById(R.id.GameDetails);
        String newDate;
        if(game.getReleaseDate().length()<6 && game.getReleaseDate()!=null){
            newDate = game.getReleaseDate();
        }else{
            newDate = formatYear(game.getReleaseDate());
        }
        Log.d("demo","the game is "+game.toString());
        tv.setText(game.getGameTitle()+". Released in "+newDate+". Platform: "+game.getPlatform()+".");
        //tv.setText("game details");

        //ll.addView(tv);

        return convertView;
    }

    public static String formatYear(String s){
        if(s != null && s.length() != 0){
            return s.substring(6);
        }else if(s == null ){
            return "";
        }else{
            return "";
        }
    }

}
