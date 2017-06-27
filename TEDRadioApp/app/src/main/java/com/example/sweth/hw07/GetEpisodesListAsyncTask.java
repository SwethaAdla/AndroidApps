package com.example.sweth.hw07;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;


public class GetEpisodesListAsyncTask extends AsyncTask<String, Void, ArrayList<Episode>> {
    MainActivity activity;
    ProgressDialog progressDialog;

    public GetEpisodesListAsyncTask(MainActivity activity) {
        this.activity = activity;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage("Loading Episodes...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    @Override
    protected ArrayList<Episode> doInBackground(String... params) {

        try {
            URL url = new URL(params[0]);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            String redirect = con.getHeaderField("Location");
            Log.d("demo","Connecting to "+url.toString());
            con.setRequestMethod("GET");
            if (redirect != null){
                con = (HttpURLConnection) new URL(redirect).openConnection();
            }
            con.connect();
            InputStream in = con.getInputStream();
            Log.d("demo","Input Stream "+in.toString());
            return EpisodeUtil.EpisodeListPullParser.parseEpisode(in);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(ArrayList<Episode> episodes) {
        super.onPostExecute(episodes);
       progressDialog.dismiss();
        if(episodes != null){
            Log.d("demo", String.valueOf(episodes.size()));
            activity.showEpisodeslist(episodes);
        }else{
            Log.d("demo","episodes is null");
        }
    }

}
