package com.advance.sanatani_vivah.model;

import com.google.gson.annotations.SerializedName;

public class MemberAction {
    @SerializedName("is_shortlist")
    int isShortlist;
    @SerializedName("is_interest")
    String isInterest;
    @SerializedName("is_like")
    String isLike;
    @SerializedName("is_block")
    int isBlock;
    @SerializedName("is_login")
    int isLogin;
    @SerializedName("is_view")
    int isView;

    public int getIsShortlist() {
        return isShortlist;
    }

    public void setIsShortlist(int isShortlist) {
        this.isShortlist = isShortlist;
    }

    public String getIsInterest() {
        return isInterest;
    }

    public void setIsInterest(String isInterest) {
        this.isInterest = isInterest;
    }

    public String getIsLike() {
        return isLike;
    }

    public void setIsLike(String isLike) {
        this.isLike = isLike;
    }

    public int getIsBlock() {
        return isBlock;
    }

    public void setIsBlock(int isBlock) {
        this.isBlock = isBlock;
    }

    public int getIsLogin() {
        return isLogin;
    }

    public void setIsLogin(int isLogin) {
        this.isLogin = isLogin;
    }

    public int getIsView() {
        return isView;
    }

    public void setIsView(int isView) {
        this.isView = isView;
    }
}
