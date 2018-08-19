package com.groovycoder.testcontainers.magazineexample;

public class Talk {

    private String title;
    private String speaker;

    public Talk(String title, String speaker) {
        this.title = title;
        this.speaker = speaker;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSpeaker() {
        return speaker;
    }

    public void setSpeaker(String speaker) {
        this.speaker = speaker;
    }
}
