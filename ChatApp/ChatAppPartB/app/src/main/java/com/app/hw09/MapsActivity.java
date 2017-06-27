package com.app.hw09;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;


import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    LocationManager mLocationMngr;
    LocationListener mListner;
    Location mCurrentLocation;
    Boolean locationTracking = false;
    Polyline polyline;
    int status =0;
    List<LatLng> latLongList;
   // Map<String, LatLng> latLngMap;
    ArrayList<Place> placeArrayList;
    private GoogleMap mMap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        mLocationMngr = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        placeArrayList = (ArrayList<Place>) getIntent().getExtras().get("PLACES");
        //latLngMap = (Map<String, LatLng>) getIntent().getExtras().get("PLACES");
       // latLongList = (List<LatLng>) getIntent().getExtras().get("PLACES");
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        /*mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));*/
        mMap = googleMap;

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

        } else if (mMap != null) {
            // Access to the location has been granted to the app.
            mMap.setMyLocationEnabled(true);
        }

        //mMap.setMyLocationEnabled(true);

        if (mCurrentLocation != null) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(mCurrentLocation.getLatitude(),
                            mCurrentLocation.getLongitude()), 12));
        }

        /*mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                if(!locationTracking){
                    latLongList.clear();
                    Log.d("MapActivity",""+latLongList.size());
                    if(status == 1) {
                        mMap.clear();
                    }
                    Toast.makeText(MapsActivity.this, "Starting Location Tracking", Toast.LENGTH_LONG).show();
                    locationTracking = true;
                    trackLocation();
                } else {
                    locationTracking = false;
                    Toast.makeText(MapsActivity.this, "Stopping Location Tracking", Toast.LENGTH_LONG).show();
                    stopTracking();
                    status = 1;
                }
            }
        });*/


        for(int j=0; j<placeArrayList.size();j++){
            /*if(j==0){
                mMap.addMarker(new MarkerOptions()
                        .position(latLongList.get(0))
                        .title("Start Point"));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                        new LatLng(latLongList.get(0).latitude,
                                latLongList.get(0).longitude), 12));
            }else if(j== latLongList.size()-1){
                mMap.addMarker(new MarkerOptions()
                        .position(latLongList.get(latLongList.size()-1))
                        .title("End Point"));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                        new LatLng(latLongList.get(latLongList.size()-1).latitude,
                                latLongList.get(latLongList.size()-1).longitude), 12));
            }else{*/
            LatLng latlang= new LatLng(placeArrayList.get(j).getLatitude(),placeArrayList.get(j).getLongitude());
                mMap.addMarker(new MarkerOptions()
                        .position(latlang)
                        .title(placeArrayList.get(j).getName()));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                        new LatLng(latlang.latitude,
                                latlang.longitude), 12));
           // }
        }



        //mCurrentLocation = locations.get(0);

        PolylineOptions polylineOptions = new PolylineOptions();

        // Create polyline options with existing LatLng ArrayList
        latLongList = Place.getLatLngList(placeArrayList);
        polylineOptions.addAll(latLongList);
        polylineOptions
                .width(7)
                .color(Color.GREEN);

        // Adding multiple points in map using polyline and arraylist
        polyline = mMap.addPolyline(polylineOptions);

        moveToBounds(polyline);

    }
    /*private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
        } else if (mMap != null) {
            // Access to the location has been granted to the app.
            mMap.setMyLocationEnabled(true);
        }
    }

    public void trackLocation(){
        if (!mLocationMngr.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("GPS Not Enabled")
                    .setMessage("would you like to enable the GPS Settings?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivity(intent);
                        }
                    }).setNegativeButton("No", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                    finish();
                }
            }).setCancelable(false);

            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        } else {
            mListner = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    latLongList.add(new LatLng(location.getLatitude(), location.getLongitude()));
                    mMap.addMarker(new MarkerOptions()
                            .position(latLongList.get(0))
                            .title("Start Point"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                            new LatLng(location.getLatitude(),
                                    location.getLongitude()), 12));
                    //mCurrentLocation = locations.get(0);

                    PolylineOptions polylineOptions = new PolylineOptions();

                    // Create polyline options with existing LatLng ArrayList
                    polylineOptions.addAll(latLongList);
                    polylineOptions
                            .width(7)
                            .color(Color.YELLOW);

                    // Adding multiple points in map using polyline and arraylist
                    polyline = mMap.addPolyline(polylineOptions);

                    moveToBounds(polyline);
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                @Override
                public void onProviderEnabled(String provider) {

                }

                @Override
                public void onProviderDisabled(String provider) {

                }
            };

            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mLocationMngr.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 50, mListner);
        }

    }

    public void stopTracking(){
        LatLng endLoc = latLongList.get(latLongList.size()-1);

        mMap.addMarker(new MarkerOptions()
                .position(endLoc)
                .title("Stop Point"));
    }
*/
    private void moveToBounds(Polyline p){
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for(int i = 0; i < p.getPoints().size();i++){
            builder.include(p.getPoints().get(i));
        }

        LatLngBounds bounds = builder.build();
        int padding = 120; // offset from edges of the map in pixels

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, padding);
        Log.d("demo",bounds.toString());
        mMap.animateCamera(cameraUpdate);
    }
}

