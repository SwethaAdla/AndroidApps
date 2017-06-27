package com.example.libraryguest2.myapplication;
/* Homework 04
   TriviaActivity.java
   Group 02- Lakshmi Sridhar, Swetha Adla
   */
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.annotation.IntegerRes;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class TriviaActvity extends AppCompatActivity {

    ArrayList<Question> qList;
    int position = 0;
    ArrayList<Integer> answersSelected;
    Map<Integer, Integer> answerChosen = new HashMap<Integer, Integer>();;
    public static final String SELECTED_ANS_LIST_KEY = "SELECTED_ANS_LIST_KEY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trivia_actvity);

        if(getIntent().getExtras()!= null){
            qList = (ArrayList<Question>) getIntent().getExtras().get(MainActivity.QUESTION_LIST_KEY);

            for(int i=0; i<qList.size();i++){
                answerChosen.put(i, -1);        // initialising map with keys 0 to 15 ans setting values to -1
            }

            //timer for 2 mins
            new CountDownTimer(120000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    TextView timerView = (TextView) findViewById(R.id.textViewTimer);
                    timerView.setText("Time left: "+ millisUntilFinished / 1000+" sec");

                }
                @Override
                public void onFinish() {
                    displayStats();
                }
            }.start();


            position=0;
            setAllfields(0);

            findViewById(R.id.buttonPrev).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(position == 0){
                        position = 0;
                    }else{
                        position = position-1;
                    }
                    setAllfields(position);
                }
            });


            findViewById(R.id.buttonNext).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(position == qList.size()-1){
                        position= qList.size()-1;

                    }else{
                        position= position+1;
                    }
                    setAllfields(position);
                    if(position == qList.size()-1) { // for last question setting finish button
                        Button btn = new Button(TriviaActvity.this);
                        btn.setText("Finish");
                        btn.setId(99998+1);
                        btn.setAllCaps(false);
                        btn.setOnClickListener(listener);
                        RelativeLayout rl = (RelativeLayout) findViewById(R.id.activity_trivia_actvity);
                        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_END);
                        rl.addView(btn, layoutParams);

                        Button btnNext = (Button) findViewById(R.id.buttonNext);
                        btnNext.setVisibility(View.INVISIBLE);


                    }

                }
            });

        }else{
            Log.d("demo", "getIntent().getExtras() is null");
        }

    }

    public void setAllfields(final Integer position){

        TextView qNoView = (TextView) findViewById(R.id.textViewQNo);
        qNoView.setText(String.valueOf("Q.No: "+(position+1)));

        if(qList.get(position).getImageUrl() != null && !qList.get(position).getImageUrl().equals("")) {
            new GetImageAsyncTask(TriviaActvity.this).execute(qList.get(position).getImageUrl());
        }else{
            ImageView iv = (ImageView) findViewById(R.id.imageView2);
            iv.setImageDrawable(null);
        }

        TextView qTextView = (TextView) findViewById(R.id.textViewQText);
        qTextView.setText(qList.get(position).getText());
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linearLayout);
        linearLayout.removeAllViews();
        RadioGroup radioGroup = new RadioGroup(TriviaActvity.this);
        RadioButton[] rb = new RadioButton[qList.get(position).getChoices().getChoices().size()];
        ArrayList<String> ansList = qList.get(position).getChoices().getChoices();

        for(int j =0; j<ansList.size(); j++) {
            rb[j] = new RadioButton(TriviaActvity.this);
            rb[j].setText(ansList.get(j));
            rb[j].setId(101+j);
            rb[j].setTextSize(14);
            radioGroup.addView(rb[j]);
        }

        linearLayout.addView(radioGroup);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                answerChosen.put(position, checkedId-101);
            }
        });

        if(answerChosen!= null && !answerChosen.isEmpty() && answerChosen.get(position) !=-1){
            radioGroup.check(answerChosen.get(position)+101);
        }

        Button btnNext = (Button) findViewById(R.id.buttonNext);
        btnNext.setVisibility(View.VISIBLE);

        if(findViewById(99998+1) != null){
            Button btnFinish = (Button) findViewById(99998+1);
            btnFinish.setVisibility(View.INVISIBLE);
        }
    }

    public void displayStats(){
        answersSelected = new ArrayList<Integer>();
        for(int i=0; i<qList.size(); i++){
            answersSelected.add(i,answerChosen.get(i));
        }
        printArrayList(answersSelected);
        Intent intent = new Intent(TriviaActvity.this, StatsActivity.class);
        intent.putExtra(MainActivity.QUESTION_LIST_KEY, qList);
        intent.putExtra(SELECTED_ANS_LIST_KEY, answersSelected);
        startActivity(intent);
    }

    private void printArrayList(ArrayList<Integer> arrList){
        for(int i=0; i<arrList.size(); i++){
            Log.d("demo", i+": "+arrList.get(i));
        }

    }

    public View.OnClickListener listener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            displayStats();

        }
    };
}
