package com.example.catsapp.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Message {
    private String messageId, message, senderId, imageUrl, receiver, url, type;
    private long timestamp;
    private int feeling = -1;

    @SerializedName("usename")
    @Expose
    private String usename;

    @SerializedName("messages")
    @Expose
    private String messages;

    public Message(String usename, String messages)
    {
        this.usename = usename;
        this.messages = messages;
    }

    public Message() { }

    public Message(String messageId, String message, String senderId, String imageUrl, String receiver, String url, String type, long timestamp, int feeling)
    {
        this.messageId = messageId;
        this.message = message;
        this.senderId = senderId;
        this.imageUrl = imageUrl;
        this.receiver = receiver;
        this.url = url;
        this.type = type;
        this.timestamp = timestamp;
        this.feeling = feeling;
    }

    public Message(String messageTxt, String senderUid, long time) {
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Message(String currentDate, String text, String s, String text1, String uid, String receiverID) {
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public int getFeeling() {
        return feeling;
    }

    public void setFeeling(int feeling) {
        this.feeling = feeling;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getUsename() {
        return usename;
    }

    public void setUsename(String usename) {
        this.usename = usename;
    }

    public String getMessages() {
        return messages;
    }

    public void setMessages(String messages) {
        this.messages = messages;
    }
}
