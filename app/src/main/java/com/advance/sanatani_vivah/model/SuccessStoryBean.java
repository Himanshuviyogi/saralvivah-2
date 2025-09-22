package com.advance.sanatani_vivah.model;

import com.google.gson.annotations.SerializedName;

public class SuccessStoryBean {

    public SuccessStoryBean() {

    }

    @SerializedName("id")
    private String id;
    @SerializedName("status")
    private String status;
    @SerializedName("weddingphoto")
    private String weddingphoto;
    @SerializedName("weddingphoto_type")
    private String weddingphotoType;
    @SerializedName("bridename")
    private String bridename;
    @SerializedName("brideid")
    private String brideid;
    @SerializedName("groomname")
    private String groomname;
    @SerializedName("groomid")
    private String groomid;
    @SerializedName("marriagedate")
    private String marriagedate;
    @SerializedName("seo_title")
    private String seoTitle;
    @SerializedName("seo_description")
    private String seoDescription;
    @SerializedName("seo_keywords")
    private String seoKeywords;
    @SerializedName("og_title")
    private String ogTitle;
    @SerializedName("og_image")
    private String ogImage;
    @SerializedName("og_description")
    private String ogDescription;
    @SerializedName("successmessage")
    private String successmessage;
    @SerializedName("created_on")
    private String createdOn;
    @SerializedName("is_deleted")
    private String isDeleted;

    private boolean isDisplayFullMsg = false;

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

    public String getWeddingphoto() {
        return  weddingphoto;
    }

    public void setWeddingphoto(String weddingphoto) {
        this.weddingphoto = weddingphoto;
    }

    public String getWeddingphotoType() {
        return weddingphotoType;
    }

    public void setWeddingphotoType(String weddingphotoType) {
        this.weddingphotoType = weddingphotoType;
    }

    public String getBridename() {
        return bridename;
    }

    public void setBridename(String bridename) {
        this.bridename = bridename;
    }

    public String getBrideid() {
        return brideid;
    }

    public void setBrideid(String brideid) {
        this.brideid = brideid;
    }

    public String getGroomname() {
        return groomname;
    }

    public void setGroomname(String groomname) {
        this.groomname = groomname;
    }

    public String getGroomid() {
        return groomid;
    }

    public void setGroomid(String groomid) {
        this.groomid = groomid;
    }

    public String getMarriagedate() {
        return marriagedate;
    }

    public void setMarriagedate(String marriagedate) {
        this.marriagedate = marriagedate;
    }

    public String getSeoTitle() {
        return seoTitle;
    }

    public void setSeoTitle(String seoTitle) {
        this.seoTitle = seoTitle;
    }

    public String getSeoDescription() {
        return seoDescription;
    }

    public void setSeoDescription(String seoDescription) {
        this.seoDescription = seoDescription;
    }

    public String getSeoKeywords() {
        return seoKeywords;
    }

    public void setSeoKeywords(String seoKeywords) {
        this.seoKeywords = seoKeywords;
    }

    public String getOgTitle() {
        return ogTitle;
    }

    public void setOgTitle(String ogTitle) {
        this.ogTitle = ogTitle;
    }

    public String getOgImage() {
        return ogImage;
    }

    public void setOgImage(String ogImage) {
        this.ogImage = ogImage;
    }

    public String getOgDescription() {
        return ogDescription;
    }

    public void setOgDescription(String ogDescription) {
        this.ogDescription = ogDescription;
    }

    public String getSuccessmessage() {
        return successmessage;
    }

    public void setSuccessmessage(String successmessage) {
        this.successmessage = successmessage;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public String getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(String isDeleted) {
        this.isDeleted = isDeleted;
    }

    public boolean isDisplayFullMsg() {
        return isDisplayFullMsg;
    }

    public void setDisplayFullMsg(boolean displayFullMsg) {
        isDisplayFullMsg = displayFullMsg;
    }
}