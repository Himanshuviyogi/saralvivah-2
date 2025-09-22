package com.advance.sanatani_vivah.model;

import com.google.gson.annotations.SerializedName;

public class CategoryModel {
    @SerializedName("id")
    private String id;
    @SerializedName("status")
    private String status;
    @SerializedName("category_position")
    private String categoryPosition;
    @SerializedName("category_name")
    private String categoryName;
    @SerializedName("category_image")
    private String categoryImage;
    @SerializedName("is_deleted")
    private String isDeleted;

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

    public String getCategoryPosition() {
        return categoryPosition;
    }

    public void setCategoryPosition(String categoryPosition) {
        this.categoryPosition = categoryPosition;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryImage() {
        return categoryImage;
    }

    public void setCategoryImage(String categoryImage) {
        this.categoryImage = categoryImage;
    }

    public String getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(String isDeleted) {
        this.isDeleted = isDeleted;
    }
}
