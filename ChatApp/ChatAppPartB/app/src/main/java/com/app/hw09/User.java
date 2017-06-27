package com.app.hw09;

import java.util.ArrayList;


public class User {
    String email,fname, lname,password, gender, profilePicUrl, key;
    ArrayList<String> friendList ;
    ArrayList<String> sentFrnReqList ;
    ArrayList<String> receivdFrnReqList ;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public ArrayList<String> getSentFrnReqList() {
        return sentFrnReqList;
    }

    public void setSentFrnReqList(ArrayList<String> sentFrnReqList) {
        this.sentFrnReqList = sentFrnReqList;
    }

    public ArrayList<String> getReceivdFrnReqList() {
        return receivdFrnReqList;
    }

    public void setReceivdFrnReqList(ArrayList<String> receivdFrnReqList) {
        this.receivdFrnReqList = receivdFrnReqList;
    }

    public ArrayList<String> getFriendList() {
        return friendList;
    }

    public void setFriendList(ArrayList<String> friendList) {
        this.friendList = friendList;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getProfilePicUrl() {
        return profilePicUrl;
    }

    public void setProfilePicUrl(String profilePicUrl) {
        this.profilePicUrl = profilePicUrl;
    }

    @Override
    public String toString() {
        return "User{" +
                "email='" + email + '\'' +
                ", fname='" + fname + '\'' +
                ", lname='" + lname + '\'' +
                ", password='" + password + '\'' +
                ", gender='" + gender + '\'' +
                ", profilePicUrl='" + profilePicUrl + '\'' +
                ", key='" + key + '\'' +
                ", friendList=" + friendList +
                ", sentFrnReqList=" + sentFrnReqList +
                ", receivdFrnReqList=" + receivdFrnReqList +
                '}';
    }

}

