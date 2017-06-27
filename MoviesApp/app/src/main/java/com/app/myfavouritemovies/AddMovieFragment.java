package com.app.myfavouritemovies;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AddMovieFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AddMovieFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddMovieFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    Spinner spinner;
    String rating;
    String genre;
    public AddMovieFragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        spinner = (Spinner) getView().findViewById(R.id.spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.genreList, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        SeekBar seekBar = (SeekBar) getView().findViewById(R.id.seekBar);
        seekBar.setMax(5);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int step = 1;
            int min = 0;
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int value =  min + (progress * step);
                rating = String.valueOf(value);
                Log.d("Rating",rating);
                TextView seekBarvalueView = (TextView) getView().findViewById(R.id.textViewSeekBarValue);
                seekBarvalueView.setText(rating);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        getView().findViewById(R.id.buttonAdd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText movieNameView = (EditText) getView().findViewById(R.id.editTextName);
                String moviename = movieNameView.getText().toString();
                Log.d("Movie Name",moviename);
                EditText descptnView = (EditText) getView().findViewById(R.id.editTextDescptn);
                String desc = descptnView.getText().toString();
                Log.d("Description",desc);
                EditText yearView = (EditText) getView().findViewById(R.id.editTextYear);
                String year = yearView.getText().toString();
                Log.d("Year",year);
                EditText imdbView = (EditText) getView().findViewById(R.id.editTextImdb);
                String imdb =imdbView.getText().toString();
                Log.d("imdb",imdb);

                spinner = (Spinner) getView().findViewById(R.id.spinner);
                genre =spinner.getSelectedItem().toString();
                Log.d("genre", genre);

                if(isNullOrBlank(moviename)||isNullOrBlank(desc) || isNullOrBlank(genre)
                        || isNullOrBlank(rating)||isNullOrBlank(year)|| isNullOrBlank(imdb)){
                    Toast.makeText(getContext(),"Please enter all the fields",Toast.LENGTH_SHORT).show();
                    return;
                    //mListener.addMovie(null);
                }else{
                    Movie movieToAdd = new Movie(moviename,desc,genre,imdb,Integer.parseInt(year),Double.parseDouble(rating));
                    Log.d("Movie", movieToAdd.toString());
                    mListener.addMovie(movieToAdd);
                   /* Intent intent = new Intent();
                    intent.putExtra(MainActivity.MOVIE_KEY,movieToAdd );
                    setResult(RESULT_OK, intent);*/
                }
                getFragmentManager().beginTransaction()
                        .replace(R.id.activity_main, new MainFragment(), "tag_mainFragment")
                        .commit();
                //finish();
            }
        });
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddMovieFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddMovieFragment newInstance(String param1, String param2) {
        AddMovieFragment fragment = new AddMovieFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_movie, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void addMovie(Movie movie);
    }

    private static boolean isNullOrBlank(String s)
    {

        return (s==null || s.trim().equals(""));
    }
}
