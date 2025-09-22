package com.advance.sanatani_vivah.model;

public class PhotoPasswordBean {
    String name,tag,matri_id,id,image,about,image_approval,date,receiver_response,ph_receiver_id,ph_requester_id,photo_view_status,
            photo_view_count,user_id,badge,badgeUrl,color,photoUrl;
    int icon;

    public PhotoPasswordBean(String name, int icon , String tag) {
        this.name = name;
        this.icon = icon;
        this.tag = tag;
    }

    public PhotoPasswordBean() {
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getPhoto_view_status() {
        return photo_view_status;
    }

    public void setPhoto_view_status(String photo_view_status) {
        this.photo_view_status = photo_view_status;
    }

    public String getPh_requester_id() {
        return ph_requester_id;
    }

    public void setPh_requester_id(String ph_requester_id) {
        this.ph_requester_id = ph_requester_id;
    }

    public String getPhoto_view_count() {
        return photo_view_count;
    }

    public void setPhoto_view_count(String photo_view_count) {
        this.photo_view_count = photo_view_count;
    }

    public String getPh_receiver_id() {
        return ph_receiver_id;
    }

    public void setPh_receiver_id(String ph_receiver_id) {
        this.ph_receiver_id = ph_receiver_id;
    }

    public String getReceiver_response() {
        return receiver_response;
    }

    public void setReceiver_response(String receiver_response) {
        this.receiver_response = receiver_response;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getImage_approval() {
        return image_approval;
    }

    public void setImage_approval(String image_approval) {
        this.image_approval = image_approval;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
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
