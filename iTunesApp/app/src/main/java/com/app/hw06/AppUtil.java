package com.app.hw06;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AppUtil {
    static public class AppJSONParser {
        static ArrayList<App> parseApp(String in) throws JSONException {
            ArrayList<App> appsList = new ArrayList<App>();
            JSONObject root = new JSONObject(in);
            JSONObject feedObj = root.getJSONObject("feed");
            JSONArray appArray = feedObj.getJSONArray("entry");
            for (int i=0; i<appArray.length(); i++){
                JSONObject appJSONObj = appArray.getJSONObject(i);
                App app = App.createApp(appJSONObj);
                appsList.add(app);
            }
            return appsList;
        }
    }
}
