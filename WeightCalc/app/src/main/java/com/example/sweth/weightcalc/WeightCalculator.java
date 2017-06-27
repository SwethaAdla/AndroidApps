package com.example.sweth.weightcalc;

import android.content.Context;
import android.content.Intent;
import android.icu.text.DecimalFormat;
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
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class WeightCalculator extends AppCompatActivity {

    private RadioGroup rg1;
    public double calculatedWeight =0.0;
    public double CalculatedWeightLowerLimit = 0.0;
    public double CalculatedWeightUpperLimit = 0.0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weight_calculator);
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

        findViewById(R.id.clickme).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rg1= (RadioGroup)findViewById(R.id.Radiogroup);

                EditText height = (EditText) findViewById(R.id.feet);
                try{
                int heightInInches = (Integer.parseInt(height.getText().toString())) * 12;
                EditText inches = (EditText) findViewById(R.id.inches);
                double actualHeight = heightInInches + Double.parseDouble(inches.getText().toString());
                TextView tv = (TextView) findViewById(R.id.Result);
                if(heightInInches<0 || heightInInches==0 || Integer.parseInt(inches.getText().toString())<0 || rg1.getCheckedRadioButtonId() == -1 || height.getText().toString()==null || inches.getText().toString()==null){
                    Toast.makeText(getApplicationContext(),"Invalid Input",Toast.LENGTH_SHORT).show();
                }else {

                    if (rg1.getCheckedRadioButtonId() == R.id.rb1) {

                        calculatedWeight = (18.5 * actualHeight * actualHeight) / (703);
                        tv.setText("Your Weight should be less than " + String.valueOf(calculatedWeight) + " lb");

                    } else if (rg1.getCheckedRadioButtonId() == R.id.rb2) {
                        CalculatedWeightLowerLimit = (18.5 * actualHeight * actualHeight) / (703);


                        CalculatedWeightUpperLimit = (24.9 * actualHeight * actualHeight) / (703);
                        String cll = String.format("%.2f",CalculatedWeightLowerLimit);
                        String cul = String.format("%.2f",CalculatedWeightUpperLimit);
                        tv.setText("Your Weight should be in between " + cll + " to " + cul + " lb");


                    } else if (rg1.getCheckedRadioButtonId() == R.id.rb3) {

                        CalculatedWeightLowerLimit = (25.0 * actualHeight * actualHeight) / (703);
                        CalculatedWeightUpperLimit = (29.9 * actualHeight * actualHeight) / (703);
                        String cll = String.format("%.2f",CalculatedWeightLowerLimit);
                        String cul = String.format("%.2f",CalculatedWeightUpperLimit);
                        tv.setText("Your Weight should be in between " + cll + " to " + cul + " lb");


                    } else if (rg1.getCheckedRadioButtonId() == R.id.rb4) {

                        calculatedWeight = (29.9 * actualHeight * actualHeight) / (703);
                        tv.setText("Your Weight should be greater than " + String.valueOf(calculatedWeight) + " lb");
                    }
                    Toast.makeText(getApplicationContext(), "Weight Calculated", Toast.LENGTH_SHORT).show();
                }
            }catch (NumberFormatException e){
                   Toast.makeText(getApplicationContext(),"Invalid Input",Toast.LENGTH_SHORT).show();
                }}


        });
    }


}
