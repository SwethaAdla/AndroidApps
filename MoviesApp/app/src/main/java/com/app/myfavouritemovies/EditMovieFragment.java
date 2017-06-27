package com.app.myfavouritemovies;

import android.content.Context;
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

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EditMovieFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EditMovieFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditMovieFragment extends Fragment {
    ArrayList<Movie> moviesList;
    String rating;
    Movie m;
    Spinner editSpinner;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public EditMovieFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EditMovieFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EditMovieFragment newInstance(String param1, String param2) {
        EditMovieFragment fragment = new EditMovieFragment();
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
        return inflater.inflate(R.layout.fragment_edit_movie, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final MainActivity activity = (MainActivity) getActivity();
        m = activity.getEditMovieObj();
        final EditText nameView= (EditText) getView().findViewById(R.id.editTextName);
        final EditText descView = (EditText) getView().findViewById(R.id.editTextDescptn);
        final EditText yearView= (EditText) getView().findViewById(R.id.editTextYear);
        final EditText imdbView = (EditText) getView().findViewById(R.id.editTextImdb);
        //final TextView selGenre = (TextView) findViewById(R.id.EditGenreView);
        editSpinner = (Spinner)getView().findViewById(R.id.spinner2);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.genreList, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        editSpinner.setAdapter(adapter);
        int spinnerPosition = adapter.getPosition(m.getGenre());
        //set the default according to value
        editSpinner.setSelection(spinnerPosition);
        SeekBar seekBar = (SeekBar) getView().findViewById(R.id.seekBar);
        final TextView seekBarvalueView = (TextView) getView().findViewById(R.id.textViewSeekBarValue);

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

        getView().findViewById(R.id.buttonSave).setOnClickListener(new View.OnClickListener() {
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

                editSpinner = (Spinner) getView().findViewById(R.id.spinner2);
                String genre =editSpinner.getSelectedItem().toString();
                Log.d("genre", genre);

                Log.d("demo","intent movie values "+m.getName()+m.getDescription()+m.getGenre()+m.getRating()+m.getYear()+m.getImdb());
                double ratingVal = Double.parseDouble(seekBarvalueView.getText().toString());

                if(isNullOrBlank(moviename)||isNullOrBlank(desc) || isNullOrBlank(genre)
                        || isNullOrBlank(seekBarvalueView.getText().toString())||isNullOrBlank(year)|| isNullOrBlank(imdb)){
                    Log.d("demo","in the null check condition");
                    Toast.makeText(getContext(), "Some fields are missing, Please fill all the fields", Toast.LENGTH_SHORT).show();
                    return;

                }else {
                    Movie editedMovie = new Movie(moviename, desc, genre, imdb, Integer.parseInt(year), ratingVal);
                    Log.d("Movie", editedMovie.toString());
                    Log.d("demo", String.valueOf(activity.getIndex()));
                    moviesList = (ArrayList<Movie>) activity.getMoviesList();
                    Log.d("demo", moviesList.toString());
                    moviesList.set(activity.getIndex(), editedMovie);
                    Toast.makeText(getContext(), "Movie edited successfully", Toast.LENGTH_SHORT).show();
                }
                getFragmentManager().beginTransaction()
                        .replace(R.id.activity_main, new MainFragment(), "tag_mainFragment")
                        .commit();
                //finish();
            }
        });


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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {

    }

    private static boolean isNullOrBlank(String s)
    {

        return (s==null || s.trim().equals(""));
    }
}
