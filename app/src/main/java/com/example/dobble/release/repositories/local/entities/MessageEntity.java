package com.example.dobble.release.repositories.local.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.dobble.release.repositories.remote.payload.response.MessageResponse;

@Entity
public class MessageEntity {
    @PrimaryKey
    private Long msgId;
    private String msgContent;
    private String msgDate;
    private Long srcId;
    private String srcFirstName;
    private String srcLastName;
    private Long srcAvatarId;
    private Long dstId;
    private String dstFirstName;
    private String dstLastName;
    private Long dstAvatarId;

    public MessageEntity() { }

    public MessageEntity(MessageResponse r) {
        msgId = r.getId();
        msgContent = r.getContent();
        msgDate = r.getDate();
        srcId = r.getSrcId();
        srcFirstName = r.getSrcFirstName();
        srcLastName = r.getSrcLastName();
        srcAvatarId = r.getSrcAvatarId();
        dstId = r.getDstId();
        dstFirstName = r.getDstFirstName();
        dstLastName = r.getDstLastName();
        dstAvatarId = r.getDstAvatarId();
    }

    public Long getMsgId() {
        return msgId;
    }

    public void setMsgId(Long msgId) {
        this.msgId = msgId;
    }

    public String getMsgContent() {
        return msgContent;
    }

    public void setMsgContent(String msgContent) {
        this.msgContent = msgContent;
    }

    public String getMsgDate() {
        return msgDate;
    }

    public void setMsgDate(String msgDate) {
        this.msgDate = msgDate;
    }

    public Long getSrcId() {
        return srcId;
    }

    public void setSrcId(Long srcId) {
        this.srcId = srcId;
    }

    public String getSrcFirstName() {
        return srcFirstName;
    }

    public void setSrcFirstName(String srcFirstName) {
        this.srcFirstName = srcFirstName;
    }

    public String getSrcLastName() {
        return srcLastName;
    }

    public void setSrcLastName(String srcLastName) {
        this.srcLastName = srcLastName;
    }

    public Long getSrcAvatarId() {
        return srcAvatarId;
    }

    public void setSrcAvatarId(Long srcAvatarId) {
        this.srcAvatarId = srcAvatarId;
    }

    public Long getDstId() {
        return dstId;
    }

    public void setDstId(Long dstId) {
        this.dstId = dstId;
    }

    public String getDstFirstName() {
        return dstFirstName;
    }

    public void setDstFirstName(String dstFirstName) {
        this.dstFirstName = dstFirstName;
    }

    public String getDstLastName() {
        return dstLastName;
    }

    public void setDstLastName(String dstLastName) {
        this.dstLastName = dstLastName;
    }

    public Long getDstAvatarId() {
        return dstAvatarId;
    }

    public void setDstAvatarId(Long dstAvatarId) {
        this.dstAvatarId = dstAvatarId;
    }
}
