package com.advance.sanatani_vivah.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class VendorParentModel {
    @SerializedName("wedding_planner_reviews_count")
    private int vendorReviewsCount;
    @SerializedName("wedding_planner_reviews")
    private ArrayList<ReviewBean> vendorReviewList = new ArrayList<>();
    @SerializedName("wedding_planner_average")
    private int vendorReviewAverage;
    @SerializedName("wedding_planner_item")
    private VendorModel vendorModel;

    public int getVendorReviewsCount() {
        return vendorReviewsCount;
    }

    public void setVendorReviewsCount(Integer vendorReviewsCount) {
        this.vendorReviewsCount = vendorReviewsCount;
    }

    public ArrayList<ReviewBean> getVendorReviewList() {
        return vendorReviewList;
    }

    public void setVendorReviewList(ArrayList<ReviewBean> vendorReviewList) {
        this.vendorReviewList = vendorReviewList;
    }

    public int getVendorReviewAverage() {
        return vendorReviewAverage;
    }

    public void setVendorReviewAverage(Integer vendorReviewAverage) {
        this.vendorReviewAverage = vendorReviewAverage;
    }

    public VendorModel getVendorModel() {
        return vendorModel;
    }

    public void setVendorModel(VendorModel vendorModel) {
        this.vendorModel = vendorModel;
    }
}
