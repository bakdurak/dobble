package com.example.dobble.release.repositories.remote.payload.body;

public class AddFriendBody {
    long target;

    public AddFriendBody() { }

    public AddFriendBody(long target) {
        this.target = target;
    }

    public long getTarget() {
        return target;
    }

    public void setTarget(long target) {
        this.target = target;
    }
}
