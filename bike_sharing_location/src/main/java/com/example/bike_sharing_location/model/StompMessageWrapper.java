package com.example.bike_sharing_location.model;

public class StompMessageWrapper {
    private String content;
    public StompMessageWrapper(String content){
        this.content = content;
    }
    public StompMessageWrapper(){

    }

    public void setContent(String content){
        this.content = content;
    }
    public String getContent(){return this.content;}
}
