package com.akul.chatzone;

//Created by AkulSrivastava
//May 2019

public class Follower
{
    public Follower(){

    }

    private String displayimage, fullname, uid;

    public Follower(String displayimage, String fullname, String uid) {
        this.displayimage = displayimage;
        this.fullname = fullname;
        this.uid = uid;
    }

    public String getDisplayimage() {
        return displayimage;
    }

    public void setDisplayimage(String displayimage) {
        this.displayimage = displayimage;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}

//Created by AkulSrivastava
//May 2019
