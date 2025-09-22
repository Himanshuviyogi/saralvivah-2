package com.advance.sanatani_vivah.dynamicprofile;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ViewProfileSectionBean {
    @SerializedName("id")
    public String id;
    @SerializedName("name")
    public String name;
    @SerializedName("value")
    public ArrayList<ViewProfileFieldsBean> viewProfileFieldList = new ArrayList<>();
    public boolean isExpanded;
    public boolean isContactVisible;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<ViewProfileFieldsBean> getViewProfileFieldList() {
        return viewProfileFieldList;
    }

    public void setViewProfileFieldList(ArrayList<ViewProfileFieldsBean> viewProfileFieldList) {
        this.viewProfileFieldList = viewProfileFieldList;
    }

    public boolean isExpanded() {
        return isExpanded;
    }

    public void setExpanded(boolean expanded) {
        isExpanded = expanded;
    }

    public boolean isContactVisible() {
        return isContactVisible;
    }

    public void setContactVisible(boolean contactVisible) {
        isContactVisible = contactVisible;
    }
}
