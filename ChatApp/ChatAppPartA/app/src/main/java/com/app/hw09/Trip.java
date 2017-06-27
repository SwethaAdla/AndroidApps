package com.app.hw09;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;


public class Trip implements Serializable{
    String title, location, photoUrl, createdByUid, key, createdDate;
    ArrayList<String> participantList;
    boolean isRemoved;

   /* public ArrayList<Messages> getMessagesList() {
        return messages;
    }

    public void setMessagesList(ArrayList<Messages> messagesList) {
        this.messages = messagesList;
    }

    ArrayList<Messages> messages;*/

    public boolean isRemoved() {
        return isRemoved;
    }

    public void setRemoved(boolean removed) {
        isRemoved = removed;
    }

    public ArrayList<String> getParticipantList() {
        return participantList;
    }

    public void setParticipantList(ArrayList<String> participantList) {
        this.participantList = participantList;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getCreatedByUid() {
        return createdByUid;
    }

    public void setCreatedByUid(String createdByUid) {
        this.createdByUid = createdByUid;
    }

    @Override
    public String toString() {
        return "Trip{" +
                "title='" + title + '\'' +
                ", location='" + location + '\'' +
                ", photoUrl='" + photoUrl + '\'' +
                ", createdByUid='" + createdByUid + '\'' +
                ", key='" + key + '\'' +
                ", createdDate='" + createdDate + '\'' +
                ", participantList=" + participantList +
                ", isRemoved=" + isRemoved +
                '}';
    }
}
