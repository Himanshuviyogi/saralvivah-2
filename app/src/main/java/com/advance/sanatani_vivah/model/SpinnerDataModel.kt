package com.advance.sanatani_vivah.model

import com.androidbuts.multispinnerfilter.KeyPairBoolData
import com.google.gson.annotations.SerializedName

class SpinnerDataModel {
    @SerializedName("religion_master")
    val religionMaster: List<KeyPairBoolData>? = null

    @SerializedName("country_master")
    val countryMaster: List<KeyPairBoolData>? = null

    @SerializedName("qualification_master")
    val qualificationMaster: List<KeyPairBoolData>? = null

    @SerializedName("occupation_master")
    val occupationMaster: List<KeyPairBoolData>? = null

    @SerializedName("income_master")
    val incomeMaster: List<KeyPairBoolData>? = null

    @SerializedName("bloodgroup_master")
    val bloodgroupMaster: List<KeyPairBoolData>? = null
}