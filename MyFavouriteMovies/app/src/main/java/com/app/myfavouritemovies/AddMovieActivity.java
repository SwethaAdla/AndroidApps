package com.app.myfavouritemovies;
/* Homework 02
   AddMovieActivity.java
   Group 02 - Lakshmi Sridhar, Swetha Adla
*/
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.FloatProperty;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class AddMovieActivity extends AppCompatActivity {

    Spinner spinner;
    String genre;
    String rating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_movie);

        spinner = (Spinner) findViewById(R.id.spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.genreList, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        SeekBar seekBar = (SeekBar) findViewById(R.id.seekBar);
        seekBar.setMax(5);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
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

        findViewById(R.id.buttonAdd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText movieNameView = (EditText) findViewById(R.id.editTextName);
                String moviename = movieNameView.getText().toString();
                Log.d("Movie Name",moviename);
                EditText descptnView = (EditText) findViewById(R.id.editTextDescptn);
                String desc = descptnView.getText().toString();
                Log.d("Description",desc);
                EditText yearView = (EditText) findViewById(R.id.editTextYear);
                String year = yearView.getText().toString();
                Log.d("Year",year);
                EditText imdbView = (EditText) findViewById(R.id.editTextImdb);
                String imdb =imdbView.getText().toString();
                Log.d("imdb",imdb);

                spinner = (Spinner) findViewById(R.id.spinner);
                genre =spinner.getSelectedItem().toString();
                Log.d("genre", genre);

                if(isNullOrBlank(moviename)||isNullOrBlank(desc) || isNullOrBlank(genre)
                        || isNullOrBlank(rating)||isNullOrBlank(year)|| isNullOrBlank(imdb)){
                    setResult(RESULT_CANCELED);
                }else{
                    Movie movieToAdd = new Movie(moviename,desc,genre,imdb,Integer.parseInt(year),Double.parseDouble(rating));
                    Log.d("Movie", movieToAdd.toString());
                    Intent intent = new Intent();
                    intent.putExtra(MainActivity.MOVIE_KEY,movieToAdd );
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
