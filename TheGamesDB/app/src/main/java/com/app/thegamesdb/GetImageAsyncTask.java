package com.app.thegamesdb;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by sweth on 2/17/2017.
 */

public class GetImageAsyncTask extends AsyncTask<String,Void,Bitmap> {
    GetGameActivity actvity;
    ProgressDialog progressDialog;
    ImageView iv;
    public GetImageAsyncTask(GetGameActivity actvity){
        this.actvity = actvity;
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
            ImageView img = (ImageView)actvity.findViewById(R.id.imageView);
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
