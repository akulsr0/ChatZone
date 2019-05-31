package com.akul.chatzone;


//Created by AkulSrivastava
//May 2019


public class ChatUserGet
{
    public ChatUserGet(){

    }

    String imgurl, name, uid;

    public ChatUserGet(String uid) {
        this.uid = uid;
    }

    public ChatUserGet(String imgurl, String name) {
        this.imgurl = imgurl;
        this.name = name;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
