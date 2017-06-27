package com.app.myfavouritemovies;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MainFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainFragment extends Fragment implements  ShowByRatingFragment.OnFragmentInteractionListener, ShowByYearFragment.OnFragmentInteractionListener{
    int index = 0;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
ArrayList<Movie> moviesList ;
    private OnFragmentInteractionListener mListener;

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
       final MainActivity activity = (MainActivity) getActivity();
        moviesList = new ArrayList<Movie>(activity.moviesList);
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        getView().findViewById(R.id.buttonEdit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final CharSequence[] items = getItems();
                if(moviesList.size() != 0) {

                    builder.setTitle("Pick a Movie")
                            .setItems(items, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    index = which;
                                    activity.setIndex(index);
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
                                    activity.setEditMovieObj(movie);
                                    getFragmentManager().beginTransaction()
                                            .replace(R.id.activity_main, new EditMovieFragment(), "tag_editMovieFragment")
                                            .addToBackStack(null)
                                            .commit();
                                }
                            });
                    final AlertDialog alert = builder.create();
                    alert.show();
                }else{
                    Toast.makeText(getContext(), "No movies to Edit. Please add movies first.", Toast.LENGTH_SHORT).show();
                }

            }
        });

        final AlertDialog.Builder builder2 = new AlertDialog.Builder(getContext());
        getView().findViewById(R.id.buttonDeleteMovie).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (moviesList.size() == 0) {
                    Toast.makeText(getContext(), "No movies to Delete. Please add movies first.", Toast.LENGTH_SHORT).show();
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
                                        Toast.makeText(getContext(), "Movie " + toBeRemovedMovie + " is deleted successfully.", Toast.LENGTH_SHORT).show();
                                    }
                                });

                        final AlertDialog alert2 = builder2.create();

                        alert2.show();
                    }
                }
            }
        });


        getView().findViewById(R.id.buttonAddMovie).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction()
                        .replace(R.id.activity_main, new AddMovieFragment(), "tag_addMovieFragment")
                        .addToBackStack(null)
                        .commit();

            }
        });

        getView().findViewById(R.id.buttonShowByRating).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(moviesList.size()==0){
                    Toast.makeText(getContext(), "No movies to show. Please add movies first.", Toast.LENGTH_SHORT).show();
                }
                else {
                    getFragmentManager().beginTransaction()
                            .replace(R.id.activity_main, new ShowByRatingFragment(), "tag_ratingListFragment")
                            .addToBackStack(null)
                            .commit();
                }
            }
        });


        getView().findViewById(R.id.buttonShowByYear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(moviesList.size()==0){
                    Toast.makeText(getContext(), "No movies to show. Please add movies first.", Toast.LENGTH_SHORT).show();
                }
                else {
                   getFragmentManager().beginTransaction()
                           .replace(R.id.activity_main, new ShowByYearFragment(), "tag_yearListFragment")
                           .addToBackStack(null)
                           .commit();
                }
            }
        });

        super.onActivityCreated(savedInstanceState);
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MainFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MainFragment newInstance(String param1, String param2) {
        MainFragment fragment = new MainFragment();
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
        return inflater.inflate(R.layout.fragment_main, container, false);
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

    public CharSequence[] getItems(){
        CharSequence[] items = new CharSequence[moviesList.size()];
        for (int i = 0; i < moviesList.size(); i++) {
            items[i] = moviesList.get(i).getName();
            Log.d("demo","CharSequece items"+items[i]);
        }
        return items;
    }

}
