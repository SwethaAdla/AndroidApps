package com.app.hw09;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MessagesActivity extends AppCompatActivity {
    ArrayList<Messages> msgsList = new ArrayList<>();
    Trip trip;
    public final static int GALLERY_CODE = 100;
    FirebaseStorage storage;
    StorageReference storageRef,imagesRef;
    MessagesAdapter adapter;
    RecyclerView recyclerView;
    LinearLayoutManager messageLayoutManager;
    final ArrayList<User> allUsers = getAllUsers();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setLogo(R.drawable.ldpi);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        Log.d("demo","onCreate");
        if(getIntent().getExtras()!=null){
            trip = (Trip) getIntent().getExtras().get("TRIP_SELECTED");
            recyclerView = (RecyclerView) findViewById(R.id.myRecyclerView);
            messageLayoutManager= new LinearLayoutManager(this,LinearLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(messageLayoutManager);
            final EditText messageText = (EditText) findViewById(R.id.editTextWriteMessage);
            ImageView sendMsg = (ImageView) findViewById(R.id.imageViewaddMessage);
            ImageView galleryImg = (ImageView) findViewById(R.id.imageViewaddPhoto);

            storage = FirebaseStorage.getInstance();
            storageRef = storage.getReferenceFromUrl("gs://hw09-a.appspot.com");
            imagesRef = storageRef.child("images");
            if(trip.isRemoved){
                sendMsg.setEnabled(false);
                galleryImg.setEnabled(false);
            }
            sendMsg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(messageText.equals("")||messageText.equals(null)||messageText==null){
                        Toast.makeText(MessagesActivity.this,"Please enter some text",Toast.LENGTH_SHORT).show();
                    }else{
                        //save the message in firebase
                        Messages m = new Messages();
                        m.setText(messageText.getText().toString());
                        m.setSentBy(FirebaseAuth.getInstance().getCurrentUser().getUid());
                        Date dt = new Date();
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MMM-dd");
                        m.setSentTime(String.valueOf(sdf.format(dt)));
                        m.setTripId(trip.getKey());
                        m.setImageUrl("");

                        Firebase.setAndroidContext(getApplicationContext());
                        final Firebase firebase = new Firebase("https://hw09-a.firebaseio.com/");
                        String key = firebase.child("trips").child(trip.getKey()).child("messages").push().getKey();
                        m.setKey(key);
                        firebase.child("trips").child(trip.getKey()).child("messages").child(key).setValue(m);
                        for(String s:trip.getParticipantList()) {
                            firebase.child("users").child(s).child("trips").child(trip.getKey()).child("messages").child(key).setValue(m);
                        }
                        messageText.setText("");
                    }

                }
            });

            galleryImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType("image/*");
                    startActivityForResult(intent,GALLERY_CODE);
                }
            });
        }

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("result code","is "+resultCode);
        Log.d("request code","is "+requestCode);

        if(requestCode == GALLERY_CODE && resultCode == RESULT_OK){
            Uri uri = data.getData();

            imagesRef.child(uri.getLastPathSegment()).putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Messages m = new Messages();
                    m.setText("");
                    m.setSentBy(FirebaseAuth.getInstance().getCurrentUser().getUid());
                    Date dt = new Date();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MMM-dd");
                    m.setSentTime(String.valueOf(sdf.format(dt)));
                    m.setTripId(trip.getKey());
                    m.setImageUrl(taskSnapshot.getDownloadUrl().toString());
                    Log.d("imageurl","imageurl is "+m.getImageUrl());

                    Firebase.setAndroidContext(getApplicationContext());
                    final Firebase firebase = new Firebase("https://hw09-a.firebaseio.com/");
                    String key = firebase.child("trips").child(trip.getKey()).child("messages").push().getKey();
                    m.setKey(key);
                    firebase.child("trips").child(trip.getKey()).child("messages").child(key).setValue(m);
                    for(String s:trip.getParticipantList()) {
                        firebase.child("users").child(s).child("trips").child(trip.getKey()).child("messages").child(key).setValue(m);
                    }

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });


        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("demo","onStart");
        final String loggedInUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
        if(loggedInUser != null){

            Firebase.setAndroidContext(getApplicationContext());

            FirebaseDatabase.getInstance().getReference().child("users").child(loggedInUser).child("trips").child(trip.getKey()).child("messages").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    msgsList.clear();
                    for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                        msgsList.add(snapshot.getValue(Messages.class));
                    }
                    Log.d("demo","Message length is "+ msgsList.size());

                    adapter = new MessagesAdapter(msgsList,MessagesActivity.this,loggedInUser,allUsers, trip);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            /*FirebaseDatabase.getInstance().getReference().child("users").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    u = dataSnapshot.getValue(User.class);
                    logInUser.setText(u.getName());
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });*/
        }


    }
    public ArrayList<User> getAllUsers(){
        final ArrayList<User> users = new ArrayList<>();
        Firebase.setAndroidContext(MessagesActivity.this);
        FirebaseDatabase.getInstance().getReference("users").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                users.clear();
                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    Log.d("demo", snapshot.getValue(User.class).toString());
                    users.add(snapshot.getValue(User.class));
                }
                Log.d("demo","nameslist size: "+users.size());
                TextView tv = (TextView) findViewById(R.id.textViewTripNameLocation);
                String name = "";

                for (User u : allUsers) {
                    if (u.getKey().equalsIgnoreCase(trip.getCreatedByUid())) {
                        name = u.getFname() + " " + u.getLname();
                    }
                }
                tv.setText(trip.getTitle()+", "+trip.getLocation()+". Created by: "+name);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return users;
    }


}
