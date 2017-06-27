package com.app.hw09;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfileActivity extends AppCompatActivity {
    EditText first_name, last_name, password;
    Switch gender_switch;
    CircleImageView avatar_vw;
    Button save_button, cancel_button;
    TextView gender_value;
    User old_user, new_user;
    public final static int GALLERY_CODE = 100;

    DatabaseReference f_database = FirebaseDatabase.getInstance().getReference();
    StorageReference f_storage = FirebaseStorage.getInstance().getReference();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setLogo(R.drawable.ldpi);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        first_name = (EditText) findViewById(R.id.firstNameEdit);
        last_name = (EditText) findViewById(R.id.lastNameEdit);
        password = (EditText) findViewById(R.id.currentPwdEdit);
        gender_switch = (Switch) findViewById(R.id.genderSwitch);
        save_button = (Button) findViewById(R.id.saveProfileButton);
        cancel_button = (Button) findViewById(R.id.cancelButton);
        avatar_vw = (CircleImageView) findViewById(R.id.avatar);
        gender_value = (TextView) findViewById(R.id.genderValue);

        f_database.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                old_user = dataSnapshot.getValue(User.class);
                first_name.setText(old_user.getFname());
                last_name.setText(old_user.getLname());
                password.setText(old_user.getPassword());
                gender_value.setText(old_user.getGender());
                Log.d("demo",gender_value.getText().toString());
                if(old_user.getGender() != null && old_user.getGender().equals("M")){
                    gender_switch.setChecked(true);
                }
                else{
                    gender_switch.setChecked(false);
                }

                if (old_user.getProfilePicUrl() != null && !old_user.getProfilePicUrl().equals("")) {
                    Picasso.with(EditProfileActivity.this).load(old_user.getProfilePicUrl())
                            .into(avatar_vw);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bitmap bitmap = ((BitmapDrawable)avatar_vw.getDrawable()).getBitmap();

                Uri tempUri = getImageUri(getApplicationContext(), bitmap);

                f_storage.child("images").child(tempUri.getLastPathSegment()).putFile(tempUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Log.d("demo","Download url:"+taskSnapshot.getDownloadUrl().toString());
                        new_user = new User();
                        new_user.setEmail(old_user.getEmail());
                        new_user.setFname(first_name.getText().toString());
                        new_user.setLname(last_name.getText().toString());
                        new_user.setPassword(password.getText().toString());
                        new_user.setKey(old_user.getKey());
                        if(gender_switch.isChecked()){
                            new_user.setGender(gender_switch.getTextOn().toString());
                        }
                        else{
                            new_user.setGender(gender_switch.getTextOff().toString());
                        }
                        new_user.setProfilePicUrl(taskSnapshot.getDownloadUrl().toString());

                        f_database.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(new_user);
                        Toast.makeText(EditProfileActivity.this, "Profile Saved", Toast.LENGTH_SHORT).show();
                        finish();

                    }
                });

            }
        });

        cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        avatar_vw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,GALLERY_CODE);
            }
        });

        gender_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    gender_value.setText("M");
                }
                else{
                    gender_value.setText("F");
                }
            }
        });



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == GALLERY_CODE && resultCode == RESULT_OK){
            Uri uri = data.getData();

            Picasso.with(EditProfileActivity.this).load(uri.toString())
                    .into(avatar_vw);
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
        Intent i = new Intent(EditProfileActivity.this, TripActivity.class);
        startActivity(i);
        super.onBackPressed();
    }
}
