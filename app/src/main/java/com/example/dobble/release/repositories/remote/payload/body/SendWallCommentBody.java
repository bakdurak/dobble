package com.example.dobble.release.repositories.remote.payload.body;

public class SendWallCommentBody {
    private String content;
    private long wallId;
    private Long commentId;

    public SendWallCommentBody() { }

    public SendWallCommentBody(String content, long wallId, Long commentId) {
        this.content = content;
        this.wallId = wallId;
        this.commentId = commentId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getWallId() {
        return wallId;
    }

    public void setWallId(long wallId) {
        this.wallId = wallId;
    }

    public Long getCommentId() {
        return commentId;
    }

    public void setCommentId(Long commentId) {
        this.commentId = commentId;
    }
}
