package com.example.sweth.inclass06;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;


public class GetGamesListAsyncTask extends AsyncTask<RequestParams, Void, ArrayList<Game>>{
    MainActivity activity;
    ProgressDialog progressDialog;

    public GetGamesListAsyncTask(MainActivity activity) {
        this.activity = activity;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = new ProgressDialog(activity);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }


    @Override
    protected ArrayList<Game> doInBackground(RequestParams... params) {

        try {
            HttpURLConnection con = params[0].setUpConnection();
            con.connect();
            int statusCode = con.getResponseCode();
            if(statusCode == HttpsURLConnection.HTTP_OK){
                InputStream in = con.getInputStream();
                return GamesUtil.GamesListPullParser.parseGame(in);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(ArrayList<Game> games) {
        super.onPostExecute(games);
        progressDialog.dismiss();
        if(games != null){
            Log.d("demo", String.valueOf(games.size()));
            Log.d("demo",games.toString());
            activity.getGameslist(games);
        }
    }
}

