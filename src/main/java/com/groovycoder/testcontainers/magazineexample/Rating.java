package com.groovycoder.testcontainers.magazineexample;

import java.util.UUID;

public class Rating {

    private UUID talkId;
    private int value;

    public Rating(UUID talkId, int value) {
        this.talkId = talkId;
        this.value = value;
    }

    public UUID getTalkId() {
        return talkId;
    }

    public void setTalkId(UUID talkId) {
        this.talkId = talkId;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
