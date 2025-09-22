package com.advance.sanatani_vivah.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Momin Nasirali on 20/11/21.
 */
public class ChatMemberBean {
    @SerializedName("member_id")
    private String memberId;
    @SerializedName("matri_id")
    private String matriId;
    @SerializedName("conversation_id")
    private String conversationId;
    @SerializedName("member_photo_url")
    private String memberPhotoUrl;
    @SerializedName("last_message")
    private String lastMessage;
    @SerializedName("last_message_date")
    private String lastMessageDate;
    @SerializedName("unread_message_count")
    private int unreadMessageCount;
    @SerializedName("is_block_us")
    private int isBlockUs;
    @SerializedName("is_online")
    private int isOnline;
    @SerializedName("is_read")
    private String isRead;

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getMatriId() {
        return matriId;
    }

    public void setMatriId(String matriId) {
        this.matriId = matriId;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public String getMemberPhotoUrl() {
        return memberPhotoUrl;
    }

    public void setMemberPhotoUrl(String memberPhotoUrl) {
        this.memberPhotoUrl = memberPhotoUrl;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public String getLastMessageDate() {
        return lastMessageDate;
    }

    public void setLastMessageDate(String lastMessageDate) {
        this.lastMessageDate = lastMessageDate;
    }

    public int getUnreadMessageCount() {
        return unreadMessageCount = 0;
    }

    public void setUnreadMessageCount(int unreadMessageCount) {
        this.unreadMessageCount = unreadMessageCount;
    }

    public int getIsBlockUs() {
        return isBlockUs;
    }

    public void setIsBlockUs(int isBlockUs) {
        this.isBlockUs = isBlockUs;
    }

    public int getIsOnline() {
        return isOnline;
    }

    public void setIsOnline(int isOnline) {
        this.isOnline = isOnline;
    }

    public String getIsRead() {
        return isRead;
    }

    public void setIsRead(String isRead) {
        this.isRead = isRead;
    }
}
