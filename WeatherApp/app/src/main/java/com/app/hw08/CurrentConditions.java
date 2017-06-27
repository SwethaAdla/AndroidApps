package com.app.hw08;

/* Group 02 - Lakshmi Sridhar, Swetha Adla
   Homework 08
   CurrentConditions.java
 */

public class CurrentConditions {
    String cityKey;
    String weatherText;
    String weatherIconUrl;
    String tempCel;
    String TempFarh;
    String localObsDateTime;

    public String getCityKey() {
        return cityKey;
    }

    public void setCityKey(String cityKey) {
        this.cityKey = cityKey;
    }

    public String getLocalObsDateTime() {
        return localObsDateTime;
    }

    public void setLocalObsDateTime(String localObsDateTime) {
        this.localObsDateTime = localObsDateTime;
    }

    public String getWeatherText() {
        return weatherText;
    }

    public void setWeatherText(String weatherText) {
        this.weatherText = weatherText;
    }

    public String getWeatherIconUrl() {
        return weatherIconUrl;
    }

    public void setWeatherIconUrl(String weatherIconUrl) {
        this.weatherIconUrl = weatherIconUrl;
    }

    public String getTempCel() {
        return tempCel;
    }

    public void setTempCel(String tempCel) {
        this.tempCel = tempCel;
    }

    public String getTempFarh() {
        return TempFarh;
    }

    public void setTempFarh(String tempFarh) {
        TempFarh = tempFarh;
    }

    @Override
    public String toString() {
        return "CurrentConditions{" +
                "cityKey='" + cityKey + '\'' +
                ", localObsDateTime='" + localObsDateTime + '\'' +
                ", weatherText='" + weatherText + '\'' +
                ", weatherIconUrl='" + weatherIconUrl + '\'' +
                ", tempCel='" + tempCel + '\'' +
                ", TempFarh='" + TempFarh + '\'' +
                '}';
    }

}
