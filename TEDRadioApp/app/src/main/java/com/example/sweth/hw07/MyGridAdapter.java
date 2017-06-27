package com.example.sweth.hw07;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;


public class MyGridAdapter extends RecyclerView.Adapter<MyGridAdapter.ViewHolder> {

    public MainActivity activity;
    public int click = 1;
    private ArrayList<Episode> mDataset = new ArrayList<Episode>();
    MediaPlayer player = new MediaPlayer();
    ProgressBar progressBar;
    ImageView ivPlay;
    ProgressDialog pd;

    public MyGridAdapter(ArrayList<Episode> mDataset, MainActivity activity) {
        this.mDataset = mDataset;
        this.activity = activity;
        this.progressBar = (ProgressBar) activity.findViewById(R.id.progressBar);
        this.ivPlay = (ImageView) activity.findViewById(R.id.pauseImageView);
        this.pd = new ProgressDialog(activity);
    }


    public static class ViewHolder extends RecyclerView.ViewHolder  {
        public TextView mTextViewGrid;
        public ImageView mImageViewGrid;
        public ImageView mPlayImageGrid;


        public ViewHolder(View itemView) {
            super(itemView);
            this.mTextViewGrid = (TextView) itemView.findViewById(R.id.textViewGrid);
            this.mImageViewGrid = (ImageView) itemView.findViewById(R.id.imageViewGrid);
            this.mPlayImageGrid = (ImageView) itemView.findViewById(R.id.imagePlayIcon);
        }

    }


    @Override
    public MyGridAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v =  LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.grid_view, parent, false);
        MyGridAdapter.ViewHolder vh = new MyGridAdapter.ViewHolder(v);
        return vh;
    }


    @Override
    public void onBindViewHolder(MyGridAdapter.ViewHolder holder, int position) {
        final Episode episode = mDataset.get(position);

        TextView tv = holder.mTextViewGrid;
        tv.setText(episode.getEpisodeTitle());

        ImageView iv = holder.mImageViewGrid;
        Picasso.with(activity.getApplicationContext()).load(episode.getImageUrl()).into(iv);

        holder.mPlayImageGrid.setOnClickListener(new View.OnClickListener() {
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
                            ivPlay.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.VISIBLE);
                            if(episode.getDuration()!=null) {
                                progressBar.setMax(Integer.parseInt(episode.getDuration()));
                            }else if(player.getDuration()!=0){
                                progressBar.setMax(player.getDuration());
                            }else{
                                progressBar.setMax(100);
                            }
                            player.start();
                            new MyGridAdapter.MediaProgressThread().run();
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
                        ivPlay.setVisibility(View.INVISIBLE);
                        player.stop();
                        activity.isPlaying = false;

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
                        if (activity.isPlaying) {
                            ivPlay.setImageResource(R.drawable.play2_audio);
                            player.pause();
                            activity.isPlaying = false;
                        } else {
                            ivPlay.setImageResource(R.drawable.pause2_audio);
                            player.start();
                            new MyGridAdapter.MediaProgressThread().run();
                            activity.isPlaying = true;
                        }
                    }
                });

            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                player.stop();
                activity.isPlaying = false;
                ivPlay.setVisibility(View.INVISIBLE);
                progressBar.setVisibility(View.INVISIBLE);

                Intent i = new Intent(activity, PlayActivity.class);
                i.putExtra("EPISODE_KEY",(Serializable)episode);
                activity.startActivity(i);
            }
        });

    }
    class MediaProgressThread implements Runnable {

        @Override
        public void run() {

            if (player.isPlaying()) {
                int currentDuration = player.getCurrentPosition() / 1000;
                progressBar.setProgress(currentDuration);
                progressBar.postDelayed(new MyGridAdapter.MediaProgressThread(), 1000);
            }

        }
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}

