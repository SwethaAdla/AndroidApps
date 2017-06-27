package com.example.sweth.hw07;

import java.io.Serializable;
import java.util.Date;

public class Episode implements Serializable{

    String episodeTitle;
    String description;
    String duration;
    String imageUrl;
    String postedOn;
    String playLink;
    Date publishedDate;

    public Episode(){

    }

    public String getEpisodeTitle() {
        return episodeTitle;
    }

    public void setEpisodeTitle(String episodeTitle) {
        this.episodeTitle = episodeTitle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getPostedOn() {
        return postedOn;
    }

    public void setPostedOn(String postedOn) {
        this.postedOn = postedOn;
    }

    public String getPlayLink() {
        return playLink;
    }

    public void setPlayLink(String playLink) {
        this.playLink = playLink;
    }

    public Date getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(Date publishedDate) {
        this.publishedDate = publishedDate;
    }

    @Override
    public String toString() {
        return "Episode{" +
                "episodeTitle='" + episodeTitle + '\'' +
                ", description='" + description + '\'' +
                ", duration='" + duration + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", postedOn='" + postedOn + '\'' +
                ", playLink='" + playLink + '\'' +
                ", publishedDate=" + publishedDate +
                '}';
    }
}
