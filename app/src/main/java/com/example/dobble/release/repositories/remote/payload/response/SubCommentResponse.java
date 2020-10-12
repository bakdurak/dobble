package com.example.dobble.release.repositories.remote.payload.response;

public class SubCommentResponse {
    private Long id;
    private Long userId;
    private Long avatarId;
    private String content;

    public SubCommentResponse() { }

    public SubCommentResponse(Long id, Long userId, Long avatarId, String content) {
        this.id = id;
        this.userId = userId;
        this.avatarId = avatarId;
        this.content = content;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getAvatarId() {
        return avatarId;
    }

    public void setAvatarId(Long avatarId) {
        this.avatarId = avatarId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
