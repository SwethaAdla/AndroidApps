package com.app.inclassassignment04;
/* In Class 04
    MainActivity.java
    Group 02- Lakshmi Sridhar, S
etha Adla*/
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements GetArticlesAsyncTask.INews {
    Spinner spinner;
    int position =0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spinner = (Spinner) findViewById(R.id.spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.newsSourceArr, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        //disable all the buttons in the bottom
        changeButtons(0);

        findViewById(R.id.buttonGetNews).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newsSource = (String) spinner.getSelectedItem();
                Log.d("selected news source", newsSource);
                RequestParams params = new RequestParams("https://newsapi.org/v1/articles", "GET");
                params.addParams("apiKey","f324497de55740708f2945dbe1f1a090");
                params.addParams("source", getNewsSrc(newsSource));
                new GetArticlesAsyncTask(MainActivity.this).execute(params);


            }
        });

    }

    public String getNewsSrc(String s) {

        if (s.equalsIgnoreCase("Buzzfeed")) {
            return "buzzfeed";
        } else if (s.equalsIgnoreCase("CNN")) {
            return "cnn";
        } else if (s.equalsIgnoreCase("BBC")) {
            return "bbc-news";
        } else if (s.equalsIgnoreCase("ESPN")) {
            return "espn";
        } else if (s.equalsIgnoreCase("Sky News")) {
            return "sky-news";
        } else {
            return "";
        }
    }

    @Override
    public void setUpScrollView(final ArrayList<Article> articles) {

        final LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linearLayout);
        ImageView imgView = (ImageView) findViewById(R.id.imageViewArticle);
       // final EditText editTxt = (EditText) findViewById(R.id.editTextMultiLine);
        final TextView tv = (TextView) findViewById(R.id.textViewMultilIne);
        changeButtons(1);
        position = 0;
        new GetImage().execute(articles.get(0).getUrlToImage());
        tv.setText("Title: "+ articles.get(0).getTitle() +"\nAuthor: "+ articles.get(0).getAuthor()+"\nPublished on: "+ articles.get(0).getPublishedAt()+"\nDescription: "+ articles.get(0).getDescription());

        findViewById(R.id.imageButtonFirst).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position =0;
                new GetImage().execute(articles.get(0).getUrlToImage());
                tv.setText("Title: "+ articles.get(0).getTitle() +"\nAuthor: "+ articles.get(0).getAuthor()+"\nPublished on: "+ articles.get(0).getPublishedAt()+"\nDescription: "+ articles.get(0).getDescription());
            }
        });

        findViewById(R.id.imageButtonlast).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position = articles.size()-1;
                new GetImage().execute(articles.get(position).getUrlToImage());
                tv.setText("Title: "+ articles.get(position).getTitle() +"\nAuthor: "+ articles.get(position).getAuthor()+"\nPublished on: "+ articles.get(position).getPublishedAt()+"\nDescription: "+ articles.get(position).getDescription());
            }
        });

        findViewById(R.id.imageButtonPrevious).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(position == 0){
                    position = 0;
                }else{
                    position = position-1;
                }
                new GetImage().execute(articles.get(position).getUrlToImage());
                tv.setText("Title: "+ articles.get(position).getTitle() +"\nAuthor: "+ articles.get(position).getAuthor()+"\nPublished on: "+ articles.get(position).getPublishedAt()+"\nDescription: "+ articles.get(position).getDescription());
               // linearLayout.addView(tv);
            }
        });

        findViewById(R.id.imageButtonNext).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(position == articles.size()-1){
                    position= articles.size()-1;
                }else{
                    position= position+1;
                }
                new GetImage().execute(articles.get(position).getUrlToImage());
                tv.setText("Title: "+ articles.get(position).getTitle() +"\nAuthor: "+ articles.get(position).getAuthor()+"\nPublished on: "+ articles.get(position).getPublishedAt()+"\nDescription: "+ articles.get(position).getDescription());
            }
        });
        findViewById(R.id.buttonFinish).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeButtons(0);
                finish();
               
            }
        });
    }


    class GetImage extends AsyncTask<String, Void, Bitmap> {
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

            if(s != null){
                //LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linearLayout);
                ImageView iv =
                        (ImageView) findViewById(R.id.imageViewArticle);
                iv.setImageBitmap(s);
                //linearLayout.addView(iv);
            }else if(s== null){
                Log.d("result image", "Null result");
            }
        }
    }

    public  void changeButtons(int i){
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linearLayout);
        TextView et = (TextView) findViewById(R.id.textViewMultilIne);
        ImageView imageView = (ImageView)findViewById(R.id.imageViewArticle);
        spinner = (Spinner) findViewById(R.id.spinner);
        ImageView ivFirst = (ImageView) findViewById(R.id.imageButtonFirst);
        ImageView ivLast = (ImageView) findViewById(R.id.imageButtonlast);
        ImageView ivNext = (ImageView) findViewById(R.id.imageButtonNext);
        ImageView ivPrev = (ImageView) findViewById(R.id.imageButtonPrevious);
        Button btnFinish = (Button) findViewById(R.id.buttonFinish);
        if(i==0) {
            Bitmap bm = null;
            et.setText("");
            imageView.setImageBitmap(bm);

            ivFirst.setEnabled(false);
            ivLast.setEnabled(false);
            ivNext.setEnabled(false);
            ivPrev.setEnabled(false);
            btnFinish.setEnabled(true);
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                    R.array.newsSourceArr, android.R.layout.simple_spinner_item);
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            // Apply the adapter to the spinner
            spinner.setAdapter(adapter);

        }else{
            ivFirst.setEnabled(true);
            ivLast.setEnabled(true);
            ivNext.setEnabled(true);
            ivPrev.setEnabled(true);
            btnFinish.setEnabled(true);
        }



    }
}
