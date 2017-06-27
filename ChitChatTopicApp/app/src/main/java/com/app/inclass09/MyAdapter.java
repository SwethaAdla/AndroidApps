package com.app.inclass09;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;
import org.ocpsoft.prettytime.PrettyTime;

import java.io.IOException;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MyAdapter extends ArrayAdapter<Msg> {
    ArrayList<Msg> mData;
    Context mContext;
    int mResource;
    LastActivity activity;
    public MyAdapter(Context context, int resource, ArrayList<Msg> objects, LastActivity activity) {
        super(context, resource, objects);
        this.mContext = context;
        this.mData = objects;
        this.mResource = resource;
        this.activity = activity;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView==null){
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(mResource,parent,false);
        }

        final Msg msg = mData.get(position);

        TextView appDetails = (TextView) convertView.findViewById(R.id.TextViewMessageDetails);
        TextView myAppDetails = (TextView) convertView.findViewById(R.id.textViewMsgpost);
        PrettyTime p = new PrettyTime();
        Date date = null;
        /*try {
           // date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(msg.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }*/
        if(activity.userEmail.equalsIgnoreCase(msg.getEmailid())) {
            myAppDetails.setText(msg.getFname() + " " + msg.getLname() + "\n" + msg.getMsg() + "\n" + msg.getTime());
            appDetails.setVisibility(View.INVISIBLE);
            myAppDetails.setVisibility(View.VISIBLE);
        }else {
            appDetails.setText(msg.getFname() + " " + msg.getLname() + "\n" + msg.getMsg() + "\n" + msg.getTime());
            appDetails.setVisibility(View.VISIBLE);
            myAppDetails.setVisibility(View.INVISIBLE);
        }
        return convertView;
    }

    public static String getDate(long milliSeconds, String dateFormat)
    {
        PrettyTime p = new PrettyTime();
        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return p.format(new Date(formatter.format(calendar.getTime())));
    }
}

