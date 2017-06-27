package com.app.hw09;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;


public class CreateTripActivity extends AppCompatActivity {
    EditText title, location;
    ImageView avatarTrip_vw;
    Button save_button, cancel_button;
    public final static int GALLERY_CODE = 100;

    DatabaseReference f_database = FirebaseDatabase.getInstance().getReference();
    StorageReference f_storage = FirebaseStorage.getInstance().getReference();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_trip);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setLogo(R.drawable.ldpi);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        title = (EditText) findViewById(R.id.titleEdit);
        location = (EditText) findViewById(R.id.locationEdit);
        save_button = (Button) findViewById(R.id.saveTripButton);
        cancel_button = (Button) findViewById(R.id.cancelTripButton);
        avatarTrip_vw = (ImageView) findViewById(R.id.avatarTrip);


        save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(avatarTrip_vw.getDrawable()==null){
                    Toast.makeText(getApplicationContext(), "Please Upload an Image", Toast.LENGTH_SHORT).show();
                }
               /* Bitmap bitmap = ((BitmapDrawable)avatarTrip_vw.getDrawable()).getBitmap();
                Bitmap emptyBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), bitmap.getConfig());
                if(bitmap.sameAs(emptyBitmap)){
                    Toast.makeText(getApplicationContext(), "Please Upload an Image", Toast.LENGTH_SHORT).show();
                }
                if(bitmap == null){
                    Toast.makeText(getApplicationContext(), "Please Upload an Image", Toast.LENGTH_SHORT).show();
                }*/else {
                    Bitmap bitmap = ((BitmapDrawable)avatarTrip_vw.getDrawable()).getBitmap();
                    Uri tempUri = getImageUri(getApplicationContext(), bitmap);

                    f_storage.child("images/trip").child(tempUri.getLastPathSegment()).putFile(tempUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Log.d("demo", "Download url:" + taskSnapshot.getDownloadUrl().toString());
                            Trip new_Trip = new Trip();
                            new_Trip.setTitle(title.getText().toString());
                            new_Trip.setLocation(location.getText().toString());
                            new_Trip.setPhotoUrl(taskSnapshot.getDownloadUrl().toString());
                            new_Trip.setCreatedByUid(FirebaseAuth.getInstance().getCurrentUser().getUid());
                            ArrayList<String> partcipantList = new ArrayList<String>();
                            partcipantList.add(FirebaseAuth.getInstance().getCurrentUser().getUid());
                            new_Trip.setParticipantList(partcipantList);
                            Date dt = new Date();
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MMM-dd");
                            new_Trip.setCreatedDate(String.valueOf(sdf.format(dt)));

                            Firebase.setAndroidContext(getApplicationContext());
                            final Firebase firebase = new Firebase("https://hw09-a.firebaseio.com/");
                            String key = firebase.child("trips").push().getKey();
                            new_Trip.setKey(key);
                            Log.d("demo", key);
                            firebase.child("trips").child(key).setValue(new_Trip);
                            Toast.makeText(CreateTripActivity.this, "Trip Created", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(CreateTripActivity.this, TripActivity.class);
                            startActivity(intent);


                        }
                    });
                }
            }
        });


        cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CreateTripActivity.this, TripActivity.class);
                startActivity(intent);
            }
        });

        avatarTrip_vw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,GALLERY_CODE);
            }
        });


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == GALLERY_CODE && resultCode == RESULT_OK){
            Uri uri = data.getData();

            Picasso.with(CreateTripActivity.this).load(uri.toString())
                    .into(avatarTrip_vw);
        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
    @Override
    public void onBackPressed() {
        Intent i = new Intent(CreateTripActivity.this, TripActivity.class);
        startActivity(i);
        super.onBackPressed();
    }
}
