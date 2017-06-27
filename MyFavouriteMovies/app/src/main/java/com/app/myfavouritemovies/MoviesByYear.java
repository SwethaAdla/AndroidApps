package com.app.myfavouritemovies;
/* Homework 02
   MoviesByYear.java
   Group 02 - Lakshmi Sridhar, Swetha Adla
*/
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class MoviesByYear extends AppCompatActivity {
    ArrayList<Movie> moviesList;
    TextView titleView;
    TextView descView;
    TextView genreView;
    TextView yearView;
    TextView ratingView;
    TextView imdbView;
    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies_by_year);

        if(getIntent().getExtras() != null){
            moviesList = (ArrayList<Movie>) getIntent().getExtras().get(MainActivity.MOVIELIST_KEY2);
            Collections.sort(moviesList, new Comparator<Movie>() {
                @Override
                public int compare(Movie o1, Movie o2) {
                    return o1.getYear()-o2.getYear();
                }

            });

            position = 0;

            titleView = (TextView)findViewById(R.id.textViewTitleVal);
            descView = (TextView)findViewById(R.id.textViewDescVal);
            genreView = (TextView)findViewById(R.id.textViewGenreVal);
            yearView = (TextView)findViewById(R.id.textViewYearVal);
            ratingView = (TextView)findViewById(R.id.textViewRatingVal);
            imdbView = (TextView)findViewById(R.id.textViewImdbVal);


            titleView.setText(moviesList.get(0).getName());
            descView.setText(moviesList.get(0).getDescription());
            genreView.setText(moviesList.get(0).getGenre());
            yearView.setText(String.valueOf(moviesList.get(0).getYear()));
            ratingView.setText(String.valueOf(moviesList.get(0).getRating()));
            imdbView.setText(moviesList.get(0).getImdb());

            findViewById(R.id.imageButtonFirst).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    position = 0;
                    titleView.setText(moviesList.get(0).getName());
                    descView.setText(moviesList.get(0).getDescription());
                    genreView.setText(moviesList.get(0).getGenre());
                    yearView.setText(String.valueOf(moviesList.get(0).getYear()));
                    ratingView.setText(String.valueOf(moviesList.get(0).getRating()));
                    imdbView.setText(moviesList.get(0).getImdb());
                }
            });

            findViewById(R.id.imageButtonlast).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    position = moviesList.size()-1;
                    titleView.setText(moviesList.get(moviesList.size()-1).getName());
                    descView.setText(moviesList.get(moviesList.size()-1).getDescription());
                    genreView.setText(moviesList.get(moviesList.size()-1).getGenre());
                    yearView.setText(String.valueOf(moviesList.get(moviesList.size()-1).getYear()));
                    ratingView.setText(String.valueOf(moviesList.get(moviesList.size()-1).getRating()));
                    imdbView.setText(moviesList.get(moviesList.size()-1).getImdb());
                }
            });

            findViewById(R.id.imageButtonPrevious).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(position == 0){
                        position= 0;
                    }else{
                        position =position-1;
                    }
                    titleView.setText(moviesList.get(position).getName());
                    descView.setText(moviesList.get(position).getDescription());
                    genreView.setText(moviesList.get(position).getGenre());
                    yearView.setText(String.valueOf(moviesList.get(position).getYear()));
                    ratingView.setText(String.valueOf(moviesList.get(position).getRating()));
                    imdbView.setText(moviesList.get(position).getImdb());
                }
            });

            findViewById(R.id.imageButtonNext).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(position == moviesList.size()-1){
                        position= moviesList.size()-1;
                    }else{
                        position= position+1;
                    }
                    titleView.setText(moviesList.get(position).getName());
                    descView.setText(moviesList.get(position).getDescription());
                    genreView.setText(moviesList.get(position).getGenre());
                    yearView.setText(String.valueOf(moviesList.get(position).getYear()));
                    ratingView.setText(String.valueOf(moviesList.get(position).getRating()));
                    imdbView.setText(moviesList.get(position).getImdb());
                }
            });
            findViewById(R.id.buttonFinish).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }


    }
}
