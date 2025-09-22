package com.advance.sanatani_vivah.model;

public class MenuChild {
    String name,clickTag;

    public MenuChild(String name,String clickTag) {
        this.name = name;
        this.clickTag=clickTag;
    }

    public String getClickTag() {
        return clickTag;
    }

    public void setClickTag(String clickTag) {
        this.clickTag = clickTag;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
