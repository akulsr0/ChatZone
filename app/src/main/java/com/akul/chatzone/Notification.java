package com.akul.chatzone;

//Created by AkulSrivastava
//May 2019

public class Notification {

//Created by AkulSrivastava
//May 2019

    String notification, status, image;

    public Notification(){

    }

    public Notification(String notification, String status, String image) {
        this.notification = notification;
        this.status = status;
        this.image = image;
    }

    public String getNotification() {
        return notification;
    }

    public void setNotification(String notification) {
        this.notification = notification;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}

//Created by AkulSrivastava
//May 2019
