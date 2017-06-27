package com.example.sweth.hw07;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
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

import static com.example.sweth.hw07.R.id.playLink;
import static com.example.sweth.hw07.R.id.progressBar;


public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    public MainActivity activity;
    public int click = 1;
    private ArrayList<Episode> mDataset = new ArrayList<Episode>();
    MediaPlayer player = new MediaPlayer();
    ProgressBar progressBar;
    ImageView ivPlay;
    ProgressDialog pd;

    public MyAdapter(ArrayList<Episode> mDataset, MainActivity activity) {
        this.mDataset = mDataset;
        this.activity = activity;
        this.progressBar = (ProgressBar) activity.findViewById(R.id.progressBar);
        this.ivPlay = (ImageView) activity.findViewById(R.id.pauseImageView);
        this.pd = new ProgressDialog(activity);
    }


    public static class ViewHolder extends RecyclerView.ViewHolder  {
        public TextView mTextViewList;
        public ImageView mImageViewList;
        public  ImageView playImageList;


        public ViewHolder(View itemView) {
            super(itemView);
            this.mTextViewList = (TextView) itemView.findViewById(R.id.episodeDetails);
            this.mImageViewList = (ImageView) itemView.findViewById(R.id.episodeImage);
            this.playImageList = (ImageView) itemView.findViewById(R.id.playLink);
        }

    }


    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v =  LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_view, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }


    @Override
    public void onBindViewHolder(final MyAdapter.ViewHolder holder, int position) {
        final Episode episode = mDataset.get(position);

        TextView tv = holder.mTextViewList;
        tv.setText(episode.getEpisodeTitle() + "\nPosted: " + episode.getPostedOn());

        ImageView iv = holder.mImageViewList;
        Picasso.with(activity.getApplicationContext()).load(episode.getImageUrl()).into(iv);

        ImageView iv2 = holder.playImageList;
        iv2.setImageResource(R.drawable.play_video);

        iv2.setOnClickListener(new View.OnClickListener() {
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
                            new MediaProgressThread().run();
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
            progressBar.postDelayed(new MediaProgressThread(), 1000);
        }

    }
}

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
