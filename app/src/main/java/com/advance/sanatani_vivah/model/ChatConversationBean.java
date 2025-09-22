package com.advance.sanatani_vivah.model;

import com.google.gson.annotations.SerializedName;

public class ChatConversationBean {
    @SerializedName("id")
    private String id;
    @SerializedName("conversation_id")
    private String conversationId;
    @SerializedName("sender_member_id")
    private String senderMemberId;
    @SerializedName("sender_member_matri_id")
    private String senderMemberMatriId;
    @SerializedName("receiver_member_id")
    private String receiverMemberId;
    @SerializedName("receiver_member_matri_id")
    private String receiverMemberMatriId;
    @SerializedName("message")
    private String message;
    @SerializedName("is_read")
    private String isRead;
    @SerializedName("send_on")
    private String sendOn;
    @SerializedName("is_deleted")
    private String isDeleted;
    @SerializedName("is_sender")
    private boolean isSender;
    @SerializedName("is_receiver")
    private boolean isReceiver;
    public boolean is_checked;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public String getSenderMemberId() {
        return senderMemberId;
    }

    public void setSenderMemberId(String senderMemberId) {
        this.senderMemberId = senderMemberId;
    }

    public String getSenderMemberMatriId() {
        return senderMemberMatriId;
    }

    public void setSenderMemberMatriId(String senderMemberMatriId) {
        this.senderMemberMatriId = senderMemberMatriId;
    }

    public String getReceiverMemberId() {
        return receiverMemberId;
    }

    public void setReceiverMemberId(String receiverMemberId) {
        this.receiverMemberId = receiverMemberId;
    }

    public String getReceiverMemberMatriId() {
        return receiverMemberMatriId;
    }

    public void setReceiverMemberMatriId(String receiverMemberMatriId) {
        this.receiverMemberMatriId = receiverMemberMatriId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getIsRead() {
        return isRead;
    }

    public void setIsRead(String isRead) {
        this.isRead = isRead;
    }

    public String getSendOn() {
        return sendOn;
    }

    public void setSendOn(String sendOn) {
        this.sendOn = sendOn;
    }

    public String getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(String isDeleted) {
        this.isDeleted = isDeleted;
    }

    public boolean getIsSender() {
        return isSender;
    }

    public void setIsSender(boolean isSender) {
        this.isSender = isSender;
    }

    public boolean getIsReceiver() {
        return isReceiver;
    }

    public void setIsReceiver(boolean isReceiver) {
        this.isReceiver = isReceiver;
    }

    public boolean isSender() {
        return isSender;
    }

    public void setSender(boolean sender) {
        isSender = sender;
    }

    public boolean isReceiver() {
        return isReceiver;
    }

    public void setReceiver(boolean receiver) {
        isReceiver = receiver;
    }

    public boolean isIs_checked() {
        return is_checked;
    }

    public void setIs_checked(boolean is_checked) {
        this.is_checked = is_checked;
    }
}
