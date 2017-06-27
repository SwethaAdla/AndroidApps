package com.app.hw08;

import java.io.Serializable;

/* Group 02 - Lakshmi Sridhar, Swetha Adla
   Homework 08
   CityInfo.java
 */

public class CityInfo implements Serializable{

    String cityKey, cityName, country, temperature, currentTime, key, tempFaren;
    boolean isFavourite;

    public String getCityKey() {
        return cityKey;
    }

    public void setCityKey(String cityKey) {
        this.cityKey = cityKey;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public boolean isFavourite() {
        return isFavourite;
    }

    public void setFavourite(boolean favourite) {
        isFavourite = favourite;
    }

    public String getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(String currentTime) {
        this.currentTime = currentTime;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getTempFaren() {
        return tempFaren;
    }

    public void setTempFaren(String tempFaren) {
        this.tempFaren = tempFaren;
    }

    @Override
    public String toString() {
        return "CityInfo{" +
                "cityKey='" + cityKey + '\'' +
                ", cityName='" + cityName + '\'' +
                ", country='" + country + '\'' +
                ", temperature='" + temperature + '\'' +
                ", currentTime='" + currentTime + '\'' +
                ", key='" + key + '\'' +
                ", tempFaren='" + tempFaren + '\'' +
                ", isFavourite=" + isFavourite +
                '}';
    }
}
