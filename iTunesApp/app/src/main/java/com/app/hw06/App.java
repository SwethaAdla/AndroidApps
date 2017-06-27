package com.app.hw06;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;


public class App implements Serializable {
    String appName, appPrice ,imageUrl;
    boolean isFavourite =false;

    public boolean isFavourite() {
        return isFavourite;
    }

    public void setFavourite(boolean favourite) {
        isFavourite = favourite;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAppPrice() {
        return appPrice;
    }

    public void setAppPrice(String appPrice) {
        this.appPrice = appPrice;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public String toString() {
        return "App{" +
                "appName='" + appName + '\'' +
                ", appPrice='" + appPrice + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }

    public static App createApp(JSONObject jsonObject) throws JSONException {
        App app = new App();
        app.setAppName(jsonObject.getJSONObject("im:name").getString("label"));
        JSONArray als = jsonObject.getJSONArray("im:image");
        app.setImageUrl(als.getJSONObject(0).getString("label"));
        app.setAppPrice(jsonObject.getJSONObject("im:price").getString("label"));

        return app;
    }
}
