package com.app.hw09;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Place implements Serializable {

    String name, addedBy, addedDate, key;
    double latitude, longitude;

    @Override
    public String toString() {
        return "Place{" +
                "name='" + name + '\'' +
                ", addedBy='" + addedBy + '\'' +
                ", addedDate='" + addedDate + '\'' +
                ", key='" + key + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddedBy() {
        return addedBy;
    }

    public void setAddedBy(String addedBy) {
        this.addedBy = addedBy;
    }

    public String getAddedDate() {
        return addedDate;
    }

    public void setAddedDate(String addedDate) {
        this.addedDate = addedDate;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public static ArrayList<LatLng>  getLatLngList(ArrayList<Place> places){
        ArrayList<LatLng> latLngs = new ArrayList<>();
        for(Place p: places){
            latLngs.add(new LatLng(p.getLatitude(),p.getLongitude()));
        }
        return latLngs;
    }
    /*public static Map<String, LatLng> getLatLngList(ArrayList<Place> places){
       // ArrayList<LatLng> latLngs = new ArrayList<>();
        Map<String, LatLng> latlngMap = new HashMap<String, LatLng>();
        for(Place p: places){
            latlngMap.put(p.getName(),(new LatLng(p.getLatitude(),p.getLongitude())));
          //  latLngs.add(new LatLng(p.getLatitude(),p.getLongitude()));
        }
        return latlngMap;
    }*/
}
