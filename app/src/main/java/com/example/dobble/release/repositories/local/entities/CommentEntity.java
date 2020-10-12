package com.example.dobble.release.repositories.local.entities;

import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.dobble.release.repositories.local.entities.converters.SubCommentsConverter;
import com.example.dobble.release.repositories.remote.payload.response.CommentsResponse;
import com.example.dobble.release.repositories.remote.payload.response.SubCommentResponse;

import java.util.ArrayList;
import java.util.List;

@Entity
public class CommentEntity {

    public static class SubComment {
        private Long id;
        private Long userId;
        private Long avatarId;
        private String content;

        public SubComment() { }

        public SubComment(SubCommentResponse sc) {
            id = sc.getId();
            userId = sc.getUserId();
            avatarId = sc.getAvatarId();
            content = sc.getContent();
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

    @PrimaryKey
    private Long id;
    private Long avatarId;
    private Long userId;
    private Long linkCnt;
    private String firstName;
    private String lastName;
    private String content;

    @TypeConverters(SubCommentsConverter.class)
    private List<SubComment> subComments;

    public CommentEntity() { }

    @Ignore
    public CommentEntity(CommentsResponse response) {
        id = response.getId();
        avatarId = response.getAvatarId();
        userId = response.getUserId();
        linkCnt = response.getLinkCnt();
        firstName = response.getFirstName();
        lastName = response.getLastName();
        content = response.getContent();
        if (response.getComments() != null && !response.getComments().isEmpty()) {
            subComments = new ArrayList<>();
            for(SubCommentResponse sc : response.getComments()) {
                SubComment subComment = new SubComment(sc);
                subComments.add(subComment);
            }
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAvatarId() {
        return avatarId;
    }

    public void setAvatarId(Long avatarId) {
        this.avatarId = avatarId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getLinkCnt() {
        return linkCnt;
    }

    public void setLinkCnt(Long linkCnt) {
        this.linkCnt = linkCnt;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<SubComment> getSubComments() {
        return subComments;
    }

    public void setSubComments(List<SubComment> subComments) {
        this.subComments = subComments;
    }
}
