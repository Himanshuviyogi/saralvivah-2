package com.advance.sanatani_vivah.model;

import org.json.JSONObject;

public class PlanItem {
    String plan_name,prise,currency,profile_view,contact,duration,message,id,plan_offers;
    boolean is_select=false;
    JSONObject plan_object;

    public PlanItem(String plan_name,boolean is_select) {
        this.plan_name = plan_name;
        this.is_select = is_select;
    }

    public PlanItem() {
    }

    public JSONObject getPlan_object() {
        return plan_object;
    }

    public void setPlan_object(JSONObject plan_object) {
        this.plan_object = plan_object;
    }

    public String getPlan_offers() {
        return plan_offers;
    }

    public void setPlan_offers(String plan_offers) {
        this.plan_offers = plan_offers;
    }

    public String getPrise() {
        return prise;
    }

    public void setPrise(String prise) {
        this.prise = prise;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getProfile_view() {
        return profile_view;
    }

    public void setProfile_view(String profile_view) {
        this.profile_view = profile_view;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isIs_select() {
        return is_select;
    }

    public void setIs_select(boolean is_select) {
        this.is_select = is_select;
    }

    public String getPlan_name() {
        return plan_name;
    }

    public void setPlan_name(String plan_name) {
        this.plan_name = plan_name;
    }
}
