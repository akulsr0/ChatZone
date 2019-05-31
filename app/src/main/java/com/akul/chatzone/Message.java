package com.akul.chatzone;

//Created by AkulSrivastava
//May 2019

public class Message
{
//Created by AkulSrivastava
//May 2019


    public Message(){

    }

    String time, message, name, imgurl;

    public Message(String time, String message, String name, String imgurl) {
        this.time = time;
        this.message = message;
        this.name = name;
        this.imgurl = imgurl;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }
}

//Created by AkulSrivastava
//May 2019
