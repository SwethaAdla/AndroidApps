package com.app.hw06;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;



public class GetAppsAsyncTask extends AsyncTask<RequestParams,Void,ArrayList<App>> {
    IApp iApp;
    MainActivity activity;
    ProgressDialog progressDialog;

    public GetAppsAsyncTask(MainActivity activity){
        this.activity = activity;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage("Loading");
        progressDialog.setCancelable(false);
        progressDialog.show();

    }

    public GetAppsAsyncTask(IApp iApp) {
        this.iApp = iApp;
    }

    @Override
    protected ArrayList<App> doInBackground(RequestParams... params) {
        BufferedReader reader=null;
        StringBuilder sb = null;
        try {
            HttpsURLConnection con = params[0].setUpConnection();
            con.connect();
            int status = con.getResponseCode();
            if(status==HttpsURLConnection.HTTP_OK){
                reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                sb = new StringBuilder();
                String line = "";
                while((line=reader.readLine())!=null){
                    sb.append(line);
                }
                Log.d("demo","This is the full string"+sb.toString());
                return AppUtil.AppJSONParser.parseApp(sb.toString());
            }else{
                Log.d("demo","response code is "+String.valueOf(status));
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }finally {
            if(reader!=null){
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        Log.d("demo","in doInBackground method. returning null");
        return null;
    }


    @Override
    protected void onPostExecute(ArrayList<App> apps) {
        super.onPostExecute(apps);
        if(apps!=null){
            Log.d("demo", String.valueOf(apps.size()));
            activity.compareLists(apps);
            activity.loadApps(apps);
        }else{
            Log.d("demo","apps size is null in on post execute method");
        }
        progressDialog.dismiss();
    }


    static public interface IApp{
        public void loadApps(ArrayList<App> apps);
        public void compareLists(ArrayList<App> apps);
    }

}
