package com.app.myfavouritemovies;

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


public class MainActivity extends AppCompatActivity
        implements MainFragment.OnFragmentInteractionListener, AddMovieFragment.OnFragmentInteractionListener,
        EditMovieFragment.OnFragmentInteractionListener, ShowByYearFragment.OnFragmentInteractionListener,ShowByRatingFragment.OnFragmentInteractionListener{

    public List<Movie> moviesList = new ArrayList<Movie>();
    public Movie editMovieObj = null;
    int index = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.activity_main, new MainFragment(), "tag_mainFragment")
                .addToBackStack(null)
                .commit();
    }


    public CharSequence[] getItems(){
        CharSequence[] items = new CharSequence[moviesList.size()];
        for (int i = 0; i < moviesList.size(); i++) {
            items[i] = moviesList.get(i).getName();
            Log.d("demo","CharSequece items"+items[i]);
        }
        return items;
    }

    @Override
    public void addMovie(Movie movie) {
        if(movie != null){
            moviesList.add(movie);
            Toast.makeText(getApplicationContext(), "Movie added successfully.", Toast.LENGTH_SHORT).show();

            for (Movie m : moviesList
                    ) {
                Log.d("MoviesList", movie.toString());
            }

        }else{
            Log.d("demo", "Add Movie Object is null");
            Toast.makeText(getApplicationContext(), "Some fields are missing, Please fill all the fields", Toast.LENGTH_SHORT).show();
        }
    }

    public List<Movie> getMoviesList() {
        return moviesList;
    }

    public void setMoviesList(List<Movie> moviesList) {
        this.moviesList = moviesList;
    }

    public Movie getEditMovieObj() {
        return editMovieObj;
    }

    public void setEditMovieObj(Movie editMovieObj) {
        this.editMovieObj = editMovieObj;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
