package com.app.thegamesdb;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by sweth on 2/17/2017.
 */

public class GetSimilarGameAsyncTask extends AsyncTask<RequestParams, Void, Game>{

    SimilargamesActivity simActivity;
    ProgressDialog progressDialog;

    public GetSimilarGameAsyncTask(SimilargamesActivity simActivity) {
        this.simActivity = simActivity;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = new ProgressDialog(simActivity);
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
                Log.d("demo","the connection is good"+statusCode);
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
            Log.d("demo","sending game to output activity"+game.toString());
            simActivity.ShowSimilarGames(game);
        }

    }
}
