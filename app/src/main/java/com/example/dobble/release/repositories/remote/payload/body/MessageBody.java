package com.example.dobble.release.repositories.remote.payload.body;

public class MessageBody {
    private String content;
    private long destination;

    public MessageBody() { }

    public MessageBody(String content, long destination) {
        this.content = content;
        this.destination = destination;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getDestination() {
        return destination;
    }

    public void setDestination(long destination) {
        this.destination = destination;
    }
}
