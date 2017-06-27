package com.app.myfavouritemovies;
/* Homework 02
   EditMovieActivity.java
   Group 02 - Lakshmi Sridhar, Swetha Adla
*/
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

public class EditMovieActivity extends AppCompatActivity {
    ArrayList<Movie> moviesList;
    String rating;
    Movie m;
    Spinner editSpinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_movie);
        m = (Movie) getIntent().getExtras().get(MainActivity.MOVIE_KEY2);


        final EditText nameView= (EditText) findViewById(R.id.editTextName);
        final EditText descView = (EditText) findViewById(R.id.editTextDescptn);
        final EditText yearView= (EditText) findViewById(R.id.editTextYear);
        final EditText imdbView = (EditText) findViewById(R.id.editTextImdb);
        //final TextView selGenre = (TextView) findViewById(R.id.EditGenreView);
        editSpinner = (Spinner)findViewById(R.id.spinner2);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.genreList, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        editSpinner.setAdapter(adapter);
        int spinnerPosition = adapter.getPosition(m.getGenre());
        //set the default according to value
        editSpinner.setSelection(spinnerPosition);
        SeekBar seekBar = (SeekBar) findViewById(R.id.seekBar);
        final TextView seekBarvalueView = (TextView) findViewById(R.id.textViewSeekBarValue);

        nameView.setText(m.getName().toString());
        descView.setText(m.getDescription().toString());
        yearView.setText(String.valueOf(m.getYear()));
        imdbView.setText(m.getImdb().toString());
        seekBarvalueView.setText(String.valueOf(m.getRating()));
        seekBar.setProgress((int) m.getRating());

        seekBar.setMax(5);        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int step = 1;
            int min = 0;
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int value =  min + (progress * step);

                rating = String.valueOf(value);
                Log.d("Rating",rating);
                TextView seekBarvalueView = (TextView) findViewById(R.id.textViewSeekBarValue);
                seekBarvalueView.setText(rating);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        findViewById(R.id.buttonSave).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String moviename = nameView.getText().toString();
                Log.d("demo","edited name "+moviename);
                String desc = descView.getText().toString();
                Log.d("demo","edited desc "+desc);
                String year = yearView.getText().toString();
                Log.d("demo","edited year "+year);
                String imdb =imdbView.getText().toString();
                Log.d("demo","edited imdb "+imdb);

                editSpinner = (Spinner) findViewById(R.id.spinner2);
                String genre =editSpinner.getSelectedItem().toString();
                Log.d("genre", genre);

                Log.d("demo","intent movie values "+m.getName()+m.getDescription()+m.getGenre()+m.getRating()+m.getYear()+m.getImdb());
                double ratingVal = Double.parseDouble(seekBarvalueView.getText().toString());

                if(isNullOrBlank(moviename)||isNullOrBlank(desc) || isNullOrBlank(genre)
                        || isNullOrBlank(seekBarvalueView.getText().toString())||isNullOrBlank(year)|| isNullOrBlank(imdb)){
                    Log.d("demo","in the null check condition");
                    setResult(RESULT_CANCELED);

                }else {
                    Movie editedMovie = new Movie(moviename, desc, genre, imdb, Integer.parseInt(year), ratingVal);
                    Log.d("Movie", editedMovie.toString());
                    Intent intent = new Intent();
                    intent.putExtra(MainActivity.MOVIE_KEY, editedMovie);
                    setResult(RESULT_OK, intent);
                }

                finish();
            }
        });


    }
    private static boolean isNullOrBlank(String s)
    {

        return (s==null || s.trim().equals(""));
    }
}
