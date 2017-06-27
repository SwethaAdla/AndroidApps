
package com.app.bmicalculator;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class BMICalculator extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bmicalculator);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editTxtWeight = (EditText) findViewById(R.id.editTextWeight);
                EditText editTxtHeightFeet = (EditText) findViewById(R.id.editTextHeightFeet);
                EditText editTxtHeightInches = (EditText) findViewById(R.id.editText3);
                try {
                    double weight = Double.parseDouble(editTxtWeight.getText().toString());
                    Log.d("weight", String.valueOf(weight));

                    double feetInInches = (Double.parseDouble(editTxtHeightFeet.getText().toString())) * 12;
                    Log.d("feetInInches", String.valueOf(feetInInches));
                    double inches = Double.parseDouble(editTxtHeightInches.getText().toString());
                    double totalInches = feetInInches + inches ;
                    Log.d("totalInchess", String.valueOf(totalInches));
                    if (weight < 0 ||weight== 0 || feetInInches < 0 || feetInInches == 0 || inches<0) {
                        Toast.makeText(getApplicationContext(), "Invalid Input", Toast.LENGTH_SHORT).show();
                    }else{
                        double bmi = (weight * 703) / (totalInches * totalInches);
                        String bmiString = String.format("%.2f",bmi);
                        Log.d("result", String.valueOf(bmi));
                        TextView resultBmi = (TextView) findViewById(R.id.textViewResultBmi);
                        TextView resultCategry = (TextView) findViewById(R.id.textViewResultCategory);
                        resultBmi.setText("Your BMI: " + bmiString);
                        if (bmi < 18.5) {
                            resultCategry.setText("You are Under Weight");
                        } else if (bmi > 18.5 && bmi < 24.9) {
                            resultCategry.setText("You are Normal Weight");
                        } else if (bmi > 25 && bmi < 29.9) {
                            resultCategry.setText("You are Over Weight");
                        } else if (bmi == 30 || bmi > 30) {
                            resultCategry.setText("You are Obese");
                        }
                        Toast.makeText(getApplicationContext(), "BMI Calculated", Toast.LENGTH_SHORT).show();
                    }

                } catch (NumberFormatException e) {
                    Toast.makeText(getApplicationContext(), "Invalid Input", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


}
