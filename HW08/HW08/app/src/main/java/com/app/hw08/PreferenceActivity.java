package com.app.hw08;

/* Group 02 - Lakshmi Sridhar, Swetha Adla
   Homework 08
   PreferenceActivity.java
 */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class PreferenceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Display the fragment as the main content.
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new PreferenceFragment())
                .commit();
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(PreferenceActivity.this, MainActivity.class);
        startActivity(i);
        super.onBackPressed();
    }
}
