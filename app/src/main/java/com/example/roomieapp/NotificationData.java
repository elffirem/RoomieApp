package com.example.roomieapp;

public class NotificationData {

    private String title;
    private String body;
    private String senderEmail;

    public NotificationData() {
    }

    public NotificationData(String title, String body, String senderEmail) {
        this.title = title;
        this.body = body;
        this.senderEmail = senderEmail;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getSenderEmail() {
        return senderEmail;
    }

    public void setSenderEmail(String senderEmail) {
        this.senderEmail = senderEmail;
    }
}
