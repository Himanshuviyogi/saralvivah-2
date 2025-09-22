package com.ninfo.koinframework.model

import com.google.gson.JsonArray
import com.google.gson.annotations.SerializedName

class RegisterFieldsModel {
    fun filledValue(value: String) {
        this.filledValue = value
    }

    @SerializedName("cell_type")
    val cellType: Int? = null

    @SerializedName("input_type")
    val inputType: Int? = null

    @SerializedName("dialog_type")
    val dialogType: String? = null

    @SerializedName("ios_inApp_purchase_mandatory")
    val iosInAppPurchaseMandatory: Boolean? = null

    @SerializedName("value")
    var filledValue: String = ""

    @SerializedName("step")
    val step: String? = null

    @SerializedName("api_param_key")
    val apiParamKey: String? = null

    @SerializedName("ios_tag")
    val iosTag: String? = null

    @SerializedName("hint")
    val hint: String? = null

    @SerializedName("dropdown_list_value")
    val dropdownListValue: JsonArray = JsonArray()

    @SerializedName("is_mandatory")
    val isMandatory: Boolean? = null

    @SerializedName("static_array")
    val staticArray: Boolean? = null

    @SerializedName("is_visible")
    var isVisible: Boolean = false

    @SerializedName("is_depedent")
    val isDepedent: Boolean? = null

    @SerializedName("master_array")
    val masterArray: Boolean? = null

    @SerializedName("has_dependent")
    val hasDependent: Boolean? = null

    @SerializedName("dropdown_list_key")
    val dropdownListKey: String = ""

    @SerializedName("selected_ids")
    val selectedIds: String? = null

    @SerializedName("dependent_key_val")
    val dependentKeyVal: String = ""

    @SerializedName("depedent_key_dis")
    val depedentKeyDis: String? = null

    @SerializedName("parent_key")
    val parentKey: String? = null

    @SerializedName("depedent_list_key")
    val depedentListKey: String = ""
}