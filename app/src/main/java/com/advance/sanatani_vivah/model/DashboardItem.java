package com.advance.sanatani_vivah.model;

import org.json.JSONObject;

public class DashboardItem {
    String name,image,image_approval,age,height,caste,religion,city, country,designation,photo_protect,photo_view_status,
            photo_password,id,matri_id,about,user_id,photo_view_count,state,profileCreatedBy,education,id_proof_approve,
            badge,badgeUrl,color,photoUrl;
    int icon;
    JSONObject action;
    boolean isOnline=false;

    public DashboardItem() {
    }

    public DashboardItem(String name, int icon, boolean isOnline) {
        this.name = name;
        this.icon = icon;
        this.isOnline = isOnline;
    }

    public String getPhoto_view_count() {
        return photo_view_count;
    }

    public void setPhoto_view_count(String photo_view_count) {
        this.photo_view_count = photo_view_count;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public JSONObject getAction() {
        return action;
    }

    public void setAction(JSONObject action) {
        this.action = action;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getMatri_id() {
        return matri_id;
    }

    public void setMatri_id(String matri_id) {
        this.matri_id = matri_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPhoto_password() {
        return photo_password;
    }

    public void setPhoto_password(String photo_password) {
        this.photo_password = photo_password;
    }

    public String getPhoto_view_status() {
        return photo_view_status;
    }

    public void setPhoto_view_status(String photo_view_status) {
        this.photo_view_status = photo_view_status;
    }

    public String getPhoto_protect() {
        return photo_protect;
    }

    public void setPhoto_protect(String photo_protect) {
        this.photo_protect = photo_protect;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getCaste() {
        return caste;
    }

    public void setCaste(String caste) {
        this.caste = caste;
    }

    public String getReligion() {
        return religion;
    }

    public void setReligion(String religion) {
        this.religion = religion;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImage_approval() {
        return image_approval;
    }

    public void setImage_approval(String image_approval) {
        this.image_approval = image_approval;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean online) {
        isOnline = online;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getProfileCreatedBy() {
        return profileCreatedBy;
    }

    public void setProfileCreatedBy(String profileCreatedBy) {
        this.profileCreatedBy = profileCreatedBy;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getId_proof_approve() {
        return id_proof_approve;
    }

    public void setId_proof_approve(String id_proof_approve) {
        this.id_proof_approve = id_proof_approve;
    }

    public String getBadge() {
        return badge;
    }

    public void setBadge(String badge) {
        this.badge = badge;
    }

    public String getBadgeUrl() {
        return badgeUrl;
    }

    public void setBadgeUrl(String badgeUrl) {
        this.badgeUrl = badgeUrl;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }
}
