package com.akul.chatzone;

public class Post
{
//Created by AkulSrivastava
//May 2019

    public Post(){

    }

    String uname, uuid, uurl, imgurl, title;

    public Post(String uname, String uuid, String uurl, String imgurl, String title) {
        this.uname = uname;
        this.uuid = uuid;
        this.uurl = uurl;
        this.imgurl = imgurl;
        this.title = title;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getUurl() {
        return uurl;
    }

    public void setUurl(String uurl) {
        this.uurl = uurl;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}

//Created by AkulSrivastava
//May 2019
