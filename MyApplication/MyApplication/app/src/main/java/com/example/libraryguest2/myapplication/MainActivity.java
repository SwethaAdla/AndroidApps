package com.example.libraryguest2.myapplication;
/* Homework 03
    MainActivity.java
    Group 02- Lakshmi Sridhar, Swetha Adla
*/
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    int completion;
    ArrayList<String> searchList;
    Map<String, Integer> wordCountMap;
    boolean isMatchCase = false;
    ProgressDialog progressDialog;
    public static final String RESULT_MAP_KEY= "resultMapKey";
    int keyWordCount ;
    boolean cursorLoc = true;
    List<EditText> allEds;
    String text ="";

    int etidentifier ;
    int imgidentifier ;
    int remidentifier ;
    int layidentifier ;
    int flag ;


    LinearLayout parentll = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        searchList = new ArrayList<String>();
        allEds = new ArrayList<EditText>();
        wordCountMap = new HashMap<String, Integer>();
        completion = 0;
        keyWordCount = 0;
         etidentifier = 200;
         imgidentifier = 300;
         remidentifier = 400;
         layidentifier = 500;
         flag = 0;

        parentll = (LinearLayout) findViewById(R.id.linearLayout_parent);
        LinearLayout lay = (LinearLayout) findViewById(R.id.Rowlayout);

        ImageButton img = new ImageButton(MainActivity.this);
        img.setId(100);
        img.setBackgroundResource(R.drawable.add2);
        img.setLayoutParams(new LinearLayout.LayoutParams(80,80));

        lay.addView(img);


        EditText e = (EditText) findViewById(R.id.editText2);

            allEds.add(e);

            searchList.add(e.getText().toString());


        findViewById(100).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                addRow(keyWordCount);

               }
        });

        //on clicking search button do async task
        findViewById(R.id.buttonSearch).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(EditText e:allEds){
                    if(e.getText().toString()==null || e.getText().toString().equals("")){
                        continue;
                    }else {
                        flag++;
                        searchList.add(e.getText().toString());
                        Log.d("demo", "all eds list is " + e.getText().toString());
                    }
                }

                if(flag==0){
                    Toast.makeText(getApplicationContext(),"please enter atleast one word to search",Toast.LENGTH_SHORT).show();
                }
                else {

                    CheckBox checkBox = (CheckBox) findViewById(R.id.checkBoxMatchCase);
                    isMatchCase = checkBox.isChecked();
                    for(int i=0; i<searchList.size();i++){
                        if(searchList.get(i).equals("")){
                            searchList.remove(i);
                        }

                    }

                    printList(searchList);
                    text = readFile();
                    progressDialog = new ProgressDialog(MainActivity.this);
                    progressDialog.setMessage("Progress");
                    progressDialog.setCancelable(false);

                    //Async Task
                    for(String searchWord: searchList){
                        new DoAsycTask().execute(searchWord);

                    }


                }

            }
        });
    }

    class DoAsycTask extends AsyncTask<String, Void, Map<String, Integer>>{

        @Override
        protected Map<String, Integer> doInBackground(String... params) {
            Map<String, Integer> outputMap = new HashMap<String, Integer>();
              outputMap.put(params[0], Util.getWordCount(params[0],isMatchCase, text));

            return outputMap;
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog.show();

        }

        @Override
        protected void onPostExecute(Map<String, Integer> stringIntegerMap) {
            super.onPostExecute(stringIntegerMap);
            for(String z: stringIntegerMap.keySet()) {
                wordCountMap.put(z, stringIntegerMap.get(z));
            }
            printMap(wordCountMap);
            completion++;
           // progressDialog.dismiss();
            displayResult(wordCountMap, completion);
        }


    }

    public void displayResult(Map<String, Integer> resultMap, int status){
    for(String d: resultMap.keySet()){
        Log.d("resultMap", d+resultMap.get(d));
    }
        if(status == searchList.size()){
            progressDialog.dismiss();
            Intent intent = new Intent(MainActivity.this, Words.class);
            intent.putExtra(RESULT_MAP_KEY, (Serializable) resultMap);
            startActivity(intent);

        }
    }

    public void printList(ArrayList<String> list){
        for (String s:list) {
            Log.d("demo","the search list in print list method "+s);
        }
    }

    public void printMap(Map<String, Integer> map){
        for (String s:map.keySet()) {
            Log.d("Word", s+ " Count-"+map.get(s));
        }
    }

    public void addRow(int position){
        Log.d("demo","this is a new row ***********************************");
        int clickedId = position;
        if(keyWordCount<19) {
            LinearLayout child = new LinearLayout(MainActivity.this);
            child.setId(layidentifier);
            child.setOrientation(LinearLayout.HORIZONTAL);

            EditText et = new EditText(MainActivity.this);
            //et.setText("working fine "+keyWordCount);
            et.setId(etidentifier);
            et.setWidth(450);

            child.addView(et);

            final ImageButton img = new ImageButton(MainActivity.this);
            img.setId(imgidentifier);
            img.setBackgroundResource(R.drawable.add2);
            img.setLayoutParams(new LinearLayout.LayoutParams(80, 80));

            child.addView(img);

            parentll.addView(child);

            allEds.add(et);

            if(keyWordCount==0){
                LinearLayout lay = (LinearLayout) findViewById(R.id.Rowlayout);
                ImageButton temp = (ImageButton) findViewById(100);
                lay.removeView(temp);
                final ImageButton remimg = new ImageButton(MainActivity.this);
                remimg.setId(remidentifier);
                remimg.setBackgroundResource(R.drawable.rem2);
                remimg.setLayoutParams(new LinearLayout.LayoutParams(80,80));

                lay.addView(remimg);

                remimg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        delRow(remimg.getId());
                    }
                });
            }else{
                LinearLayout prevll = (LinearLayout) findViewById(500+(clickedId-300));
                ImageButton previmg = (ImageButton) findViewById(300+(clickedId-300));
                prevll.removeView(previmg);

                final ImageButton replimg = new ImageButton(MainActivity.this);
                replimg.setId(remidentifier);
                replimg.setBackgroundResource(R.drawable.rem2);
                replimg.setLayoutParams(new LinearLayout.LayoutParams(80,80));
                prevll.addView(replimg);

                replimg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d("demo","printing the replimg id value "+replimg.getId());
                        delRow(replimg.getId());
                    }
                });


            }
            Log.d("demo","keyword entered "+keyWordCount);
            Log.d("demo"," et number "+etidentifier);
            Log.d("demo","image number "+imgidentifier);
            Log.d("demo","linear layout number "+layidentifier);
            Log.d("demo","remove number "+remidentifier);

            img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int clickedId = img.getId();
                    addRow(clickedId);
                    Log.d("demo","in the img on click listener "+keyWordCount);
                }
            });
            keyWordCount++;
            etidentifier=etidentifier+1;
            imgidentifier = imgidentifier+1;
            layidentifier = layidentifier+1;
            remidentifier = remidentifier+1;

        }else{
            Toast.makeText(getApplicationContext(),"Can search only 20 words at a time.",Toast.LENGTH_SHORT).show();
        }


    }

    public void delRow(int rowNum){

        if(rowNum==400){
            LinearLayout lay = (LinearLayout) findViewById(R.id.Rowlayout);
            EditText et = (EditText) findViewById(R.id.editText2);
            allEds.remove(0);
            parentll.removeView(lay);
            Log.d("demo","please work now "+rowNum);
        }else {
            LinearLayout remll = (LinearLayout) findViewById((500 + (rowNum - 400)-1));
            int testll = (500+(rowNum-400)-1);
            Log.d("demo","this is the value of layout id "+testll);
            Log.d("demo","removing he word "+allEds.get(rowNum-400).getText().toString());
            allEds.remove(rowNum-400);
            parentll.removeView(remll);
            Log.d("demo", "in delrow method " + rowNum);



        }
    }

    public String readFile(){

        try {
            InputStream is = getAssets().open("textfile.txt");
            int size = is.available();
            byte[] bffer = new byte[size];
            is.read(bffer);
            is.close();
            text = new String(bffer);
        }catch (IOException e){
            e.printStackTrace();
        }
        return text;
    }
}

