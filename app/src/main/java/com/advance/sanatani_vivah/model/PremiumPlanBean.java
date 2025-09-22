package com.advance.sanatani_vivah.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PremiumPlanBean {
    @SerializedName("id")
    private String id;
    @SerializedName("status")
    private String status;
    @SerializedName("icon")
    private String icon;
    @SerializedName("category_name")
    private String categoryName;
    @SerializedName("extra_text")
    private String extraText;
    @SerializedName("offer_text")
    private String offerText;
    @SerializedName("color")
    private String color;
    @SerializedName("is_deleted")
    private String isDeleted;
    @SerializedName("plan_data")
    private List<PlanDatum> planData = null;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getExtraText() {
        return extraText;
    }

    public void setExtraText(String extraText) {
        this.extraText = extraText;
    }

    public String getOfferText() {
        return offerText;
    }

    public void setOfferText(String offerText) {
        this.offerText = offerText;
    }

    public String getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(String isDeleted) {
        this.isDeleted = isDeleted;
    }

    public List<PlanDatum> getPlanData() {
        return planData;
    }

    public void setPlanData(List<PlanDatum> planData) {
        this.planData = planData;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
