package com.app.hw09;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


public class FriendsListActivity extends AppCompatActivity {
    ListView listView;
    ArrayList<User> users_list;
    ArrayList<String> friendList;
    ArrayList<String> sentReqList;
    ArrayList<String> recevdReqList;
    String userId ="";
    User present_user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_list);
        listView = (ListView) findViewById(R.id.frnListVw);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setLogo(R.drawable.ldpi);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        Firebase.setAndroidContext(getApplicationContext());
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Log.d("Current User id" , userId);
        FirebaseDatabase.getInstance().getReference("users").addValueEventListener(new com.google.firebase.database.ValueEventListener(){
        @Override
        public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
            users_list = new ArrayList<User>();
            for (DataSnapshot d : dataSnapshot.getChildren()) {
                String id = d.getKey();
                Log.d("id", id);
                User user = d.getValue(User.class);
                Log.d("user", user.toString());
                if (id.equals(userId)) {
                    present_user = user;
                    friendList = present_user.getFriendList();
                    sentReqList = present_user.getSentFrnReqList();
                    recevdReqList = present_user.getReceivdFrnReqList();
                } else {
                    users_list.add(user);
                }
            }
            Log.d("FrndListActivity ","User ArrayList size: "+users_list.size());
            if(!users_list.isEmpty()){
                listView.setVisibility(View.VISIBLE);
                FrnListAdapter adapter = new FrnListAdapter(getApplicationContext(),R.layout.frnlist_row_view,users_list, FriendsListActivity.this);
                listView.setAdapter(adapter);
            }
            else {
                Log.d("demo","Users List is Empty");

            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    });
}

    public ArrayList<String> getFriendList() {
        return friendList;
    }

    public void setFriendList(ArrayList<String> friendList) {
        this.friendList = friendList;
    }

    public ArrayList<String> getSentReqList() {
        return sentReqList;
    }

    public void setSentReqList(ArrayList<String> sentReqList) {
        this.sentReqList = sentReqList;
    }

    public ArrayList<String> getRecevdReqList() {
        return recevdReqList;
    }

    public void setRecevdReqList(ArrayList<String> recevdReqList) {
        this.recevdReqList = recevdReqList;
    }
}
