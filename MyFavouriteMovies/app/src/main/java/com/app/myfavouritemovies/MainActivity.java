package com.app.myfavouritemovies;
/* Homework 02
   MainActivity.java
   Group 02 - Lakshmi Sridhar, Swetha Adla
*/
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Parcelable;
import android.support.annotation.IdRes;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.app.myfavouritemovies.Movie;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    final static String MOVIELIST_KEY = "MoviesList";
    final static String MOVIELIST_KEY2 = "MoviesList2";
    final static String MOVIE_KEY = "Movie";
    final static String MOVIE_KEY2 = "movie";
    final static String ITEMS_KEY = "items";
    final static int REQ_CODE = 100;
    final static int REQ_CODE2 = 200;
    List<Movie> moviesList =new ArrayList<Movie>();
    int index = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        findViewById(R.id.buttonEdit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final CharSequence[] items = getItems();
                if(moviesList.size() != 0) {

                    builder.setTitle("Pick a Movie")
                            .setItems(items, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    index = which;
                                    String selectedMovie = (String) moviesList.get(which).getName();
                                    int k = 0;
                                    for (k = 0; k < moviesList.size(); k++) {
                                        if (selectedMovie.equalsIgnoreCase(moviesList.get(k).getName())) {
                                            break;
                                        } else {
                                            continue;
                                        }
                                    }

                                    Movie movie = moviesList.get(k);
                                    Intent editIntent = new Intent(MainActivity.this, EditMovieActivity.class);
                                    editIntent.putExtra(MOVIE_KEY2, movie);
                                    //editIntent.putExtra(MOVIELIST_KEY, SingletonArrayList.getInstance().getArrayList());
                                    startActivityForResult(editIntent, REQ_CODE2);

                                }
                            });
                    final AlertDialog alert = builder.create();
                    alert.show();
                }else{
                    Toast.makeText(getApplicationContext(), "No movies to Edit. Please add movies first.", Toast.LENGTH_SHORT).show();
                }

            }
        });

        final AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
        findViewById(R.id.buttonDeleteMovie).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (moviesList.size() == 0) {
                    Toast.makeText(getApplicationContext(), "No movies to Delete. Please add movies first.", Toast.LENGTH_SHORT).show();
                } else {
                    final CharSequence[] items2 = getItems();
                    if (items2 != null) {

                        builder2.setTitle("Pick a Movie")
                                .setItems(items2, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        String toBeRemovedMovie = moviesList.get(which).getName().toString();
                                        moviesList.remove(which);

                                        Log.d("demo", "deleted movie");
                                        Log.d("demo", "size after :" + moviesList.size());
                                        for (Movie m : moviesList) {
                                            Log.d("demo", "Movie names :" + m.getName());
                                        }
                                        Toast.makeText(getApplicationContext(), "Movie " + toBeRemovedMovie + " is deleted successfully.", Toast.LENGTH_SHORT).show();
                                    }
                                });

                        final AlertDialog alert2 = builder2.create();

                        alert2.show();
                    }
                }
            }
        });


        findViewById(R.id.buttonAddMovie).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addIntent = new Intent(MainActivity.this, AddMovieActivity.class);
                startActivityForResult(addIntent, REQ_CODE);

            }
        });

        findViewById(R.id.buttonShowByRating).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(moviesList.size()==0){
                    Toast.makeText(getApplicationContext(), "No movies to show. Please add movies first.", Toast.LENGTH_SHORT).show();
                }
                else {
                    Intent movieByRatingIntent = new Intent("com.app.myfavouritemovies.intent.action.VIEW");
                    movieByRatingIntent.addCategory(Intent.CATEGORY_DEFAULT);
                    movieByRatingIntent.putExtra(MOVIELIST_KEY, (Serializable) moviesList);
                    startActivity(movieByRatingIntent);
                }
            }
        });


        findViewById(R.id.buttonShowByYear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(moviesList.size()==0){
                    Toast.makeText(getApplicationContext(), "No movies to show. Please add movies first.", Toast.LENGTH_SHORT).show();
                }
                else {
                    Intent moviebyYearIntent = new Intent("com.app.myfavouritemovies.intent.action.YEAR");
                    moviebyYearIntent.addCategory(Intent.CATEGORY_DEFAULT);
                    moviebyYearIntent.putExtra(MOVIELIST_KEY2, (Serializable) moviesList);
                    startActivity(moviebyYearIntent);
                }
            }
        });

        }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQ_CODE) {
            if (resultCode == RESULT_CANCELED) {
                Log.d("Result", "Result Cancelled");
                Toast.makeText(getApplicationContext(), "Some fields are missing, Please fill all the fields", Toast.LENGTH_SHORT).show();
            } else if (resultCode == RESULT_OK) {
                Movie movieToAdd = data.getParcelableExtra(MOVIE_KEY);
                moviesList.add(movieToAdd);
                Toast.makeText(getApplicationContext(), "Movie added successfully.", Toast.LENGTH_SHORT).show();
                Log.d("Result", "Result OK");
                for (Movie movie : moviesList
                        ) {
                    Log.d("MoviesList", movie.toString());
                }
            }
        }
        if (requestCode == REQ_CODE2) {
            if (resultCode == RESULT_CANCELED) {
                Log.d("Result", "Movie is not edited");
                Toast.makeText(getApplicationContext(), "Some fields are missing, Please fill all the fields", Toast.LENGTH_SHORT).show();
            } else if (resultCode == RESULT_OK) {
                Movie movieToedit = data.getParcelableExtra(MOVIE_KEY);
                moviesList.set(index, movieToedit);
                Toast.makeText(getApplicationContext(), "Movie edited successfully", Toast.LENGTH_SHORT).show();
                Log.d("Result", "Result OK");
                for (Movie movie : moviesList
                        ) {
                    Log.d("MoviesList", movie.toString());
                }
            }

        }
    }

    public CharSequence[] getItems(){
        CharSequence[] items = new CharSequence[moviesList.size()];
        for (int i = 0; i < moviesList.size(); i++) {
            items[i] = moviesList.get(i).getName();
            Log.d("demo","CharSequece items"+items[i]);
        }
        return items;
    }
}
