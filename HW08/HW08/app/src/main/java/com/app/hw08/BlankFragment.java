package com.app.hw08;
/* Group 02 - Lakshmi Sridhar, Swetha Adla
   Homework 08
   BlankFragment.java
 */

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ParseException;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.ocpsoft.prettytime.PrettyTime;
import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BlankFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BlankFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BlankFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public BlankFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BlankFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BlankFragment newInstance(String param1, String param2) {
        BlankFragment fragment = new BlankFragment();
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
        return inflater.inflate(R.layout.fragment_blank, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            // mListener.onFragmentInteraction(uri);
        }
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
        // TODO: Update argument type and name
        //void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        MainActivity activity = (MainActivity) getActivity();
        final String DEGREE  = "\u00b0";
        Log.d("demo","degree symbol is "+DEGREE);
        String newWeatherIconUrl;
        if(activity.cityInfo != null) {
            TextView cityState = (TextView) getView().findViewById(R.id.textViewCityName);
            cityState.setVisibility(View.VISIBLE);

            cityState.setText(activity.cityInfo.getCityName() + ", " + activity.cityInfo.getCountry());

            TextView status = (TextView) getView().findViewById(R.id.textViewstatus);
            status.setVisibility(View.VISIBLE);
            status.setText(activity.currentCityConditions.getWeatherText());
            ImageView iv = (ImageView) getView().findViewById(R.id.imageViewstatus);
            iv.setVisibility(View.VISIBLE);

            if ((Integer.parseInt(activity.currentCityConditions.getWeatherIconUrl())) < 10) {
                newWeatherIconUrl = "0" + activity.currentCityConditions.getWeatherIconUrl();
            } else {
                newWeatherIconUrl = activity.currentCityConditions.getWeatherIconUrl();
            }
            Picasso.with(getContext()).load("http://developer.accuweather.com/sites/default/files/" + newWeatherIconUrl + "-s.png").into(iv);

            TextView temp = (TextView) getView().findViewById(R.id.textViewTemperature);
            temp.setVisibility(View.VISIBLE);

            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
            String tempUnit = sp.getString("pref_tempUnit", "");
            Log.d("demo", tempUnit);
            if (tempUnit.equalsIgnoreCase("Celcius")) {


                temp.setText("Temperature : " + activity.currentCityConditions.getTempCel() +DEGREE+"C");
            } else {
                temp.setText("Temperature : " + activity.currentCityConditions.getTempFarh()+DEGREE + "F");
            }

            Date dt = null;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
            try {
                dt = sdf.parse(activity.currentCityConditions.getLocalObsDateTime());
            } catch (Exception e) {
                e.printStackTrace();
            }

            PrettyTime p = new PrettyTime();
            TextView timeLapsed = (TextView) getView().findViewById(R.id.textViewLastUpdated);
            timeLapsed.setVisibility(View.VISIBLE);
            timeLapsed.setText("Updated " + p.format(dt));
        }

    }
}