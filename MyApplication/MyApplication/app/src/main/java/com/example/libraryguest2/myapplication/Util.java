package com.example.libraryguest2.myapplication;
/* Homework 03
    Util.java
    Group 02- Lakshmi Sridhar, Swetha Adla
*/

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Util {


     public static Integer getWordCount(String searchWord, boolean isMatchCase, String text){

        String totalText = text;
        int count = 0;
        String stringToSearch = searchWord;
        if(!isMatchCase){
            totalText = text.toLowerCase();
            stringToSearch = searchWord.toLowerCase();
            Log.d("searchWord", stringToSearch);
            Pattern p = Pattern.compile(stringToSearch);
            Matcher matcher = p.matcher(totalText);
            while(matcher.find()){
                count++;
            }
        }else {
            Log.d("searchWord", searchWord);
            Pattern p = Pattern.compile(searchWord);
            Matcher matcher = p.matcher(text);
            while (matcher.find()) {
                count++;
            }
        }
       return count;


    }


}
