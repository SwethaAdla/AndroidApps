package com.example.libraryguest2.myapplication;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static android.R.id.list;

/* Homework 04
   Question.java
   Group 02- Lakshmi Sridhar, Swetha Adla
   */

public class Question implements Serializable{
    String id;
    String text;
    String imageUrl;
    Choices choices;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Choices getChoices() {
        return choices;
    }

    public void setChoices(Choices choices) {
        this.choices = choices;
    }

    @Override
    public String toString() {
        return "Question{" +
                "id='" + id + '\'' +
                ", text='" + text + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", choices=" + choices +
                '}';
    }

    public static Question createQuestion(JSONObject jsonObject) throws JSONException {
        Question question = new Question();
        question.setId(jsonObject.getString("id"));
        question.setText(jsonObject.getString("text"));
        if(jsonObject.isNull("image")) {
            question.setImageUrl("");
        }else {
            question.setImageUrl(jsonObject.getString("image"));
        }
        Choices choices = new Choices();
        JSONArray choicesArray = jsonObject.getJSONObject("choices").getJSONArray("choice");
        ArrayList<String> choiceslist = new ArrayList<String>();
        for (int i=0; i<choicesArray.length(); i++) {
            choiceslist.add( choicesArray.getString(i) );
        }
        choices.setChoices(choiceslist);
        choices.setAnswer(jsonObject.getJSONObject("choices").getString("answer"));
        question.setChoices(choices);
        return question;
    }
}
