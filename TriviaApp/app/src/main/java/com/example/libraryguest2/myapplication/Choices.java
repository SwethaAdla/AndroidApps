package com.example.libraryguest2.myapplication;

import java.io.Serializable;
import java.util.ArrayList;

/* Homework 04
   Choices.java
   Group 02- Lakshmi Sridhar, Swetha Adla
   */

public class Choices implements Serializable {

    ArrayList<String> choices ;
    String answer;

    public ArrayList<String> getChoices() {
        return choices;
    }

    public void setChoices(ArrayList<String> choices) {
        this.choices = choices;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    @Override
    public String toString() {
        return "Choices{" +
                "choices=" + choices +
                ", answer='" + answer + '\'' +
                '}';
    }
}
