package com.example.sweth.hw07;

import android.app.ProgressDialog;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;


public class PlayActivity extends AppCompatActivity {
    Episode episode;
    int click = 1;
    MediaPlayer player = new MediaPlayer();
    ProgressBar progressBar;
    ImageView ivPlay;
    ProgressDialog pd;
    boolean isPlaying =false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setLogo(R.drawable.app_logo);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        progressBar = (ProgressBar) findViewById(R.id.progressBar3);
        ivPlay = (ImageView) findViewById(R.id.imageView2);
        pd=new ProgressDialog(this);

        if(getIntent().getExtras()!= null){
            episode  = (Episode) getIntent().getExtras().get("EPISODE_KEY");
            Log.d("demo", episode.toString());
            TextView tvTitle = (TextView) findViewById(R.id.textViewEpisodeTitle);
            tvTitle.setText(episode.getEpisodeTitle());

            TextView tvDesc = (TextView) findViewById(R.id.textViewDescription);
            tvDesc.setText("Description: "+episode.getDescription());

            TextView tvPubDate = (TextView) findViewById(R.id.textViewPublicationdate);
            tvPubDate.setText("Publication Date: "+episode.getPostedOn());

            TextView tvDuration = (TextView) findViewById(R.id.textViewDuration);
            tvDuration.setText("Duration: "+episode.getDuration());

            ImageView iv = (ImageView) findViewById(R.id.imageViewEpisodImage);
            Picasso.with(this).load(episode.getImageUrl()).into(iv);

            ivPlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        pd.setMessage("Loading episode..");
                        pd.setCancelable(false);
                        pd.show();
                        player.reset();
                        player.setDataSource(episode.getPlayLink());
                        player.prepareAsync();
                        player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                            @Override
                            public void onPrepared(MediaPlayer mp) {
                                pd.dismiss();
                                ivPlay.setImageResource(R.drawable.pause2_audio);
                                if(episode.getDuration()!=null) {
                                    progressBar.setMax(Integer.parseInt(episode.getDuration()));
                                }else if(player.getDuration()!=0){
                                    progressBar.setMax(player.getDuration());
                                }else{
                                    progressBar.setMax(100);
                                }
                                player.start();
                                new MediaProgressThread().run();
                            }
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    //removing play image and progressbar once the player is completed
                    player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            progressBar.setVisibility(View.INVISIBLE);
                            ivPlay.setImageResource(R.drawable.play2_audio);
                            player.stop();
                            isPlaying = false;

                        }
                    });

                    player.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
                        @Override
                        public void onBufferingUpdate(MediaPlayer mp, int percent) {
                            if (mp.isPlaying()) {
                                ivPlay.setBackgroundResource(R.drawable.pause2_audio);
                            }
                        }
                    });

                    //play/pause of audio
                    ivPlay.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (isPlaying) {
                                ivPlay.setImageResource(R.drawable.play2_audio);
                                player.pause();
                                isPlaying = false;
                            } else {
                                ivPlay.setImageResource(R.drawable.pause2_audio);
                                player.start();
                                new MediaProgressThread().run();
                                isPlaying = true;
                            }
                        }
                    });

                }

            });

        }else{
            Log.d("demo", getIntent().getExtras()+" is null");
        }
    }

    class MediaProgressThread implements Runnable {

        @Override
        public void run() {

            if (player.isPlaying()) {
                int currentDuration = player.getCurrentPosition() / 1000;
                progressBar.setProgress(currentDuration);
                progressBar.postDelayed(new MediaProgressThread(), 1000);
            }

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        player.stop();
        finish();
    }
}
