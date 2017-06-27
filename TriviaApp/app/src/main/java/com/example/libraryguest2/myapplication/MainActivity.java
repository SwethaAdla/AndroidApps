package com.example.libraryguest2.myapplication;
/* Homework 04
   MainActivity.java
   Group 02- Lakshmi Sridhar, Swetha Adla
   */
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements GetQuestionsAsyncTask.IQuestions{
    public static final String QUESTION_LIST_KEY = "QUESTION_LIST_KEY";
    ArrayList<Question> questionArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button startBtn = (Button) findViewById(R.id.buttonStart);
        startBtn.setEnabled(false);
        RequestParams params = new RequestParams("http://dev.theappsdr.com/apis/trivia_json/index.php","GET");
        new GetQuestionsAsyncTask(MainActivity.this).execute(params);



        findViewById(R.id.buttonStart).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TriviaActvity.class);
                intent.putExtra(QUESTION_LIST_KEY, questionArrayList);
                startActivity(intent);
            }
        });

        findViewById(R.id.buttonExit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    @Override
    public void loadQuestions(final ArrayList<Question> questions) {
        questionArrayList = new ArrayList<Question>();

        for(Question q: questions){
            questionArrayList.add(q);
        }
        Log.d("questionList", String.valueOf(questionArrayList.size()));
        printQuesList(questionArrayList);
    }

    private void printQuesList(ArrayList<Question> list){
        for(Question q: list){
            Log.d("id",q.getId());
            Log.d("text", q.getText());
            Log.d("image", q.getImageUrl());
            Log.d("Choices", "choice");
            for(String s:q.getChoices().getChoices()){
                Log.d("",s);

            }
            Log.d("answer", q.getChoices().getAnswer());
        }

}
}
