package com.app.hw09;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;
import java.util.ArrayList;

public class TripActivity extends AppCompatActivity {
    ArrayList<Trip> tripsList ;
    ArrayList<Trip> myTripsList;
    ArrayList<Trip> othersTripsList;
    ArrayList<User> usersList;
    User currentUser;
    String currentUid = "";
    ArrayList<String> frndList;
    ListView myTrips;
    ListView othersTrips;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setLogo(R.drawable.ldpi);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        myTrips = (ListView) findViewById(R.id.listView_MyTrips);
        othersTrips = (ListView) findViewById(R.id.listView_othersTrips);

        findViewById(R.id.buttonAddTrip).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(TripActivity.this, CreateTripActivity.class);
                startActivity(i);
            }
        });
        currentUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Firebase.setAndroidContext(getApplicationContext());
        FirebaseDatabase.getInstance().getReference("trips").addValueEventListener(new com.google.firebase.database.ValueEventListener() {

            @Override
            public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
                tripsList = new ArrayList<Trip>();
                for (com.google.firebase.database.DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    Log.d("demo", snapshot.getValue(Trip.class).toString());
                    tripsList.add(snapshot.getValue(Trip.class));
                }
                Log.d("demo","Tripslist size: "+tripsList.size());
                loadTripsList(tripsList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void loadTripsList(ArrayList<Trip> trips){
        myTripsList = new ArrayList<>();
        othersTripsList = new ArrayList<>();
        for(Trip t:trips){
            if(t.getParticipantList()!= null && t.getParticipantList().contains(currentUid)){
                myTripsList.add(t);
            }else{
                othersTripsList.add(t);
            }
        }
        if(!myTripsList.isEmpty()) {
            loadMyTrips(myTripsList);
        }else{
            TextView tv01 = (TextView) findViewById(R.id.textViewNotripsHdng1);
            tv01.setVisibility(View.VISIBLE);
            myTrips.setVisibility(View.GONE);
        }
        if(!othersTripsList.isEmpty()) {
            loadOtherTrips(othersTripsList);
        }else{
            TextView tv02 = (TextView) findViewById(R.id.textViewNotripsHdng2);
            tv02.setVisibility(View.VISIBLE);
            othersTrips.setVisibility(View.GONE);
        }

    }

    public void loadMyTrips(final ArrayList<Trip> myTripsList){
        Firebase.setAndroidContext(getApplicationContext());
        currentUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Log.d("Current User id" , currentUid);
        FirebaseDatabase.getInstance().getReference("users").addValueEventListener(new com.google.firebase.database.ValueEventListener(){
            @Override
            public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
                usersList = new ArrayList<User>();
                for (DataSnapshot d : dataSnapshot.getChildren()) {
                    String id = d.getKey();
                    Log.d("id", id);
                    User user = d.getValue(User.class);
                    Log.d("user", user.toString());
                    if (id.equals(currentUid)) {
                        currentUser = user;
                        TextView tvWelcomeMssg = (TextView) findViewById(R.id.textViewWelcome);
                        tvWelcomeMssg.setText("Welcome "+currentUser.getFname()+" "+currentUser.getLname()+ ".");
                    } else {
                        usersList.add(user);
                    }
                }
                Log.d("TripActivity ","User ArrayList size: "+usersList.size());

                LoadTripAdapter adapter = new LoadTripAdapter(getApplicationContext(), R.layout.tripslist_row_view, myTripsList, currentUid,TripActivity.this, usersList);
                myTrips.setAdapter(adapter);
                myTrips.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Trip selTrip = myTripsList.get(position);
                        Intent intent_message = new Intent(TripActivity.this,MessagesActivity.class);
                        intent_message.putExtra("TRIP_SELECTED",selTrip);
                        startActivity(intent_message);
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void loadOtherTrips(final ArrayList<Trip> othersTripsList){
        Firebase.setAndroidContext(getApplicationContext());
        currentUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Log.d("Current User id" , currentUid);
        FirebaseDatabase.getInstance().getReference("users").addValueEventListener(new com.google.firebase.database.ValueEventListener(){
            @Override
            public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
                usersList = new ArrayList<User>();
                ArrayList<Trip> tempList = new ArrayList<>();
                for (DataSnapshot d : dataSnapshot.getChildren()) {
                    String id = d.getKey();
                    Log.d("id", id);
                    User user = d.getValue(User.class);
                    Log.d("user", user.toString());
                    if (id.equals(currentUid)) {
                        currentUser = user;
                        TextView tvWelcomeMssg = (TextView) findViewById(R.id.textViewWelcome);
                        tvWelcomeMssg.setText("Welcome "+currentUser.getFname()+" "+currentUser.getLname());
                        frndList = currentUser.getFriendList();
                        for(Trip trip : othersTripsList){
                            if(currentUser.getFriendList() != null && currentUser.getFriendList().contains(trip.getCreatedByUid())){
                                tempList.add(trip);
                            }
                        }

                        if(tempList.isEmpty()){
                            Log.d("demo","there r no trips displaying hdng");
                            TextView tv02 = (TextView) findViewById(R.id.textViewNotripsHdng2);
                            tv02.setVisibility(View.VISIBLE);
                            othersTrips.setVisibility(View.GONE);
                        }
                        else{
                            LoadTripAdapter adapter2 = new LoadTripAdapter(getApplicationContext(), R.layout.tripslist_row_view, tempList, currentUid,TripActivity.this, usersList);
                            othersTrips.setAdapter(adapter2);
                            othersTrips.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    Toast.makeText(getApplicationContext(), "Please Join the trip to view trip details", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                    } else {
                        usersList.add(user);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_friendList:
                Intent intent_frn = new Intent(TripActivity.this,FriendsListActivity.class);
                startActivity(intent_frn);
                break;
            case R.id.nav_logout:
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(TripActivity.this,MainActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_editProfile:
                Intent intent_profile = new Intent(TripActivity.this,EditProfileActivity.class);
                startActivity(intent_profile);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}