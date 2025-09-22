package com.advance.sanatani_vivah.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Momin Nasirali on 22/11/21.
 */
public class CreateConversationBean {
    @SerializedName("conversation_id")
    private String conversationId;
    @SerializedName("sender_member_id")
    private String senderMemberId;
    @SerializedName("sender_member_matri_id")
    private String senderMemberMatriId;
    @SerializedName("sender_member_photo_url")
    private String senderMemberPhotoUrl;
    @SerializedName("receiver_member_id")
    private String receiverMemberId;
    @SerializedName("receiver_member_matri_id")
    private String receiverMemberMatriId;
    @SerializedName("receiver_member_photo_url")
    private String receiverMemberPhotoUrl;
    @SerializedName("message_list")
    private List<ChatConversationBean> conversationList = new ArrayList<>();

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

    public String getSenderMemberPhotoUrl() {
        return senderMemberPhotoUrl;
    }

    public void setSenderMemberPhotoUrl(String senderMemberPhotoUrl) {
        this.senderMemberPhotoUrl = senderMemberPhotoUrl;
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

    public String getReceiverMemberPhotoUrl() {
        return receiverMemberPhotoUrl;
    }

    public void setReceiverMemberPhotoUrl(String receiverMemberPhotoUrl) {
        this.receiverMemberPhotoUrl = receiverMemberPhotoUrl;
    }

    public List<ChatConversationBean> getConversationList() {
        return conversationList;
    }

    public void setConversationList(List<ChatConversationBean> conversationList) {
        this.conversationList = conversationList;
    }
}
