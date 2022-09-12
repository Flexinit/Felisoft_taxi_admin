package com.example.drupp_driver.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ChatReceivedMessage extends BaseMessage {
    @SerializedName("chatId")
    @Expose
    private String chatId;
    @SerializedName("senderId")
    @Expose
    private int senderId;
    @SerializedName(("messageId"))
    @Expose
    private String messageId;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("imageMessage")
    @Expose
    private String imageMessage;
    @SerializedName("isType")
    @Expose
    private String isType;
    @SerializedName("receiverid")
    @Expose
    private String receiverId;
    @SerializedName("receiver")
    @Expose
    private String receiver;
    @SerializedName("createdAt")
    @Expose
    private long createdAt;
    @SerializedName("created_at")
    @Expose
    private String created_at;
    @SerializedName("receiver_id")
    @Expose
    private String receiver_id;
    @SerializedName("sender_id")
    @Expose
    private String sender_id;


    public ChatReceivedMessage(String message, String mUsername) {
        this.message = message;
        receiver = mUsername;
    }

    public ChatReceivedMessage() {

    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getReceiver_id() {
        return receiver_id;
    }

    public void setReceiver_id(String receiver_id) {
        this.receiver_id = receiver_id;
    }

    public String getSender_id() {
        return sender_id;
    }

    public void setSender_id(String sender_id) {
        this.sender_id = sender_id;
    }

    public String getImageMessage() {
        return imageMessage;
    }

    public void setImageMessage(String imageMessage) {
        this.imageMessage = imageMessage;
    }

    public String getIsType() {
        return isType;
    }

    public void setIsType(String isType) {
        this.isType = isType;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public String getChatId() {
        return chatId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }

    public int getSenderId() {
        return senderId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getReceiver() {
        return receiver;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }
}
