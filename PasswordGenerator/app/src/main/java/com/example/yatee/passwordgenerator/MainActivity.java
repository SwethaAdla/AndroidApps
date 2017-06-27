/*
Assignment # - InClass03
FileName - MainActivity.java
Group Members - Yateen Kedare | Swetha Adla
 */
package com.example.yatee.passwordgenerator;

import android.app.IntentService;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static android.R.attr.password;

public class MainActivity extends AppCompatActivity {

    Handler handler;
    ProgressDialog progressDialog;
    int totalPasswords,totalGeneratedPasswords = 0;
    final ArrayList<String> ThreadPasswords = new ArrayList<String>();
    final ArrayList<String> AsyncPasswords = new ArrayList<String>();

    @Override
    protected void onResume() {
        super.onResume();
        progressDialog= new ProgressDialog(this);
        progressDialog.setMessage("Generating Passwords");
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final SeekBar sbThreadPC = (SeekBar) findViewById(R.id.SBThreadPC);
        final SeekBar sbThreadPL = (SeekBar) findViewById(R.id.SBThreadPL);
        final SeekBar sbAsyncPC = (SeekBar) findViewById(R.id.SBAsyncPC);
        final SeekBar sbAsyncPL = (SeekBar) findViewById(R.id.SBAsyncPL);

        final TextView tvTPC = (TextView) findViewById(R.id.TVthreadPC);
        final TextView tvTPL = (TextView) findViewById(R.id.TVThreadPL);
        final TextView tvAPC = (TextView) findViewById(R.id.TVAsyncPC);
        final TextView tvAPL = (TextView) findViewById(R.id.TVAsyncPL);
        tvTPC.setText("4");tvTPL.setText("4");tvAPC.setText("4");tvAPL.setText("4");
        sbThreadPC.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvTPC.setText(Integer.toString(progress+1));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        sbThreadPL.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvTPL.setText(Integer.toString(progress+1));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        sbAsyncPC.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvAPC.setText(Integer.toString(progress+1));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        sbAsyncPL.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvAPL.setText(Integer.toString(progress+1));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                ThreadPasswords.add((String) msg.obj);
                Log.d("Thread Passwords",(String) msg.obj);

                if (totalGeneratedPasswords == totalPasswords) {
                        progressDialog.dismiss();
                        GotoNextActivity();
                    }
                    return true;

            }
        });





        Button GenerateButton = (Button) findViewById(R.id.button);
        GenerateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ThreadPasswords.clear();
                AsyncPasswords.clear();
                totalPasswords = sbThreadPC.getProgress()+1+sbAsyncPC.getProgress()+1;
                totalGeneratedPasswords = 0;
                progressDialog.setMax(totalPasswords);
                progressDialog.setProgress(0);
                ExecutorService threadPool;
                threadPool = Executors.newFixedThreadPool(1);
                threadPool.execute(new CalculateThreadPassword(sbThreadPC.getProgress()+1, sbThreadPL.getProgress()+1));
                new CalculateAsyncPassword().execute(sbAsyncPC.getProgress()+1, sbAsyncPL.getProgress()+1);
                progressDialog.show();

            }
        });


    }

    public void GotoNextActivity(){
        Intent intent = new Intent(getApplicationContext(), GeneratedPasswords.class);
        intent.putExtra("ThreadPasswords", ThreadPasswords);
        intent.putExtra("AsyncPasswords", AsyncPasswords);
        startActivity(intent);

    }

    class CalculateThreadPassword implements Runnable{
        private int length = 0, count = 0;
        CalculateThreadPassword(int count, int length){
            this.length = length;
            this.count = count;
        }
        @Override
        public void run() {
            for(int i = 0; i < count; i++){
                String password = Util.getPassword(length);
                progressDialog.incrementProgressBy(totalGeneratedPasswords++);
                Log.i("SOme---", ""+totalGeneratedPasswords);
                Message msg = new Message();
                msg.what = i;
                msg.obj = password;
                handler.sendMessage(msg);
            }
        }
    }

    class CalculateAsyncPassword extends AsyncTask<Integer,Integer,Void> {
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            progressDialog.incrementProgressBy(totalGeneratedPasswords++);
            Log.i("Async SOme---", ""+totalGeneratedPasswords);
            if (totalGeneratedPasswords == totalPasswords) {
                progressDialog.dismiss();
                GotoNextActivity();
            }

        }

        @Override
        protected Void doInBackground(Integer... params) {

            for(int i = 0; i < params[0]; i++) {
                String password = Util.getPassword(params[1]);
                AsyncPasswords.add(password);
                Log.d("Async Passwords",password+"  - "+i);
                publishProgress(i);
            }
            return null;
        }
    }
}
