package com.app.hw08;

/* Group 02 - Lakshmi Sridhar, Swetha Adla
   Homework 08
   FiveDayForecast.java
 */

public class FiveDayForecast {

    String minTemp, maxTemp, dayIcon, nightIcon, dayPhrase, nightPhrase, detaliUrl, forecastDate;

    public String getForecastDate() {
        return forecastDate;
    }

    public void setForecastDate(String forecastDate) {
        this.forecastDate = forecastDate;
    }

    public String getDetaliUrl() {
        return detaliUrl;
    }

    public void setDetaliUrl(String detaliUrl) {
        this.detaliUrl = detaliUrl;
    }

    public String getMinTemp() {
        return minTemp;
    }

    public void setMinTemp(String minTemp) {
        this.minTemp = minTemp;
    }

    public String getMaxTemp() {
        return maxTemp;
    }

    public void setMaxTemp(String maxTemp) {
        this.maxTemp = maxTemp;
    }

    public String getDayIcon() {
        return dayIcon;
    }

    public void setDayIcon(String dayIcon) {
        this.dayIcon = dayIcon;
    }

    public String getNightIcon() {
        return nightIcon;
    }

    public void setNightIcon(String nightIcon) {
        this.nightIcon = nightIcon;
    }

    public String getDayPhrase() {
        return dayPhrase;
    }

    public void setDayPhrase(String dayPhrase) {
        this.dayPhrase = dayPhrase;
    }

    public String getNightPhrase() {
        return nightPhrase;
    }

    public void setNightPhrase(String nightPhrase) {
        this.nightPhrase = nightPhrase;
    }

    @Override
    public String toString() {
        return "FiveDayForecast{" +
                "minTemp='" + minTemp + '\'' +
                ", maxTemp='" + maxTemp + '\'' +
                ", dayIcon='" + dayIcon + '\'' +
                ", nightIcon='" + nightIcon + '\'' +
                ", dayPhrase='" + dayPhrase + '\'' +
                ", nightPhrase='" + nightPhrase + '\'' +
                ", detaliUrl='" + detaliUrl + '\'' +
                ", forecastDate='" + forecastDate + '\'' +
                '}';
    }
}
