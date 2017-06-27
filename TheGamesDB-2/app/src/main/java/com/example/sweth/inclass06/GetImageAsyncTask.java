package com.example.sweth.inclass06;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class GetImageAsyncTask extends AsyncTask<String,Void,Bitmap> {
    GetGameActivity actvity;
    MainActivity mainActivity;
    ProgressDialog progressDialog;
    ImageView iv;
    public GetImageAsyncTask(GetGameActivity actvity){
        this.actvity = actvity;
    }
    public GetImageAsyncTask(MainActivity actvity){
        this.mainActivity = actvity;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = new ProgressDialog(actvity);
        progressDialog.setCancelable(false);
        progressDialog.show();

    }
    @Override
    protected Bitmap doInBackground(String... params) {
        InputStream is = null;
        try {
            URL url = new URL(params[0]);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            is = con.getInputStream();
            Bitmap image = BitmapFactory.decodeStream(is);
            return image;
        }catch (MalformedURLException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        } finally {
            if(is!=null){
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return null;
    }

    @Override
    protected void onPostExecute(Bitmap s) {
        super.onPostExecute(s);
        progressDialog.dismiss();
        if(s != null){
            ImageView img = (ImageView)actvity.findViewById(R.id.GameImage);
            img.setImageBitmap(s);
            img.setVisibility(View.VISIBLE);
        }else if(s== null){
            Log.d("result image", "Null result");
        }
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }
}

