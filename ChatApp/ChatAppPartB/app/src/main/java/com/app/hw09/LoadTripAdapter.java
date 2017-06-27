package com.app.hw09;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class LoadTripAdapter extends ArrayAdapter<Trip> {
    TripActivity activity;
    ArrayList<Trip> mData;
    Context mContext;
    int mResource;
    String currentUid ="";
    ArrayList<User> users_list ;
    ArrayList<String> participantsList2 = new ArrayList<String>();
    ArrayList<String> tripsList = new ArrayList<String>();

    public LoadTripAdapter(Context context, int resource, ArrayList<Trip> objects, String currentUid,TripActivity activity, ArrayList<User> users_list) {
        super(context, resource, objects);
        this.mContext = context;
        this.mData = objects;
        this.mResource = resource;
        this.currentUid = currentUid;
        this.activity = activity;
        this.users_list = users_list;
    }


    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Firebase firebase = new Firebase("https://hw09-a.firebaseio.com/");
        if(convertView==null){
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(mResource,parent,false);
        }
        final Trip trip = mData.get(position);

        final Button btnJoin = (Button) convertView.findViewById(R.id.buttonJoinTrip);
        final Button btnRemove = (Button) convertView.findViewById(R.id.buttonRemoveTrip);
        final TextView tripid = (TextView) convertView.findViewById(R.id.textViewTripTitle);
        final Button btnTripPlan = (Button) convertView.findViewById(R.id.buttonTripPlan);
        tripid.setText(trip.getTitle()+","+trip.getLocation()+".");

        if(trip.getParticipantList() !=null && trip.getParticipantList().contains(currentUid)) {
            if(trip.createdByUid.equals(currentUid)){
                btnRemove.setVisibility(View.VISIBLE);
                btnRemove.setEnabled(true);
                btnRemove.setText("Remove");
            }
            btnJoin.setVisibility(View.INVISIBLE);
            btnTripPlan.setVisibility(View.VISIBLE);
            /*}else{
                btnJoin.setVisibility(View.INVISIBLE);
                btnRemove.setVisibility(View.INVISIBLE);
                btnTripPlan.setVisibility(View.INVISIBLE);
            }*/

        }else{
            btnJoin.setText("Join");
            btnJoin.setEnabled(true);
            btnJoin.setVisibility(View.VISIBLE);
            btnRemove.setVisibility(View.INVISIBLE);
            btnTripPlan.setVisibility(View.INVISIBLE);
        }

        String name = "";
        for(User u:users_list){
            if(trip.getCreatedByUid().equalsIgnoreCase(u.getKey())){
                name = u.getFname()+" "+u.getLname();
                Log.d("demo", name);
            }
        }
        TextView createdBy = (TextView) convertView.findViewById(R.id.textViewcreatedBy);
        //find if the trip is mytrip or other's trip by validating currentuserid
        if(trip.getCreatedByUid().equalsIgnoreCase(currentUid)){
            createdBy.setVisibility(View.GONE);
           // btnRemove.setVisibility(View.VISIBLE);
        }else{
            createdBy.setText(name);
            createdBy.setVisibility(View.VISIBLE);
          //  btnRemove.setVisibility(View.INVISIBLE);

        }
        if(trip.isRemoved){
            btnJoin.setEnabled(false);
            btnJoin.setVisibility(View.INVISIBLE);
            btnRemove.setEnabled(false);
            btnRemove.setText("Removed");
            btnRemove.setVisibility(View.VISIBLE);
        }
        TextView createdDate = (TextView) convertView.findViewById(R.id.textViewCreatedOn);
        Log.d("demo", trip.getCreatedDate());
        createdDate.setText(trip.getCreatedDate());

        btnJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnJoin.setText("Joined");
                btnJoin.setEnabled(false);
                participantsList2 = trip.getParticipantList();
                participantsList2.add(currentUid);
                Log.d("demo", participantsList2.toString());
                Firebase.setAndroidContext(activity.getApplicationContext());
                firebase.child("trips").child(trip.getKey()).child("participantList").setValue(participantsList2);
                tripsList.add(trip.getKey());
                Log.d("LoadTripAdapter","list2 modified-"+participantsList2.size() +"db modified-"+trip.getParticipantList());
            }
        });

        btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Firebase.setAndroidContext(activity.getApplicationContext());
                final Firebase firebase = new Firebase("https://hw09-a.firebaseio.com/");
                //remove the messages from chat room
                firebase.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(trip.getKey()).child("messages").removeValue();
                // set flag isRemoved true
                firebase.child("trips").child(trip.getKey()).child("removed").setValue(true);

            }
        });

        btnTripPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, TripPlanActivity.class);
                intent.putExtra("TRIP", trip);
                activity.startActivity(intent);
            }
        });
        return convertView;
    }


}

