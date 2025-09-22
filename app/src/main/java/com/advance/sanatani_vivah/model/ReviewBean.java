package com.advance.sanatani_vivah.model;

import com.google.gson.annotations.SerializedName;

public class ReviewBean {
    @SerializedName("id")
    private String id;
    @SerializedName("vendor_id")
    private String vendorId;
    @SerializedName("r_name")
    private String rName;
    @SerializedName("r_email")
    private String rEmail;
    @SerializedName("r_title")
    private String rTitle;
    @SerializedName("r_message")
    private String rMessage;
    @SerializedName("r_star")
    private String rStar;
    @SerializedName("r_date")
    private String rDate;
    @SerializedName("status")
    private String status;
    @SerializedName("is_deleted")
    private String isDeleted;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVendorId() {
        return vendorId;
    }

    public void setVendorId(String vendorId) {
        this.vendorId = vendorId;
    }

    public String getrName() {
        return rName;
    }

    public void setrName(String rName) {
        this.rName = rName;
    }

    public String getrEmail() {
        return rEmail;
    }

    public void setrEmail(String rEmail) {
        this.rEmail = rEmail;
    }

    public String getrTitle() {
        return rTitle;
    }

    public void setrTitle(String rTitle) {
        this.rTitle = rTitle;
    }

    public String getrMessage() {
        return rMessage;
    }

    public void setrMessage(String rMessage) {
        this.rMessage = rMessage;
    }

    public String getrStar() {
        return rStar;
    }

    public void setrStar(String rStar) {
        this.rStar = rStar;
    }

    public String getrDate() {
        return rDate;
    }

    public void setrDate(String rDate) {
        this.rDate = rDate;
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
}
