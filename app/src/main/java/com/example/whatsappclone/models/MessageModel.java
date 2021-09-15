package com.example.whatsappclone.models;

public class MessageModel {
    private String uid;
    private String messageText;
    private Long messageTimeStamp;

    public MessageModel() {
    }

    public MessageModel(String uid, String messageText, Long messageTimeStamp) {
        this.uid = uid;
        this.messageText = messageText;
        this.messageTimeStamp = messageTimeStamp;
    }

    public MessageModel(String uid, String messageText) {
        this.uid = uid;
        this.messageText = messageText;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public Long getMessageTimeStamp() {
        return messageTimeStamp;
    }

    public void setMessageTimeStamp(Long messageTimeStamp) {
        this.messageTimeStamp = messageTimeStamp;
    }

    @Override
    public String toString() {
        return "MessageModel{" +
                "uid='" + uid + '\'' +
                ", messageText='" + messageText + '\'' +
                ", messageTimeStamp=" + messageTimeStamp +
                '}';
    }
}
