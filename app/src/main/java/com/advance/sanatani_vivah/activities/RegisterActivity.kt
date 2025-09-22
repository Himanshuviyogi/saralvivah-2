package com.advance.sanatani_vivah.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.advance.sanatani_vivah.R
import com.advance.sanatani_vivah.adapter.RegisterFieldsAdapter
import com.advance.sanatani_vivah.application.MyApplication
import com.advance.sanatani_vivah.model.SpinnerDataModel
import com.advance.sanatani_vivah.retrofit.ApiRequestResponse
import com.advance.sanatani_vivah.utility.AppConstants
import com.advance.sanatani_vivah.utility.AppDebugLog
import com.advance.sanatani_vivah.utility.Common
import com.advance.sanatani_vivah.utility.SessionManager
import com.androidbuts.multispinnerfilter.MultiSpinnerSearch
import com.androidbuts.multispinnerfilter.SingleSpinnerSearch
import com.google.android.material.button.MaterialButton
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.google.gson.reflect.TypeToken
import com.hbb20.CountryCodePicker
import com.ninfo.koinframework.model.RegisterFieldsModel
import com.ninfo.koinframework.model.StepNameModel
import org.json.JSONException
import java.util.ArrayList

class RegisterActivity : AppCompatActivity(), RegisterFieldsAdapter.ItemListener,
    ApiRequestResponse.AppApiRequestCallbacks {

    var fieldsList: List<RegisterFieldsModel> = ArrayList<RegisterFieldsModel>()
    var stepNameModel: List<StepNameModel> = ArrayList<StepNameModel>()
    private var currentRegisterStepMain = 1
    var totalRegistrationSteps: Int = 0
    private val registerFieldListMap: HashMap<Int, ArrayList<RegisterFieldsModel>> = HashMap(2)
    private var adapter: RegisterFieldsAdapter? = null
    var isValid = true
    var spinnerDataModel: SpinnerDataModel? = null
    var dependentTagStr: String = ""
    var registrationId: String = ""
    var gender: String = ""
    var dependentPosition: Int = -1
    var isSuccess: Boolean = false

    val gson =
        GsonBuilder().setDateFormat(AppConstants.GSONDateTimeFormat)
            .create()

    private var recyclerView: RecyclerView? = null
    private var loader: RelativeLayout? = null
    private var common: Common? = null
    private var session: SessionManager? = null
    private var btnSubmit: MaterialButton? = null
    private var btnLogin: MaterialButton? = null
    private var layoutRegisterButtons: LinearLayout? = null
    private var layoutAlreadyMember: LinearLayout? = null
    private var lblTerms: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        common = Common(this)
        session = SessionManager(this)

        loader = findViewById(R.id.loader)
        recyclerView = findViewById(R.id.recyclerView)
        btnSubmit = findViewById(R.id.btnSubmit)
        btnLogin = findViewById(R.id.btnLogin)
        layoutRegisterButtons = findViewById(R.id.layoutRegisterButtons)
        layoutAlreadyMember = findViewById(R.id.layoutAlreadyMember)
        lblTerms = findViewById(R.id.lblTerms)

        initialize()

        singleTextView(lblTerms)

        btnLogin!!.setOnClickListener {
            startActivity(
                Intent(
                    this@RegisterActivity,
                    LoginActivity::class.java
                )
            )
            finish()
        }

        btnSubmit!!.setOnClickListener {
            AppDebugLog.print("isValid : $isValid")
            setValuesInArray()
            if (isValid) {
                registerRequest(currentRegisterStepMain)
            } else {
                isValid = true
            }
        }
    }

    private fun registerRequest(currentRegisterStep: Int) {
        common!!.showProgressRelativeLayout(loader)

        currentRegisterStepMain = currentRegisterStep

        val param = java.util.HashMap<String, String>()
        param["id"] = registrationId

        var apiUrl = ""
        if (currentRegisterStep == 1) {
            param["android_device_id"] = session!!.getLoginData(SessionManager.KEY_DEVICE_TOKEN)
            apiUrl = AppConstants.register_first
        } else {
            apiUrl = AppConstants.register_step
        }

        for (registerFieldsModel in adapter!!.getAdapterList()!!) {
            param.put(registerFieldsModel?.apiParamKey!!, registerFieldsModel.filledValue)
            if (registerFieldsModel.apiParamKey.equals("mobile_number", false)) {
                val numberArr = registerFieldsModel.filledValue.split("-").toTypedArray()
                param.put("country_code", numberArr[0])
                param.put("mobile_number", numberArr[1])
            }
        }

        AppDebugLog.print("submit : " + param.toString())

        Common.hideSoftKeyboard(this)

        val apiRequestResponse = ApiRequestResponse()
        apiRequestResponse.postRequest(
            this@RegisterActivity,
            apiUrl,
            param,
            this,
            "SUBMIT"
        )
    }

    private fun setValuesInArray() {
        val values = ArrayList<String>()
        //input types number = 2 , text = 1, email = 32, password = 128
        for (i in 0 until (adapter?.getAdapterList()?.size!!)) {
            val registerFieldsModel: RegisterFieldsModel = adapter?.getAdapterList()!!.get(i)!!
            val view: View = recyclerView!!.getChildAt(i)

            when (registerFieldsModel.cellType) {
                adapter?.TYPE_EDITTEXT -> {
                    val editText = view.findViewById<EditText>(R.id.editText)

                    if (registerFieldsModel.isMandatory == true) {
                        if (Common.isTextViewEmpty(editText.text)) {
                            editText.error = "Please enter " + editText.hint
                            isValid = false
                        } else {
                            if (registerFieldsModel.inputType == 32) {
                                if (Common.isValidEmail(editText.text.toString())) {
                                    values.add(editText.text.toString())
                                    registerFieldsModel.filledValue(editText.text.toString())
                                } else {
                                    editText.error = "Please enter valid " + editText.hint
                                    isValid = false
                                }
                            }
                        }
                    }
                    registerFieldsModel.filledValue(editText.text.toString())
                    values.add(editText.text.toString())

                }
                adapter?.TYPE_COUNTRY_CODE -> {
                    val countryCodePicker: CountryCodePicker =
                        view.findViewById(R.id.countryCodePicker)
                    val edtxtMobileNumber = view.findViewById<EditText>(R.id.etPhoneNumber)
                    if (Common.isTextViewEmpty(edtxtMobileNumber.text)) {
                        edtxtMobileNumber.error = "Please enter " + edtxtMobileNumber.hint
                        isValid = false
                    } else {
                        if (Common.isValidMobileNumber(edtxtMobileNumber.text.toString())) {
                            val mobileNoWithCountryCode =
                                countryCodePicker.selectedCountryCodeWithPlus + "-" + edtxtMobileNumber.text.toString()
                            registerFieldsModel.filledValue(mobileNoWithCountryCode)
                            values.add(mobileNoWithCountryCode)
                        } else {
                            edtxtMobileNumber.error =
                                "Please enter valid " + edtxtMobileNumber.hint
                            isValid = false
                        }
                    }
                }
                adapter?.TYPE_SPINNER -> {
                    val spinner: SingleSpinnerSearch = view.findViewById(R.id.spinner)
                    if (spinner.selectedIds.size == 0) {
                        if (registerFieldsModel.isMandatory == true) {
                            common!!.spinnerSetError(
                                spinner,
                                "Please select " + registerFieldsModel.hint
                            )
                            isValid = false
                        }
                    } else {
                        registerFieldsModel.filledValue(spinner.selectedIds[0])
                        values.add(spinner.selectedIds[0]!!)
                    }
                }
                adapter?.TYPE_MULTI_SPINNER -> {
                    val multiSpinner: MultiSpinnerSearch = view.findViewById(R.id.multiSpinner)

                    if (multiSpinner.selectedIds.size == 0) {
                        if (registerFieldsModel.isMandatory == true) {
                            common!!.spinnerSetError(
                                multiSpinner,
                                "Please select " + registerFieldsModel.hint
                            )
                            isValid = false
                        }

                    } else {
                        registerFieldsModel.filledValue(multiSpinner.selectedIdsInString)
                        values.add(multiSpinner.selectedIdsInString)
                    }
                }
                adapter?.TYPE_RADIO_BUTTON -> {
                    val radioGroup: RadioGroup = view.findViewById(R.id.radioGroup)
                    lateinit var selectedRadioButton: RadioButton

                    val selectedRadioButtonId: Int = radioGroup.checkedRadioButtonId
                    if (selectedRadioButtonId != -1) {
                        selectedRadioButton = findViewById(selectedRadioButtonId)
                        val string: String = selectedRadioButton.getTag().toString()
                        registerFieldsModel.filledValue(string)
                        values.add(string)
                        gender = string
                    } else {
                        if (registerFieldsModel.isMandatory == true) {
                            Toast.makeText(
                                this,
                                "Please select " + registerFieldsModel.hint,
                                Toast.LENGTH_SHORT
                            ).show()
                            isValid = false
                        }
                    }
                }
                else -> {

                }
            }

        }
        AppDebugLog.printStringArray(values)
    }

    private fun singleTextView(textView: TextView?) {
        val clickableTextStr = "Terms and Conditions."
        val spanText = SpannableStringBuilder()
        spanText.append("By submitting you agree our $clickableTextStr")
        spanText.setSpan(object : ClickableSpan() {
            override fun onClick(widget: View) {
                openCMSDataDialog()
            }

            override fun updateDrawState(textPaint: TextPaint) {
                textPaint.color =
                    ContextCompat.getColor(
                        this@RegisterActivity,
                        R.color.blue_color
                    ) // you can use custom color
                textPaint.isUnderlineText = false // this remove the underline
            }
        }, spanText.length - clickableTextStr.length, spanText.length, 0)

        textView!!.setMovementMethod(LinkMovementMethod.getInstance())
        textView.setHighlightColor(
            ContextCompat.getColor(
                this@RegisterActivity,
                R.color.transparent
            )
        )
        textView.setText(spanText, TextView.BufferType.SPANNABLE)
    }

    private fun openCMSDataDialog() {
        val intent = Intent(this, AllCmsActivity::class.java)
        intent.putExtra(AppConstants.KEY_INTENT, "term")
        startActivity(intent)
    }

    private fun initialize() {
        if (MyApplication.getSpinDataStr() != null) {
            initDropDownData()
        } else {
            getList()
        }
    }

    private fun initDropDownData() {
        val jsonFileString: String =
            Common.getJsonFromAssets(applicationContext, "dynamic_reg_fields.json")

        val jsonObject = JsonParser().parse(jsonFileString).asJsonObject
        val data: JsonObject = jsonObject.get("data").asJsonObject

        fieldsList = gson.fromJson<List<RegisterFieldsModel>>(
            data.getAsJsonArray("registeration_fields"),
            object : TypeToken<List<RegisterFieldsModel?>?>() {}.type
        )

        totalRegistrationSteps =
            data.get("total_registration_step").getAsInt()
        for (step in 1..totalRegistrationSteps) {
            val fieldList: ArrayList<RegisterFieldsModel> =
                ArrayList<RegisterFieldsModel>()
            for (registerFieldsModel in fieldsList) {
                if (registerFieldsModel.step?.toInt() === step) {
                    fieldList.add(registerFieldsModel)
                }
            }
            registerFieldListMap.put(step, fieldList)
        }

        initializeRecyclerView(
            registerFieldListMap.get(currentRegisterStepMain)!!,
            currentRegisterStepMain
        )

        layoutRegisterButtons!!.visibility = View.VISIBLE

    }

    private fun initializeRecyclerView(
        registerFieldList: ArrayList<RegisterFieldsModel>,
        stepNo: Int
    ) {

        adapter = RegisterFieldsAdapter(this, registerFieldList)
        adapter!!.setListener(this)
        recyclerView!!.setLayoutManager(LinearLayoutManager(this))
        recyclerView!!.setAdapter(adapter)
    }

    override fun onBackPressed() {
        if (currentRegisterStepMain == 1) {
            super.onBackPressed()
        } else {
            if (totalRegistrationSteps >= currentRegisterStepMain) {
                currentRegisterStepMain--
                if (currentRegisterStepMain == 1) {
                    layoutAlreadyMember!!.setVisibility(View.VISIBLE)
                    lblTerms!!.setVisibility(View.VISIBLE)
                }
                initializeRecyclerView(
                    registerFieldListMap[currentRegisterStepMain]!!,
                    currentRegisterStepMain
                )
            }
        }
    }

    //TODO api calls related code
    private fun getList() {
        common!!.showProgressRelativeLayout(loader)

        val apiRequestResponse = ApiRequestResponse()
        apiRequestResponse.postRequest(
            this@RegisterActivity,
            AppConstants.common_list,
            null,
            this,
            "DDR"
        )
    }

    override fun getDependentList(
        dependentRegisterFieldsModel: RegisterFieldsModel?,
        selectedId: String,
        tag: String,
        position: Int
    ) {
        if (!selectedId.isNullOrEmpty()) {
            common!!.showProgressRelativeLayout(loader)
            dependentTagStr = tag
            dependentPosition = position

            val param = java.util.HashMap<String, String>()
            param["get_list"] = tag
            param["currnet_val"] = selectedId
            param["multivar"] = ""
            param["retun_for"] = ""

            val apiRequestResponse = ApiRequestResponse()
            apiRequestResponse.postRequest(
                this@RegisterActivity,
                AppConstants.common_depedent_list,
                param,
                this,
                "DEPENDENT_DDR"
            )
        }
    }

    override fun onSuccess(data: JsonObject?, requestTag: String?) {
        common!!.hideProgressRelativeLayout(loader)

        when (requestTag) {
            "DDR" -> {
                try {
                    MyApplication.setSpinDataStr(data.toString())
                    initDropDownData()
                } catch (e: JSONException) {
                    e.printStackTrace()
                    Common.showToast(getString(R.string.err_msg_try_again_later))
                }
            }
            "DEPENDENT_DDR" -> {
                val jsonArr = data!!.getAsJsonArray("data")

                when (dependentTagStr) {
                    "caste_list" -> {
                        adapter?.setCasteList(
                            Common.getSpinnerListFromArray(
                                jsonArr
                            ), dependentPosition
                        )
                    }
                    "state_list" -> {
                        adapter?.setStateList(
                            Common.getSpinnerListFromArray(
                                jsonArr
                            ), dependentPosition
                        )
                    }
                    "city_list" -> {
                        adapter?.setCityList(
                            Common.getSpinnerListFromArray(
                                jsonArr
                            ), dependentPosition
                        )
                    }
                }
            }
            "SUBMIT" -> {
                Toast.makeText(
                    applicationContext,
                    data!!.get("errmessage").asString,
                    Toast.LENGTH_LONG
                ).show()

                if (currentRegisterStepMain == 1) {
                    registrationId = data.get("id").asString
                }

                currentRegisterStepMain++
                if (currentRegisterStepMain != 1) {
                    layoutAlreadyMember!!.setVisibility(View.GONE)
                    lblTerms!!.setVisibility(View.GONE)
                }

                if (registerFieldListMap[currentRegisterStepMain] != null) {
                    initializeRecyclerView(
                        registerFieldListMap[currentRegisterStepMain]!!,
                        currentRegisterStepMain
                    )
                }
                isValid = true

                // if this is final step dynamic form then redirect to photos upload screen
                AppDebugLog.print("totalRegistrationSteps : " + totalRegistrationSteps + " || currentRegisterStep : " + currentRegisterStepMain)
                if (totalRegistrationSteps == currentRegisterStepMain) {
                    val intent = Intent(
                        this@RegisterActivity,
                        RegisterPhotoUploadActivity::class.java
                    )
                    intent.putExtra("registrationId", registrationId)
                    intent.putExtra("gender", gender)
                    startActivity(intent)
                    finish()
                }
            }

        }
    }

    override fun onError(isError: Boolean, requestTag: String?) {
        common!!.hideProgressRelativeLayout(loader)
    }

}
