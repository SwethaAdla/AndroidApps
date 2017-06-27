package com.app.hw09;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class TripPlanActivity extends AppCompatActivity {
    int PLACE_PICKER_REQUEST = 1;
    Trip trip;
    ArrayList<com.app.hw09.Place> placesList = new ArrayList<>();
    final ArrayList<User> allUsers = getAllUsers();
    ListView placesListView;
    Button btnRoundTrip;
    Button btnAddPlace;
    TextView noPlaces;
    TextView loginUser;
    String currentUid = "";
    public static final String  YES = "Yes";
    public static final String  NO = "No";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_plan);
        placesListView = (ListView) findViewById(R.id.placesListView);
        btnRoundTrip = (Button) findViewById(R.id.buttonRoundTrip);
        noPlaces = (TextView) findViewById(R.id.textViewnoplaceshdng);
        btnAddPlace = (Button) findViewById(R.id.buttonAddPlace);
        currentUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        loginUser = (TextView) findViewById(R.id.textViewLoginUser);

        if(getIntent().getExtras() != null) {
            trip = (Trip) getIntent().getExtras().get("TRIP");
            Log.d("demo", trip.toString());
            findViewById(R.id.buttonRoundTrip).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                   Intent intent = new Intent(TripPlanActivity.this, MapsActivity.class);
                    Log.d("demo","places list size is -calling maps"+placesList.size());
                    intent.putExtra("PLACES",placesList);
                    startActivity(intent);
                }
            });

            btnAddPlace.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

                    try {
                        startActivityForResult(builder.build(TripPlanActivity.this), PLACE_PICKER_REQUEST);
                        onStart();
                    } catch (GooglePlayServicesRepairableException e) {
                        e.printStackTrace();
                    } catch (GooglePlayServicesNotAvailableException e) {
                        e.printStackTrace();
                    }
                }
            });

            findViewById(R.id.imageViewGoogleMaps).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Create a Uri from an intent string. Use the result to create an Intent.
                    Uri gmmIntentUri = Uri.parse("google.streetview:cbll=46.414382,10.013988");

                    // Create an Intent from gmmIntentUri. Set the action to ACTION_VIEW
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                    // Make the Intent explicit by setting the Google Maps package
                    mapIntent.setPackage("com.google.android.apps.maps");
                    if (mapIntent.resolveActivity(getPackageManager()) != null) {
                        startActivity(mapIntent);
                    }else{
                        Toast.makeText(TripPlanActivity.this, "Google Maps not Installed", Toast.LENGTH_SHORT);
                    }

                }
            });
        }else{
            Toast.makeText(this, "Trip Not found", Toast.LENGTH_SHORT);
        }
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(this, data);
                String toastMsg = String.format("Place: %s", place.getName());
                Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();

                Firebase.setAndroidContext(TripPlanActivity.this);
                final Firebase firebase = new Firebase("https://hw09-a.firebaseio.com/");
                com.app.hw09.Place p = new com.app.hw09.Place();
                p.setName(place.getName().toString());
                p.setLatitude(place.getLatLng().latitude);
                p.setLongitude(place.getLatLng().longitude);
                p.setAddedBy(currentUid);
                Date dt = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MMM-dd");
                p.setAddedDate(String.valueOf(sdf.format(dt)));

                if(placesList != null){
                    placesList.add(p);
                    firebase.child("trips").child(trip.getKey()).child("places").setValue(placesList);
                }else{
                    ArrayList<com.app.hw09.Place> places = new ArrayList<>();
                    //default location charlotte is added first
                   // Latitude	35.227085
                   // Longitude	-80.843124
                    com.app.hw09.Place defaultPlace = new com.app.hw09.Place();
                    defaultPlace.setName("Charlotte");
                    defaultPlace.setLatitude(35.227085);
                    defaultPlace.setLongitude(-80.843124);
                    defaultPlace.setAddedBy("");
                   // Date date = new Date();
                   // SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MMM-dd");
                    defaultPlace.setAddedDate(String.valueOf(sdf.format(dt)));
                    places.add(defaultPlace);
                    places.add(p);
                    firebase.child("trips").child(trip.getKey()).child("places").setValue(places);
                }
            }
        }
    }
    public ArrayList<User> getAllUsers(){
        final ArrayList<User> users = new ArrayList<>();
        Firebase.setAndroidContext(TripPlanActivity.this);
        FirebaseDatabase.getInstance().getReference("users").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                users.clear();
                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    Log.d("demo", snapshot.getValue(User.class).toString());
                    users.add(snapshot.getValue(User.class));
                }
                Log.d("demo","nameslist size: "+users.size());
                String name = "";
                String loginName="";

                for (User u : users) {
                    if (u.getKey().equalsIgnoreCase(trip.getCreatedByUid())) {
                        name = u.getFname() + " " + u.getLname();
                    }
                    if(u.getKey().equalsIgnoreCase(currentUid)){
                        loginName = u.getFname()+" "+u.getLname();
                    }
                }
                TextView tripName = (TextView) findViewById(R.id.textViewTripPlanName);
                tripName.setText(trip.getTitle()+", "+trip.getLocation()+". Created by: "+name);
                loginUser.setText("Welcome "+loginName+".");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return users;
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseDatabase.getInstance().getReference("trips").child(trip.getKey()).child("places").addValueEventListener(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
                placesList = new ArrayList<com.app.hw09.Place>();
                for (com.google.firebase.database.DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    Log.d("demo", snapshot.getValue(Trip.class).toString());
                    placesList.add(snapshot.getValue(com.app.hw09.Place.class));
                }
                Log.d("demo","all places size: "+placesList.size());
                //trip.setPlaces(allPlaces);
                if(!placesList.isEmpty()){
                    btnRoundTrip.setEnabled(true);
                    noPlaces.setVisibility(View.GONE);
                    LoadPlacesAdapter adapter = new LoadPlacesAdapter(getApplicationContext(), R.layout.places_row_view, placesList, currentUid,TripPlanActivity.this, allUsers,trip.getKey());
                    placesListView.setAdapter(adapter);
                    placesListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                        @Override
                        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                            Log.d("demo","Long clicked the place "+placesList.get(position));
                            Log.d("demo","Position value is "+position);
                            alertDailogBuilder(position);
                            return false;
                        }
                    });
                }else{
                    Log.d("demo","there r no places added here");
                    noPlaces.setVisibility(View.VISIBLE);
                    placesListView.setVisibility(View.INVISIBLE);
                    btnRoundTrip.setEnabled(false);
                }
                if(trip.isRemoved){
                    btnAddPlace.setEnabled(false);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void alertDailogBuilder(final int pos) {

        AlertDialog.Builder builder = new AlertDialog.Builder(TripPlanActivity.this);

        builder.setMessage("Do you really want to delete this place?")
                .setPositiveButton(YES, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Firebase.setAndroidContext(getApplicationContext());
                        final Firebase firebase = new Firebase("https://hw09-a.firebaseio.com/");
                        //firebase.child("trips1").child(trip.getKey()).child("places").child(trip.getPlaces().get(pos).getKey()).removeValue();
                        //firebase.child("trips").child(trip.getKey()).child("places").child(String.valueOf(pos)).removeValue();
                        placesList.remove(pos);
                        firebase.child("trips").child(trip.getKey()).child("places").setValue(placesList);
                    }
                })
                .setNegativeButton(NO, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .show();
    }

}
