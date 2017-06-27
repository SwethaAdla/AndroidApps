package com.app.hw09;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener{
    EditText firstName;
    EditText lastName;
    EditText email;
    EditText choosePwd;
    EditText repeatPwd;
    Button cancelBtn;
    Button signUpBtn;
    User user = new User();
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setLogo(R.drawable.ldpi);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        auth = FirebaseAuth.getInstance();
        firstName = (EditText) findViewById(R.id.editTextFirstName);
        lastName = (EditText) findViewById(R.id.editTextlastName);
        email = (EditText) findViewById(R.id.editTextEmail1);
        choosePwd = (EditText) findViewById(R.id.editTextChoosePassword);
        repeatPwd = (EditText) findViewById(R.id.editTextrepeatPwd);
        cancelBtn = (Button) findViewById(R.id.buttonCancel);
        signUpBtn = (Button) findViewById(R.id.buttonSignUp1);

        signUpBtn.setOnClickListener(this);
        cancelBtn.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case  R.id.buttonSignUp1:
                if(email.getText().toString().equals("") || choosePwd.getText().toString().equals("")||firstName.getText().equals("") ){
                    Toast.makeText(SignUpActivity.this, "Email,Password and full  name cannot be empty", Toast.LENGTH_SHORT).show();
                }
                else{
                    user.setEmail(email.getText().toString());
                    user.setFname(firstName.getText().toString());
                    user.setLname(lastName.getText().toString());
                    user.setPassword(choosePwd.getText().toString());
                    user.setGender("");
                    user.setProfilePicUrl("");
                    signup(firstName.getText().toString(),email.getText().toString(),choosePwd.getText().toString());

                }
                break;
            case R.id.buttonCancel:
                finish();
                break;

        }
    }


    public void signup(String name,String mail,String pass){
        Log.d("test", mail + "--------" + pass);
        auth.createUserWithEmailAndPassword(mail,pass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (!task.isSuccessful()) {
                    Toast.makeText(SignUpActivity.this, task.getResult().toString(), Toast.LENGTH_SHORT).show();
                }else{

                    Firebase.setAndroidContext(getApplicationContext());
                    final Firebase firebase = new Firebase("https://hw09-a.firebaseio.com/");
                    FirebaseUser u = FirebaseAuth.getInstance().getCurrentUser();
                    String key = firebase.child("users").push().getKey();
                    user.setKey(u.getUid());
                    firebase.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user);
                    Toast.makeText(SignUpActivity.this, "Account created! Please, log in.", Toast.LENGTH_SHORT).show();
                    FirebaseAuth.getInstance().signOut();

                    Intent in = new Intent(SignUpActivity.this,MainActivity.class);
                    startActivity(in);
                }
            }
        });
    }

}

