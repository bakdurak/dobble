package com.example.dobble.release.repositories.remote.payload.body;

public class SendMessageBody {
    private long destination;
    private String content;

    public SendMessageBody() { }

    public SendMessageBody(long destination, String content) {
        this.destination = destination;
        this.content = content;
    }

    public long getDestination() {
        return destination;
    }

    public void setDestination(long destination) {
        this.destination = destination;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
