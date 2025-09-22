package com.advance.sanatani_vivah.model;

import com.google.gson.annotations.SerializedName;
import com.advance.sanatani_vivah.utility.AppConstants;

/**
 * Created by Momin Nasirali on 06/10/21.
 */
public class BankDetailsBean {
    @SerializedName("id")
    private String id;
    @SerializedName("status")
    private String status;
    @SerializedName("account_no")
    private String accountNo;
    @SerializedName("account_name")
    private String accountName;
    @SerializedName("account_type")
    private String accountType;
    @SerializedName("ifsc_code")
    private String ifscCode;
    @SerializedName("logo")
    private String logo;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getIfscCode() {
        return ifscCode;
    }

    public void setIfscCode(String ifscCode) {
        this.ifscCode = ifscCode;
    }

    public String getLogo() {
        return AppConstants.PAYMENT_LOGO_PREFIX_URL + logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }
}
