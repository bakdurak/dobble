package com.example.dobble.release.repositories.remote.payload.response;


import com.example.dobble.release.repositories.local.entities.CommentEntity;
import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;

public class CommentsResponse {
    private Long id;
    private Long avatarId;
    private Long userId;
    private Long linkCnt;
    private String firstName;
    private String lastName;
    private String content;
    private List<SubCommentResponse> comments;

    @Expose(serialize = false)
    private boolean pending = false;

    public CommentsResponse() {}

    public CommentsResponse(long id, Long avatarId, long userId, Long linkCnt,
                            String firstName, String lastName, String content,
                            List<SubCommentResponse> comments) {
        this.id = id;
        this.avatarId = avatarId;
        this.userId = userId;
        this.linkCnt = linkCnt;
        this.firstName = firstName;
        this.lastName = lastName;
        this.content = content;
        this.comments = comments;
    }

    public CommentsResponse(CommentEntity entity) {
        id = entity.getId();
        avatarId = entity.getAvatarId();
        userId = entity.getUserId();
        linkCnt = entity.getLinkCnt();
        firstName = entity.getFirstName();
        lastName = entity.getLastName();
        content = entity.getContent();

        if (entity.getSubComments() == null) {
            return;
        }
        comments = new ArrayList<>();
        for(CommentEntity.SubComment sc : entity.getSubComments()) {
            SubCommentResponse subComment = new SubCommentResponse(sc.getId(), sc.getUserId(),
                sc.getAvatarId(), sc.getContent());
            comments.add(subComment);
        }
    }

    public void refresh(SendWallCommentResponse r) {
        id = r.getId();
        linkCnt = r.getLinkCnt();
        firstName = r.getFirstName();
        lastName = r.getLastName();
        content = r.getContent();
        avatarId = r.getAvatarId();
        pending = false;
    }

    public Long getId() {
        return id;
    }

    public CommentsResponse setId(Long id) {
        this.id = id;
        return this;
    }

    public Long getAvatarId() {
        return avatarId;
    }

    public CommentsResponse setAvatarId(long avatarId) {
        this.avatarId = avatarId;
        return this;
    }

    public Long getUserId() {
        return userId;
    }

    public CommentsResponse setUserId(Long userId) {
        this.userId = userId;
        return this;
    }

    public Long getLinkCnt() {
        return linkCnt;
    }

    public CommentsResponse setLinkCnt(Long linkCnt) {
        this.linkCnt = linkCnt;
        return this;
    }

    public String getFirstName() {
        return firstName;
    }

    public CommentsResponse setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public CommentsResponse setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public String getContent() {
        return content;
    }

    public CommentsResponse setContent(String content) {
        this.content = content;
        return this;
    }

    public List<SubCommentResponse> getComments() {
        return comments;
    }

    public CommentsResponse setComments(List<SubCommentResponse> comments) {
        this.comments = comments;
        return this;
    }

    public boolean isPending() {
        return pending;
    }

    public CommentsResponse setPending(boolean pending) {
        this.pending = pending;
        return this;
    }
}
