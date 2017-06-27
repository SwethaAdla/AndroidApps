package com.app.hw09;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;


public class FrnListAdapter extends ArrayAdapter<User> {
    Button sendBtn;
    Button acptBtn ;
    Button remvBtn;
    ArrayList<User> mData;
    Context mContext;
    int mResource;
    FriendsListActivity activity;


    public FrnListAdapter(Context context, int resource, ArrayList<User> objects, FriendsListActivity activity) {
        super(context, resource, objects);
        this.mContext = context;
        this.mData = objects;
        this.mResource = resource;
        this.activity =activity;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(mResource, parent, false);
        }
        sendBtn = (Button) convertView.findViewById(R.id.buttonSendFrnReq);
        acptBtn = (Button) convertView.findViewById(R.id.buttonAccptFrnReq);
        remvBtn = (Button) convertView.findViewById(R.id.buttonRemvFrn);
        final User user = mData.get(position);

        TextView userNameView = (TextView) convertView.findViewById(R.id.UserNameVw);
        userNameView.setText(user.getFname() + " " + user.getLname());
        if(activity.getFriendList() != null && activity.getFriendList().contains(user.getKey())){
            sendBtn.setVisibility(View.INVISIBLE);
            acptBtn.setVisibility(View.INVISIBLE);
            remvBtn.setVisibility(View.VISIBLE);
        }else if(activity.getSentReqList() != null && activity.getSentReqList().contains(user.getKey())){
            sendBtn.setVisibility(View.VISIBLE);
            sendBtn.setEnabled(false);
            acptBtn.setVisibility(View.INVISIBLE);
            remvBtn.setVisibility(View.INVISIBLE);
        }else if(activity.getRecevdReqList() != null && activity.getRecevdReqList().contains(user.getKey())){
            sendBtn.setVisibility(View.INVISIBLE);
            acptBtn.setVisibility(View.VISIBLE);
            remvBtn.setVisibility(View.INVISIBLE);
        }else {
            sendBtn.setVisibility(View.VISIBLE);
            acptBtn.setVisibility(View.INVISIBLE);
            remvBtn.setVisibility(View.INVISIBLE);
        }

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //update present user's sentReqList
                Firebase.setAndroidContext(activity.getApplicationContext());
                final Firebase firebase = new Firebase("https://hw09-a.firebaseio.com/");
                if(activity.getSentReqList() != null){
                    activity.getSentReqList().add(user.getKey());

                }else{
                    activity.sentReqList = new ArrayList<String>();
                    activity.sentReqList.add(user.getKey());
                }
                firebase.child("users").child(activity.present_user.getKey()).child("sentFrnReqList").setValue(activity.getSentReqList());
                activity.present_user.setSentFrnReqList(activity.getSentReqList());
                sendBtn.setEnabled(false);

                //update user's recReqList
                if(user.getReceivdFrnReqList() != null){
                    user.getReceivdFrnReqList().add(user.getKey());

                }else{
                    ArrayList<String> recvList = new ArrayList<String>();
                    recvList.add(activity.present_user.getKey());
                    user.setReceivdFrnReqList(recvList);
                }
                firebase.child("users").child(user.getKey()).child("receivdFrnReqList").setValue(user.getReceivdFrnReqList());

            }
        });

        acptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //update present user's frndlist, recReqList
                Firebase.setAndroidContext(activity.getApplicationContext());
                final Firebase firebase = new Firebase("https://hw09-a.firebaseio.com/");
                if(activity.getFriendList() != null){
                    activity.getFriendList().add(user.getKey());

                }else{
                    activity.friendList = new ArrayList<String>();
                    activity.friendList.add(user.getKey());

                }
                activity.getRecevdReqList().remove(user.getKey());
                firebase.child("users").child(activity.present_user.getKey()).child("receivdFrnReqList").setValue(activity.getRecevdReqList());
                activity.present_user.setReceivdFrnReqList(activity.getRecevdReqList());
                firebase.child("users").child(activity.present_user.getKey()).child("friendList").setValue(activity.getFriendList());
                activity.present_user.setFriendList(activity.getFriendList());
                sendBtn.setVisibility(View.INVISIBLE);
                acptBtn.setVisibility(View.INVISIBLE);

                //update user's sent, frndList
                if(user.getFriendList() != null){
                    user.getFriendList().add(activity.present_user.getKey());
                }else{
                    ArrayList<String> frnList = new ArrayList<String>();
                    frnList.add(activity.present_user.getKey());
                    user.setFriendList(frnList);
                }
                if(user.getSentFrnReqList()!= null) {
                    user.getSentFrnReqList().remove(activity.present_user.getKey());
                }
                firebase.child("users").child(user.getKey()).child("sentFrnReqList").setValue(user.getSentFrnReqList());
                firebase.child("users").child(user.getKey()).child("friendList").setValue(user.getFriendList());
            }
        });

        remvBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Firebase.setAndroidContext(activity.getApplicationContext());
                final Firebase firebase = new Firebase("https://hw09-a.firebaseio.com/");
                //update frn list of present_user and user
                if(activity.getFriendList() != null){
                    activity.getFriendList().remove(user.getKey());
                }
                firebase.child("users").child(activity.present_user.getKey()).child("friendList").setValue(activity.getFriendList());
            }
        });
        return convertView;
    }


}