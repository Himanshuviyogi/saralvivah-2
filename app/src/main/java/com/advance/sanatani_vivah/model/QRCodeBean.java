package com.advance.sanatani_vivah.model;

import com.google.gson.annotations.SerializedName;
import com.advance.sanatani_vivah.utility.AppConstants;

/**
 * Created by Momin Nasirali on 06/10/21.
 */
public class QRCodeBean {
    @SerializedName("id")
    private String id;
    @SerializedName("status")
    private String status;
    @SerializedName("icon")
    private String icon;
    @SerializedName("upi_id")
    private String upiId;
    @SerializedName("logo")
    private String logo;
    @SerializedName("qr_code")
    private String qrCode;

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

    public String getIcon() {
        return AppConstants.PAYMENT_LOGO_PREFIX_URL + icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getUpiId() {
        return upiId;
    }

    public void setUpiId(String upiId) {
        this.upiId = upiId;
    }

    public String getLogo() {
        return AppConstants.PAYMENT_LOGO_PREFIX_URL + logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getQrCode() {
        return AppConstants.PAYMENT_LOGO_PREFIX_URL + qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }
}
