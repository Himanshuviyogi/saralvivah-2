package com.advance.sanatani_vivah.model;

public class BlockItem {
    String name,image,detail,matri_id,image_approval,photo_view_status,user_id,photo_view_count;
    int icon;

    public BlockItem() {

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

    public String getPhoto_view_status() {
        return photo_view_status;
    }

    public void setPhoto_view_status(String photo_view_status) {
        this.photo_view_status = photo_view_status;
    }

    public String getImage_approval() {
        return image_approval;
    }

    public void setImage_approval(String image_approval) {
        this.image_approval = image_approval;
    }

    public String getMatri_id() {
        return matri_id;
    }

    public void setMatri_id(String matri_id) {
        this.matri_id = matri_id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
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
}
