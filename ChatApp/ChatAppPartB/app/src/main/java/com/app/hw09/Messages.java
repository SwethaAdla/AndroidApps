package com.app.hw09;

import java.io.Serializable;


public class Messages implements Serializable {
    String text, sentBy, sentTime, tripId, imageUrl, key;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getSentBy() {
        return sentBy;
    }

    public void setSentBy(String sentBy) {
        this.sentBy = sentBy;
    }

    public String getSentTime() {
        return sentTime;
    }

    public void setSentTime(String sentTime) {
        this.sentTime = sentTime;
    }

    public String getTripId() {
        return tripId;
    }

    public void setTripId(String tripId) {
        this.tripId = tripId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return "Messages{" +
                "text='" + text + '\'' +
                ", sentBy='" + sentBy + '\'' +
                ", sentTime='" + sentTime + '\'' +
                ", tripId='" + tripId + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", key='" + key + '\'' +
                '}';
    }
}
