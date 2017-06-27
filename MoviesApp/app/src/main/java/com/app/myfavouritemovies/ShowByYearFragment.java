package com.app.myfavouritemovies;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;



public class ShowByYearFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    ArrayList<Movie> moviesList;
    TextView titleView;
    TextView descView;
    TextView genreView;
    TextView yearView;
    TextView ratingView;
    TextView imdbView;
    int position;

    public ShowByYearFragment() {
        // Required empty public constructor
    }

    public static ShowByYearFragment newInstance(String param1, String param2) {
        ShowByYearFragment fragment = new ShowByYearFragment();
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
        return inflater.inflate(R.layout.fragment_show_by_year, container, false);
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
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        MainActivity activity = (MainActivity) getActivity();
        moviesList = new ArrayList<Movie>(activity.getMoviesList());

        Collections.sort(moviesList, new Comparator<Movie>() {
            @Override
            public int compare(Movie o1, Movie o2) {
                return o1.getYear()-o2.getYear();
            }

        });

        position = 0;

        titleView = (TextView)getView().findViewById(R.id.textViewTitleVal);
        descView = (TextView)getView().findViewById(R.id.textViewDescVal);
        genreView = (TextView)getView().findViewById(R.id.textViewGenreVal);
        yearView = (TextView)getView().findViewById(R.id.textViewYearVal);
        ratingView = (TextView)getView().findViewById(R.id.textViewRatingVal);
        imdbView = (TextView)getView().findViewById(R.id.textViewImdbVal);


        titleView.setText(moviesList.get(0).getName());
        descView.setText(moviesList.get(0).getDescription());
        genreView.setText(moviesList.get(0).getGenre());
        yearView.setText(String.valueOf(moviesList.get(0).getYear()));
        ratingView.setText(String.valueOf(moviesList.get(0).getRating()));
        imdbView.setText(moviesList.get(0).getImdb());

        getView().findViewById(R.id.imageButtonFirst).setOnClickListener(new View.OnClickListener() {
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

        getView().findViewById(R.id.imageButtonlast).setOnClickListener(new View.OnClickListener() {
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

        getView().findViewById(R.id.imageButtonPrevious).setOnClickListener(new View.OnClickListener() {
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

        getView().findViewById(R.id.imageButtonNext).setOnClickListener(new View.OnClickListener() {
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
        getView().findViewById(R.id.buttonFinish).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction()
                        .replace(R.id.activity_main, new MainFragment(), "tag_mainFragment")
                        .commit();
                //  finish();
            }
        });

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
}
