package com.example.dobble.release.repositories.remote.payload.response;

import com.example.dobble.release.ui.adapters.WallAdapter;
import com.google.gson.annotations.Expose;

public class SendWallCommentResponse {
    private Long id;
    private Long linkCnt;
    private String content;
    private Long sender;
    private Long wallId;
    private String firstName;
    private String lastName;
    private Long avatarId;

    public SendWallCommentResponse() { }

    public SendWallCommentResponse(long id, Long linkCnt, String content, long sender, long wallId,
                                   String firstName, String lastName, Long avatarId) {
        this.id = id;
        this.linkCnt = linkCnt;
        this.content = content;
        this.sender = sender;
        this.wallId = wallId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.avatarId = avatarId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getLinkCnt() {
        return linkCnt;
    }

    public void setLinkCnt(Long linkCnt) {
        this.linkCnt = linkCnt;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getSender() {
        return sender;
    }

    public void setSender(Long sender) {
        this.sender = sender;
    }

    public Long getWallId() {
        return wallId;
    }

    public void setWallId(Long wallId) {
        this.wallId = wallId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Long getAvatarId() {
        return avatarId;
    }

    public void setAvatarId(Long avatarId) {
        this.avatarId = avatarId;
    }
}
