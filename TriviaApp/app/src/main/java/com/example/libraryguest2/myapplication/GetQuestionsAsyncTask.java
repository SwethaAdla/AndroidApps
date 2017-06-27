package com.example.libraryguest2.myapplication;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

/* Homework 04
   GetQuestionsAsyncTask.java
   Group 02- Lakshmi Sridhar, Swetha Adla
   */

public class GetQuestionsAsyncTask extends AsyncTask<RequestParams,Void,ArrayList<Question>> {
    IQuestions iQuestions;
    MainActivity activity;
    ProgressDialog progressDialog;

    public GetQuestionsAsyncTask(MainActivity activity){
        this.activity = activity;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage("Loading Trivia");
        progressDialog.setCancelable(false);
        progressDialog.show();

    }

    public GetQuestionsAsyncTask(IQuestions iQuestions) {
        this.iQuestions = iQuestions;
    }

    @Override
    protected ArrayList<Question> doInBackground(RequestParams... params) {
        BufferedReader reader=null;
        StringBuilder sb = null;
        try {
            HttpURLConnection con = params[0].setUpConnection();
            con.connect();
            int status = con.getResponseCode();
            if(status==HttpURLConnection.HTTP_OK){
                reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                sb = new StringBuilder();
                String line = "";
                while((line=reader.readLine())!=null){
                   sb.append(line);
                }
                Log.d("demo","This is the full string"+sb.toString());
                return QuestionsUtil.questionJSONParser.parseQuestions(sb.toString());
            }else{
                Log.d("demo","response code is "+String.valueOf(status));
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }finally {
            if(reader!=null){
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        Log.d("demo","in doInBackground method. returning null");
        return null;
    }


    @Override
    protected void onPostExecute(ArrayList<Question> questions) {
        super.onPostExecute(questions);
        if(questions!=null){
            activity.loadQuestions(questions);
        }else{
            Log.d("demo","questions size is null in on post execute method");
        }
        progressDialog.dismiss();
        ImageView iv = (ImageView) activity.findViewById(R.id.imageView);
        iv.setImageResource(R.drawable.trivia);
        TextView tv = (TextView) activity.findViewById(R.id.textViewTriviaReady);
        tv.setText("Trivia Ready");
        Button startBtn = (Button) activity.findViewById(R.id.buttonStart);
        startBtn.setEnabled(true);

    }


    static public interface IQuestions{
        public void loadQuestions(ArrayList<Question> questions);
    }
}
