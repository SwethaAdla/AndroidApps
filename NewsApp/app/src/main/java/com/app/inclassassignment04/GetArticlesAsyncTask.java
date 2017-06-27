package com.app.inclassassignment04;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;



public class GetArticlesAsyncTask extends AsyncTask<RequestParams, Void, ArrayList<Article>> {

        INews inews;

    public GetArticlesAsyncTask(INews inews) {
        this.inews = inews;
    }

    @Override
    protected ArrayList<Article> doInBackground(RequestParams... params) {

        BufferedReader reader = null;
        StringBuilder sb = null;
        try {
            HttpsURLConnection con = params[0].setUpConnection();
            con.connect();
            int status = con.getResponseCode();
            Log.d("Response Code", String.valueOf(status));
            if(status == HttpsURLConnection.HTTP_OK){
                 reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                 sb = new StringBuilder();
                String line= "";
                while((line = reader.readLine()) != null){
                    sb.append(line);
                }
                Log.d("demo", sb.toString());
                return ArticlesUtil.articleJSONParser.parseArticles(sb.toString());
            }else{
                Log.d("Response Code", String.valueOf(con.getResponseCode()));
                return null;
            }


        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            if(reader != null){
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        Log.d("doINBackground", "returning null");
        return null;
    }

    @Override
    protected void onPostExecute(ArrayList<Article> articles) {
        super.onPostExecute(articles);
        if(articles != null){
            Log.d("articles", articles.toString());
            inews.setUpScrollView(articles);
        }
    }

static public interface INews{
    public void setUpScrollView(ArrayList<Article> articles);
}

}
