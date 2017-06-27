package com.example.libraryguest2.myapplication;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/* Homework 04
   QuestionUtil.java
   Group 02- Lakshmi Sridhar, Swetha Adla
   */

public class QuestionsUtil {
    static public class questionJSONParser {
        static ArrayList<Question> parseQuestions(String in) throws JSONException {
            ArrayList<Question> questionsList = new ArrayList<Question>();
            JSONObject root = new JSONObject(in);
            JSONArray questionArray = root.getJSONArray("questions");
            for (int i=0; i<questionArray.length(); i++){
                JSONObject questionJSONObj = questionArray.getJSONObject(i);
                Question question = Question.createQuestion(questionJSONObj);
                questionsList.add(question);
            }
           return questionsList;
        }
    }
}
