package com.advance.sanatani_vivah.model;

import com.google.gson.annotations.SerializedName;

public class PlanDatum {
    @SerializedName("id")
    private String id;
    @SerializedName("plan_name")
    private String planName;
    @SerializedName("badge")
    private String badge;
    @SerializedName("color")
    private String color;
    @SerializedName("category_type")
    private String categoryType;
    @SerializedName("offer_per")
    private String offerPer;
    @SerializedName("in_app_product_id")
    private Object inAppProductId;
    @SerializedName("plan_type")
    private String planType;
    @SerializedName("plan_amount")
    private String planAmount;
    @SerializedName("in_app_price")
    private Object inAppPrice;
    @SerializedName("plan_amount_type")
    private String planAmountType;
    @SerializedName("plan_duration")
    private String planDuration;
    @SerializedName("plan_contacts")
    private String planContacts;
    @SerializedName("profile")
    private String profile;
    @SerializedName("plan_msg")
    private String planMsg;
    @SerializedName("video")
    private String video;
    @SerializedName("chat")
    private String chat;
    @SerializedName("plan_offers")
    private String planOffers;
    @SerializedName("status")
    private String status;
    @SerializedName("is_deleted")
    private String isDeleted;
    @SerializedName("created_on")
    private String createdOn;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public String getBadge() {
        return badge;
    }

    public void setBadge(String badge) {
        this.badge = badge;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getCategoryType() {
        return categoryType;
    }

    public void setCategoryType(String categoryType) {
        this.categoryType = categoryType;
    }

    public String getOfferPer() {
        return offerPer;
    }

    public void setOfferPer(String offerPer) {
        this.offerPer = offerPer;
    }

    public Object getInAppProductId() {
        return inAppProductId;
    }

    public void setInAppProductId(Object inAppProductId) {
        this.inAppProductId = inAppProductId;
    }

    public String getPlanType() {
        return planType;
    }

    public void setPlanType(String planType) {
        this.planType = planType;
    }

    public String getPlanAmount() {
        return planAmount;
    }

    public void setPlanAmount(String planAmount) {
        this.planAmount = planAmount;
    }

    public Object getInAppPrice() {
        return inAppPrice;
    }

    public void setInAppPrice(Object inAppPrice) {
        this.inAppPrice = inAppPrice;
    }

    public String getPlanAmountType() {
        return planAmountType;
    }

    public void setPlanAmountType(String planAmountType) {
        this.planAmountType = planAmountType;
    }

    public String getPlanDuration() {
        return planDuration;
    }

    public void setPlanDuration(String planDuration) {
        this.planDuration = planDuration;
    }

    public String getPlanContacts() {
        return planContacts;
    }

    public void setPlanContacts(String planContacts) {
        this.planContacts = planContacts;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getPlanMsg() {
        return planMsg;
    }

    public void setPlanMsg(String planMsg) {
        this.planMsg = planMsg;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getChat() {
        return chat;
    }

    public void setChat(String chat) {
        this.chat = chat;
    }

    public String getPlanOffers() {
        return planOffers;
    }

    public void setPlanOffers(String planOffers) {
        this.planOffers = planOffers;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(String isDeleted) {
        this.isDeleted = isDeleted;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }
}
