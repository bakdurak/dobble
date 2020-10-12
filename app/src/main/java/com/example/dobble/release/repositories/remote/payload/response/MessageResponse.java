package com.example.dobble.release.repositories.remote.payload.response;

import com.example.dobble.release.repositories.local.entities.MessageEntity;

import org.json.JSONException;
import org.json.JSONObject;

public class MessageResponse {
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

    public MessageResponse() { }

    public MessageResponse(MessageEntity e) {
        msgId = e.getMsgId();
        msgContent = e.getMsgContent();
        msgDate = e.getMsgDate();
        srcId = e.getSrcId();
        srcFirstName = e.getSrcFirstName();
        srcLastName = e.getSrcLastName();
        srcAvatarId = e.getSrcAvatarId();
        dstId = e.getDstId();
        dstFirstName = e.getDstFirstName();
        dstLastName = e.getDstLastName();
        dstAvatarId = e.getDstAvatarId();
    }

    public MessageResponse(JSONObject jsonMsg) throws JSONException {
        msgId = jsonMsg.getLong("msgId");
        msgContent = jsonMsg.getString("msgContent");
        msgDate = jsonMsg.getString("msgDate");
        srcId = jsonMsg.getLong("srcId");
        srcFirstName = jsonMsg.getString("srcFirstName");
        srcLastName = jsonMsg.getString("srcLastName");
        dstId = jsonMsg.getLong("dstId");
        dstFirstName = jsonMsg.getString("dstFirstName");
        dstLastName = jsonMsg.getString("dstLastName");

        if (!jsonMsg.isNull("srcAvatarId")) {
            srcAvatarId = jsonMsg.getLong("srcAvatarId");
        }
        if (!jsonMsg.isNull("dstAvatarId")) {
            dstAvatarId = jsonMsg.getLong("dstAvatarId");
        }
    }

    public Long getId() {
        return msgId;
    }

    public void setId(long id) {
        this.msgId = id;
    }

    public String getContent() {
        return msgContent;
    }

    public void setContent(String content) {
        this.msgContent = content;
    }

    public String getDate() {
        return msgDate;
    }

    public void setDate(String date) {
        this.msgDate = date;
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
