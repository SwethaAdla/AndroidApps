package com.example.libraryguest2.myapplication;
/* Homework 04
   StatsActivity.java
   Group 02- Lakshmi Sridhar, Swetha Adla
   */
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class StatsActivity extends AppCompatActivity {

    ArrayList<Question> qList;
    ArrayList<Integer> answersSelected;
    ArrayList<Question> newList = new ArrayList<Question>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        Log.d("demo","entered stats activity java class");
        LinearLayout ll = (LinearLayout) findViewById(R.id.Linearlayoutstats);

        if(getIntent().getExtras()!=null){
            int numOfWrongAnswers = 0;
            Log.d("demo","intents are not null");
            qList= (ArrayList<Question>) getIntent().getExtras().get(MainActivity.QUESTION_LIST_KEY);
            answersSelected= (ArrayList<Integer>) getIntent().getExtras().get(TriviaActvity.SELECTED_ANS_LIST_KEY);


            for(int i=0;i<qList.size();i++){

                ArrayList<String> choices = qList.get(i).getChoices().getChoices();
                int actualAnswer = Integer.parseInt(qList.get(i).getChoices().getAnswer());
                int selectedAnswer = answersSelected.get(i)+1;

                if(actualAnswer==selectedAnswer){
                    newList.add(qList.get(i));
                    Log.d("demo","actual answer for question"+qList.get(i).getText()+"=="+choices.get(actualAnswer-1)+"and the given answer is "+choices.get(answersSelected.get(i)));
                    continue;
                }else{
                    numOfWrongAnswers++;
                    TextView tv = new TextView(StatsActivity.this);
                    tv.setText("Q"+qList.get(i).getId()+" "+qList.get(i).getText().toString());
                    tv.setTextColor(Color.parseColor("#000000"));
                    TextView tv1 = new TextView(StatsActivity.this);
                    int tempAnswer = 0;
                    if(answersSelected.get(i)<0){

                        /*if(actualAnswer==1){
                            tempAnswer = actualAnswer+1;
                        }else{
                            tempAnswer = actualAnswer-2;
                        }*/
                        tv1.setText("Your answer: No answer was selected");
                        Log.d("demo","the temp answer for this is "+tempAnswer+"and the value is "+choices.get(tempAnswer));
                    }else{
                        tv1.setText("Your answer: "+choices.get(answersSelected.get(i)));
                        Log.d("demo","the selected answer for this is "+answersSelected.get(i)+"and the value is "+choices.get(answersSelected.get(i)));
                    }

                    tv1.setTextColor(Color.parseColor("#000000"));
                    tv1.setBackgroundResource(R.color.red);
                    TextView tv2 = new TextView(StatsActivity.this);
                    tv2.setText("Correct answer: "+choices.get(actualAnswer-1));
                    tv2.setTextColor(Color.parseColor("#000000"));

                    ll.addView(tv);
                    ll.addView(tv1);
                    ll.addView(tv2);

                    Log.d("demo","Wrong answer*****"+qList.get(i).getText()+"=="+choices.get(actualAnswer-1)+"and the given answer is "+choices.get(tempAnswer));

                }
            }

            Log.d("demo","wrong answers"+numOfWrongAnswers);
            ProgressBar progress = (ProgressBar) findViewById(R.id.progressBar2);
            int numOfCrctAns = 16-numOfWrongAnswers;
            Log.d("demo","correct answers"+numOfCrctAns);
            int percentage = (numOfCrctAns*100)/16;
            progress.setProgress(percentage);
            progress.setVisibility(View.VISIBLE);

            TextView progressTxt = (TextView) findViewById(R.id.displayPercent);
            progressTxt.setText(percentage+"%");
            Log.d("demo","percentage calculated is "+percentage);

           }

        findViewById(R.id.finishButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StatsActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        }
}
