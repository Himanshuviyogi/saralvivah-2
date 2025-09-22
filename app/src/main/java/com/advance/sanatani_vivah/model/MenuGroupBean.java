package com.advance.sanatani_vivah.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MenuGroupBean {
    @SerializedName("menu_title")
    public String menuTitle;
    @SerializedName("menu_id")
    public int menuId;
    @SerializedName("is_expandable")
    public int isExpandable;
    @SerializedName("menu_action")
    public String menuAction;
    @SerializedName("menu_img")
    public String menuImg;
    @SerializedName("sub_menu")
    public List<MenuChildBean> drawerChildList = null;

    public String getMenuTitle() {
        return menuTitle;
    }

    public void setMenuTitle(String menuTitle) {
        this.menuTitle = menuTitle;
    }

    public int getMenuId() {
        return menuId;
    }

    public void setMenuId(int menuId) {
        this.menuId = menuId;
    }

    public int getIsExpandable() {
        return isExpandable;
    }

    public void setIsExpandable(int isExpandable) {
        this.isExpandable = isExpandable;
    }

    public String getMenuAction() {
        return menuAction;
    }

    public void setMenuAction(String menuAction) {
        this.menuAction = menuAction;
    }

    public String getMenuImg() {
        return menuImg;
    }

    public void setMenuImg(String menuImg) {
        this.menuImg = menuImg;
    }

    public List<MenuChildBean> getDrawerChildList() {
        return drawerChildList;
    }

    public void setDrawerChildList(List<MenuChildBean> drawerChildList) {
        this.drawerChildList = drawerChildList;
    }

}
