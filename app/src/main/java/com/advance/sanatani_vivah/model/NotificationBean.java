package com.advance.sanatani_vivah.model;

import com.google.gson.annotations.SerializedName;

public class NotificationBean {
    @SerializedName("id")
    private String id;
    @SerializedName("sender_id")
    private String senderId;
    @SerializedName("receiver_id")
    private String receiverId;
    @SerializedName("notification_type")
    private String notificationType;
    @SerializedName("title")
    private String title;
    @SerializedName("message")
    private String message;
    @SerializedName("read_status")
    private String readStatus;
    @SerializedName("app_type")
    private String appType;
    @SerializedName("created_on")
    private String createdOn;
    @SerializedName("photo_url")
    private String image;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public String getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(String notificationType) {
        this.notificationType = notificationType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getReadStatus() {
        return readStatus;
    }

    public void setReadStatus(String readStatus) {
        this.readStatus = readStatus;
    }

    public String getAppType() {
        return appType;
    }

    public void setAppType(String appType) {
        this.appType = appType;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }


    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
