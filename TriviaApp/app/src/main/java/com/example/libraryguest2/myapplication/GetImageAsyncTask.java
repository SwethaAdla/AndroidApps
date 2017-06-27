package com.example.libraryguest2.myapplication;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Window;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;

/* Homework 04
   GetImageAsyncTask.java
   Group 02- Lakshmi Sridhar, Swetha Adla
   */

public class GetImageAsyncTask extends AsyncTask<String,Void,Bitmap>{
    TriviaActvity actvity;
    //ProgressDialog progressDialog;

    ProgressBar progressBar;
    ImageView iv;
    public GetImageAsyncTask(TriviaActvity actvity){
        this.actvity = actvity;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        iv = (ImageView) actvity.findViewById(R.id.imageView2);
        progressBar = (ProgressBar) actvity.findViewById(R.id.progressBar);
       /* progressDialog.setMessage("Loading Image..");
        progressDialog.setCancelable(false);

        progressDialog.show();*/
        progressBar.setVisibility(View.VISIBLE);
        iv.setVisibility(View.INVISIBLE);

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
        }finally {
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
        // progressDialog.dismiss();
        if(s != null){
            ImageView img = (ImageView)actvity.findViewById(R.id.imageView2);
            img.setImageBitmap(s);
            img.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.INVISIBLE);
        }else if(s== null){
            Log.d("result image", "Null result");
        }
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

}
