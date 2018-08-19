package com.groovycoder.testcontainers.magazineexample;

import java.util.Objects;
import java.util.UUID;

public class Talk {

    private UUID id;
    private String title;
    private String speaker;

    public Talk(UUID id, String title, String speaker) {
        this.id = id;
        this.title = title;
        this.speaker = speaker;
    }

    public String getTitle() {
        return title;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Talk talk = (Talk) o;
        return Objects.equals(id, talk.id) &&
                Objects.equals(title, talk.title) &&
                Objects.equals(speaker, talk.speaker);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, speaker);
    }
}
