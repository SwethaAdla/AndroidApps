package com.example.sweth.inclass06;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

import javax.net.ssl.HttpsURLConnection;


public class GetGameAsyncTask  extends AsyncTask<RequestParams, Void, Game> {

    GetGameActivity gameActivity;
    //SimilargamesActivity simActivity;
    ProgressDialog progressDialog;

    public GetGameAsyncTask(GetGameActivity activity) {
        this.gameActivity = activity;
    }

    /*public GetGameAsyncTask(SimilargamesActivity simActivity) {
        this.simActivity = simActivity;
    }*/



    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = new ProgressDialog(gameActivity);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }


    @Override
    protected Game doInBackground(RequestParams... params) {
        try {
            HttpURLConnection con = params[0].setUpConnection();
            con.connect();
            int statusCode = con.getResponseCode();
            if(statusCode == HttpsURLConnection.HTTP_OK){
                InputStream in = con.getInputStream();
                return GamesUtil.GamePullParser.parseGame(in);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Game game) {
        super.onPostExecute(game);
        progressDialog.dismiss();
        if(game!=null) {
            Log.d("demo",game.toString());
            gameActivity.showGame(game);
        }

    }
}

