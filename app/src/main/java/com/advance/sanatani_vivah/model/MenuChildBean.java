package com.advance.sanatani_vivah.model;

import com.google.gson.annotations.SerializedName;

public class MenuChildBean {
    @SerializedName("sub_menu_title")
    public String subMenuTitle;
    @SerializedName("sub_menu_id")
    public int subMenuId;
    @SerializedName("img_sub_menu")
    public String imgSubMenu;
    @SerializedName("submenu_tag")
    public String submenuTag;
    @SerializedName("sub_menu_action")
    public String subMenuAction;

    public String getSubMenuTitle() {
        return subMenuTitle;
    }

    public void setSubMenuTitle(String subMenuTitle) {
        this.subMenuTitle = subMenuTitle;
    }

    public void setSubMenuId(int subMenuId) {
        this.subMenuId = subMenuId;
    }

    public String getImgSubMenu() {
        return imgSubMenu;
    }

    public void setImgSubMenu(String imgSubMenu) {
        this.imgSubMenu = imgSubMenu;
    }

    public String getSubmenuTag() {
        return submenuTag;
    }

    public void setSubmenuTag(String submenuTag) {
        this.submenuTag = submenuTag;
    }

    public String getSubMenuAction() {
        return subMenuAction;
    }

    public void setSubMenuAction(String subMenuAction) {
        this.subMenuAction = subMenuAction;
    }
}
