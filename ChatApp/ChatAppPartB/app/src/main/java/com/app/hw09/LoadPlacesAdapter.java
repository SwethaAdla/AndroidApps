package com.app.hw09;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;


public class LoadPlacesAdapter extends ArrayAdapter<Place> {
    TripPlanActivity activity;
    ArrayList<Place> mData;
    Context mContext;
    int mResource;
    String currentUid ="";
    ArrayList<User> users_list ;
    String tripId;


    public LoadPlacesAdapter(Context context, int resource, ArrayList<Place> objects, String currentUid, TripPlanActivity activity, ArrayList<User> users_list, String tripId) {
        super(context, resource, objects);
        this.mContext = context;
        this.mData = objects;
        this.mResource = resource;
        this.currentUid = currentUid;
        this.activity = activity;
        this.users_list = users_list;
        this.tripId=tripId;
    }


    @NonNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
            if(convertView==null){
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(mResource,parent,false);
        }
        final com.app.hw09.Place place = mData.get(position);
//        Log.d("demo","mData place is "+place.toString());

        final TextView placeName = (TextView) convertView.findViewById(R.id.textViewPlaceName1);
        final TextView placeDetails = (TextView) convertView.findViewById(R.id.textViewPlaceDetails);

        String name = "";
        for(User u:users_list){
            if(place.getAddedBy().equalsIgnoreCase(u.getKey())){
                name = u.getFname()+" "+u.getLname();
                Log.d("demo", name);
            }
        }
        placeName.setText(place.getName());
        placeDetails.setText("Added by "+name+" on "+place.getAddedDate());
        return convertView;
    }

}
