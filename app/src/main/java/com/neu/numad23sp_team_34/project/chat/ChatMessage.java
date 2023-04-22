package com.neu.numad23sp_team_34.project.chat;

public class ChatMessage {
    private String senderId;
    private String senderName;
    private String message;

    public ChatMessage() {
    }

    public ChatMessage(String senderId, String senderName, String message) {
        this.senderId = senderId;
        this.senderName = senderName;
        this.message = message;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
