package com.example.dobble.release.repositories.remote.payload.body;

public class ApproveFriendBody {
    private long target;

    public ApproveFriendBody() { }

    public ApproveFriendBody(long target) {
        this.target = target;
    }

    public long getTarget() {
        return target;
    }

    public void setTarget(long target) {
        this.target = target;
    }
}
