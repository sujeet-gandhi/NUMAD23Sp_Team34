package com.neu.numad23sp_team_34.sticktoem.models;

public class Message {
    private String senderUsername;
    private String receiverUsername;
    private String stickerId;

    private byte[] stickerImage;

    private long timestamp;

    public Message() {}

    public Message(String senderUsername, String receiverUsername, String stickerId, long timestamp) {
        this.senderUsername = senderUsername;
        this.receiverUsername = receiverUsername;
        this.stickerId = stickerId;
        this.stickerImage = stickerImage;
        this.timestamp = timestamp;
    }


    public String getSenderUsername() {
        return senderUsername;
    }

    public String getReceiverUsername() {
        return receiverUsername;
    }

    public String getStickerId() {
        return stickerId;
    }

    public byte[] getStickerImage() {
        return stickerImage;
    }

    public long getTimestamp() {
        return timestamp;
    }
}

