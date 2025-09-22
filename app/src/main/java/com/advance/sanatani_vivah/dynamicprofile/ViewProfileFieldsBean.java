package com.advance.sanatani_vivah.dynamicprofile;

import com.google.gson.annotations.SerializedName;

public class ViewProfileFieldsBean {
    @SerializedName("title")
    public String title;
    @SerializedName("value")
    public String value;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @SerializedName("id")
    public String id;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
