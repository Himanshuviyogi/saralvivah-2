package com.advance.sanatani_vivah.model;

public class CurrentPlanItem {
    String plan_name,currency,plan_amount,plan_activated,plan_expired,plan_duration,message_used,message,contacts_used,
            contacts,chat,profile,profile_used,tax_name,tax_percentage,grand_total,tax_amount,discount_amount,offer_per;

    public String getDiscount_amount() {
        return discount_amount;
    }

    public void setDiscount_amount(String discount_amount) {
        this.discount_amount = discount_amount;
    }

    public String getTax_amount() {
        return tax_amount;
    }

    public void setTax_amount(String tax_amount) {
        this.tax_amount = tax_amount;
    }

    public String getChat() {
        return chat;
    }

    public void setChat(String chat) {
        this.chat = chat;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getProfile_used() {
        return profile_used;
    }

    public void setProfile_used(String profile_used) {
        this.profile_used = profile_used;
    }

    public String getTax_name() {
        return tax_name;
    }

    public void setTax_name(String tax_name) {
        this.tax_name = tax_name;
    }

    public String getTax_percentage() {
        return tax_percentage;
    }

    public void setTax_percentage(String tax_percentage) {
        this.tax_percentage = tax_percentage;
    }

    public String getGrand_total() {
        return grand_total;
    }

    public void setGrand_total(String grand_total) {
        this.grand_total = grand_total;
    }

    public String getPlan_name() {
        return plan_name;
    }

    public void setPlan_name(String plan_name) {
        this.plan_name = plan_name;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getPlan_amount() {
        return plan_amount;
    }

    public void setPlan_amount(String plan_amount) {
        this.plan_amount = plan_amount;
    }

    public String getPlan_activated() {
        return plan_activated;
    }

    public void setPlan_activated(String plan_activated) {
        this.plan_activated = plan_activated;
    }

    public String getPlan_expired() {
        return plan_expired;
    }

    public void setPlan_expired(String plan_expired) {
        this.plan_expired = plan_expired;
    }

    public String getPlan_duration() {
        return plan_duration;
    }

    public void setPlan_duration(String plan_duration) {
        this.plan_duration = plan_duration;
    }

    public String getMessage_used() {
        return message_used;
    }

    public void setMessage_used(String message_used) {
        this.message_used = message_used;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getContacts_used() {
        return contacts_used;
    }

    public void setContacts_used(String contacts_used) {
        this.contacts_used = contacts_used;
    }

    public String getContacts() {
        return contacts;
    }

    public void setContacts(String contacts) {
        this.contacts = contacts;
    }

    public String getOffer_per() {
        return offer_per;
    }

    public void setOffer_per(String offer_per) {
        this.offer_per = offer_per;
    }
}
