package com.advance.sanatani_vivah.model;

public class paymentItem {
    String name,logo;
    boolean selected=false;

    public paymentItem(String name, String logo) {
        this.name = name;
        this.logo = logo;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }
}
