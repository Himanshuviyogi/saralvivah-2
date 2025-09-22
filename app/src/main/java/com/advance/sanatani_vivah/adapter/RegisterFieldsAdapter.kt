package com.advance.sanatani_vivah.adapter

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.View.TEXT_ALIGNMENT_CENTER
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.advance.sanatani_vivah.R
import com.advance.sanatani_vivah.activities.RegisterActivity
import com.advance.sanatani_vivah.application.MyApplication
import com.advance.sanatani_vivah.dialog.DatePickerDialogFragment
import com.advance.sanatani_vivah.utility.AppDebugLog
import com.advance.sanatani_vivah.utility.Common
import com.androidbuts.multispinnerfilter.KeyPairBoolData
import com.androidbuts.multispinnerfilter.MultiSpinnerSearch
import com.androidbuts.multispinnerfilter.SingleSpinnerSearch
import com.androidbuts.multispinnerfilter.SpinnerListener
import com.google.android.material.textfield.TextInputLayout
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.hbb20.CountryCodePicker
import com.ninfo.koinframework.model.RegisterFieldsModel
import java.text.SimpleDateFormat
import java.util.*

class RegisterFieldsAdapter(
    private val mContext: Context,
    private var registerFields: List<RegisterFieldsModel>
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>(), DatePickerDialog.OnDateSetListener,
    SpinnerListener {
    private var myListener: ItemListener? = null

    private var dobCellPosition = 0
    private var dobTimeCellPosition = 0
    private var strDOB: String? = null
    private var strDOBTime: String? = null
    private var setListerner = false

    val TYPE_EDITTEXT = 0
    val TYPE_SPINNER = 1
    val TYPE_MULTI_SPINNER = 2
    val TYPE_COUNTRY_CODE = 3
    val TYPE_RADIO_BUTTON = 5
    val TYPE_BUTTON = 6

    var casteLists: List<KeyPairBoolData> = ArrayList()
    var stateLists: List<KeyPairBoolData> = ArrayList()
    var cityLists: List<KeyPairBoolData> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(mContext)
        when (viewType) {
            TYPE_SPINNER -> return SpinnerViewHolder(
                inflater.inflate(R.layout.cell_register_single_spinner, parent, false)
            )
            TYPE_MULTI_SPINNER -> return MultiSpinnerViewHolder(
                inflater.inflate(R.layout.cell_register_multi_spinner, parent, false)
            )
            TYPE_EDITTEXT -> return EditTextViewHolder(
                inflater.inflate(R.layout.cell_register_edittext, parent, false)
            )
            TYPE_COUNTRY_CODE -> return CountryCodeViewHolder(
                inflater.inflate(
                    R.layout.cell_register_mobile_with_country_code,
                    parent,
                    false
                )
            )
            TYPE_BUTTON -> return ButtonViewHolder(
                inflater.inflate(R.layout.cell_register_button, parent, false)
            )
            TYPE_RADIO_BUTTON -> return RadioViewHolder(
                inflater.inflate(R.layout.cell_register_radio_button, parent, false)
            )
        }
        return EditTextViewHolder(
            inflater.inflate(
                R.layout.cell_register_edittext,
                parent,
                false
            )
        )
    }

    override fun getItemViewType(position: Int): Int {
        val type: Int = registerFields[position].cellType!!.toInt()
        when (type) {
            TYPE_EDITTEXT -> return TYPE_EDITTEXT
            TYPE_SPINNER -> return TYPE_SPINNER
            TYPE_MULTI_SPINNER -> return TYPE_MULTI_SPINNER
            TYPE_COUNTRY_CODE -> return TYPE_COUNTRY_CODE
            TYPE_RADIO_BUTTON -> return TYPE_RADIO_BUTTON
            TYPE_BUTTON -> return TYPE_BUTTON
        }
        return TYPE_EDITTEXT
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        val registerFieldsModel: RegisterFieldsModel = registerFields[position]

        val strJson = MyApplication.getSpinDataStr()
        val jsonObject: JsonObject = JsonParser().parse(strJson).getAsJsonObject()

        when (getItemViewType(position)) {
            TYPE_EDITTEXT -> {

                if (registerFieldsModel.dialogType != null) {
                    (holder as EditTextViewHolder).editText.isFocusable = false
                    if (registerFieldsModel.dialogType.equals("date", false)
                    ) {
                        dobCellPosition = position
                        holder.editText.setText(strDOB)
                        holder.editText.setOnClickListener { v: View? ->
                            val bundle = Bundle()
                            val sdf = SimpleDateFormat("yyyy-MM-dd")
                            val currentDate = sdf.format(Date())
                            bundle.putString("date", currentDate.toString())
                            val dobDialog = DatePickerDialogFragment()
                            dobDialog.setArguments(bundle)
                            dobDialog.setCallBack(this)
                            dobDialog.show(
                                (mContext as RegisterActivity).getSupportFragmentManager(),
                                "date"
                            )
                        }
                    }

                    if (registerFieldsModel.dialogType.equals("time", false)
                    ) {
                        dobTimeCellPosition = position
                        holder.editText.setText(strDOBTime)
                        holder.editText.setOnClickListener { v: View? ->
                            val mcurrentTime = Calendar.getInstance()
                            val hour: Int = mcurrentTime.get(Calendar.HOUR_OF_DAY)
                            val minute: Int = mcurrentTime.get(Calendar.MINUTE)
                            val mTimePicker: TimePickerDialog

                            mTimePicker = TimePickerDialog(
                                mContext,
                                { timePicker: TimePicker, selectedHour: Int, selectedMinute: Int ->
                                    holder.editText.setText(selectedHour.toString() + ":" + selectedMinute.toString())
                                }, hour, minute, true
                            )
                            mTimePicker.setTitle("Select " + registerFieldsModel.hint)
                            mTimePicker.show()
                        }
                    }
                }

                //if user filled value and goto next and return back then set that filled value
                if (registerFieldsModel.filledValue.length > 0) {
                    (holder as EditTextViewHolder).editText.setText(
                        registerFieldsModel.filledValue
                    )
                }

                (holder as EditTextViewHolder).textInputLayout.setHint(
                    registerFieldsModel.hint
                )
                if (registerFieldsModel.inputType == 1) {
                    (holder as EditTextViewHolder).editText.setInputType(
                        InputType.TYPE_CLASS_TEXT
                    )
                } else if (registerFieldsModel.inputType == 2) {
                    (holder as EditTextViewHolder).editText.setInputType(
                        InputType.TYPE_CLASS_NUMBER
                    )
                } else if (registerFieldsModel.inputType == 32) {
                    (holder as EditTextViewHolder).editText.setInputType(
                        InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
                    )
                } else if (registerFieldsModel.inputType == 128) {
                    (holder as EditTextViewHolder).editText.setInputType(
                        InputType.TYPE_TEXT_VARIATION_PASSWORD
                    )
                } else {
                    (holder as EditTextViewHolder).editText.setInputType(
                        InputType.TYPE_CLASS_TEXT
                    )
                }

            }
            TYPE_SPINNER -> {
                // set position as a tag for use in onItemsSelected function
                (holder as SpinnerViewHolder).spinner.setTag(
                    position
                )

                if (registerFieldsModel.apiParamKey.equals("total_children") ||
                    registerFieldsModel.apiParamKey.equals("status_children")
                ) {
                    if (!registerFieldsModel.isVisible) {
                        //holder.mainLayout.visibility = View.GONE
                        holder.mainLayout.isEnabled = false
                        holder.mainLayout.isClickable = false
                        holder.mainLayout.isFocusable = false

                        holder.spinner.isEnabled = false
                        holder.spinner.isClickable = false
                        holder.spinner.isFocusable = false

//                        val marginLayoutParams =
//                            ViewGroup.MarginLayoutParams(holder.mainLayout.getLayoutParams())
//                        marginLayoutParams.width = 0
//                        marginLayoutParams.height = 0
//                        marginLayoutParams.setMargins(0, 0, 0, 0)
//                        holder.mainLayout.setLayoutParams(marginLayoutParams)

                    } else {
                        //holder.mainLayout.visibility = View.VISIBLE
                        holder.mainLayout.isEnabled = true
                        holder.mainLayout.isClickable = true
                        holder.mainLayout.isFocusable = true

                        holder.spinner.isEnabled = true
                        holder.spinner.isClickable = true
                        holder.spinner.isFocusable = true

//                        val marginLayoutParams =
//                            ViewGroup.MarginLayoutParams(holder.mainLayout.getLayoutParams())
//                        marginLayoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
//                        marginLayoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
//                        marginLayoutParams.setMargins(0, 25, 0, 0)
//                        holder.mainLayout.setLayoutParams(marginLayoutParams)
                    }
                } else {
                    //holder.mainLayout.visibility = View.VISIBLE
                    holder.mainLayout.isEnabled = true
                    holder.mainLayout.isClickable = true
                    holder.mainLayout.isFocusable = true

                    holder.spinner.isEnabled = true
                    holder.spinner.isClickable = true
                    holder.spinner.isFocusable = true

//                    val marginLayoutParams =
//                        ViewGroup.MarginLayoutParams(holder.mainLayout.getLayoutParams())
//                    marginLayoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
//                    marginLayoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
//                    marginLayoutParams.setMargins(0, 25, 0, 0)
//                    holder.mainLayout.setLayoutParams(marginLayoutParams)
                }


                // 1 = depedent, 0 = Indepedent (spinner depedent or not)
                if (registerFieldsModel.isDepedent == true) {

                    //for caste
                    if (registerFieldsModel.hint!!.contains("Caste")) {
                        if (casteLists.size == 0) {

                            // for first time inlialize
                            (holder).spinner.setItems(
                                (holder).spinner, Common.getDefaultSpinnerListFromArray(
                                    registerFieldsModel.hint
                                ), -1, this, registerFieldsModel.hint
                            )
                        } else {
                            (holder).spinner.setItems(
                                (holder).spinner, casteLists, -1, this, registerFieldsModel.hint
                            )
                            //if user filled value and goto next and return back then set that selected value
                            if (registerFieldsModel.filledValue.length > 0) {
                                (holder).spinner.setSelection(
                                    registerFieldsModel.filledValue
                                )
                            }
                        }
                    }
                    //for state
                    if (registerFieldsModel.hint.contains("State")) {
                        if (stateLists.size == 0) {
                            // for first time inlialize

                            (holder).spinner.setItems(
                                (holder).spinner, Common.getDefaultSpinnerListFromArray(
                                    registerFieldsModel.hint
                                ), -1, this, registerFieldsModel.hint
                            )
                        } else {
                            (holder).spinner.setItems(
                                (holder).spinner, stateLists, -1, this, registerFieldsModel.hint
                            )
                            //if user filled value and goto next and return back then set that selected value
                            if (registerFieldsModel.filledValue.length > 0) {
                                (holder).spinner.setSelection(
                                    registerFieldsModel.filledValue
                                )
                            }
                        }
                    }
                    //for city
                    if (registerFieldsModel.hint.contains("City")) {
                        if (cityLists.size == 0) {
                            // for first time inlialize
                            (holder).spinner.setItems(
                                (holder).spinner, Common.getDefaultSpinnerListFromArray(
                                    registerFieldsModel.hint
                                ), -1, this, registerFieldsModel.hint
                            )
                        } else {

                            (holder).spinner.setItems(
                                (holder).spinner, cityLists, -1, this, registerFieldsModel.hint
                            )
                            //if user filled value and goto next and return back then set that selected value
                            if (registerFieldsModel.filledValue.length > 0) {
                                (holder).spinner.setSelection(
                                    registerFieldsModel.filledValue
                                )
                            }
                        }
                    }
                } else {

                    if (registerFieldsModel.staticArray == true) {

                        (holder).spinner.setItems(
                            (holder).spinner, Common.getSpinnerListFromArrayWithAll(
                                registerFieldsModel.dropdownListValue
                            ), -1, this, registerFieldsModel.hint
                        )
                    } else {
                        val obj1: JsonObject = jsonObject.get("data").asJsonObject
                        val jsonArr: JsonArray =
                            obj1.get(registerFieldsModel.dropdownListKey).asJsonArray

                        (holder).spinner.setItems(
                            (holder).spinner, Common.getSpinnerListFromArray(
                                jsonArr
                            ), -1, this, registerFieldsModel.hint!!
                        )
                    }

                    //if user filled value and goto next and return back then set that selected value
                    if (registerFieldsModel.filledValue.length > 0) {
                        (holder).spinner.setSelection(
                            registerFieldsModel.filledValue
                        )
                    }
                }
                if (registerFields.size - 1 == position) {
                    setListerner = false
                }

            }
            TYPE_MULTI_SPINNER -> {

                // set position as a tag for use in onItemsSelected function
                (holder as MultiSpinnerViewHolder).multiSpinner.setTag(
                    position
                )

                // 1 = depedent, 0 = Indepedent (spinner depedent or not)
                if (registerFieldsModel.isDepedent == true) {

                    //for caste
                    if (registerFieldsModel.hint!!.contains("Caste")) {
                        if (casteLists.size == 0) {

                            // for first time inlialize
                            (holder).multiSpinner.setItems(
                                (holder).multiSpinner, Common.getDefaultSpinnerListFromArray(
                                    registerFieldsModel.hint
                                ), -1, this, registerFieldsModel.hint
                            )
                        } else {
                            (holder).multiSpinner.setItems(
                                (holder).multiSpinner,
                                casteLists,
                                -1,
                                this,
                                registerFieldsModel.hint
                            )
                            //if user filled value and goto next and return back then set that selected value
                            if (registerFieldsModel.filledValue.length > 0) {
                                (holder).multiSpinner.setSelection(
                                    registerFieldsModel.filledValue
                                )
                            }
                        }
                    }
                    //for state
                    if (registerFieldsModel.hint.contains("State")) {
                        if (stateLists.size == 0) {
                            // for first time inlialize

                            (holder).multiSpinner.setItems(
                                (holder).multiSpinner, Common.getDefaultSpinnerListFromArray(
                                    registerFieldsModel.hint
                                ), -1, this, registerFieldsModel.hint
                            )
                        } else {
                            (holder).multiSpinner.setItems(
                                (holder).multiSpinner,
                                stateLists,
                                -1,
                                this,
                                registerFieldsModel.hint
                            )
                            //if user filled value and goto next and return back then set that selected value
                            if (registerFieldsModel.filledValue.length > 0) {
                                (holder).multiSpinner.setSelection(
                                    registerFieldsModel.filledValue
                                )
                            }
                        }
                    }
                    //for city
                    if (registerFieldsModel.hint.contains("City")) {
                        if (cityLists.size == 0) {
                            // for first time inlialize
                            (holder).multiSpinner.setItems(
                                (holder).multiSpinner, Common.getDefaultSpinnerListFromArray(
                                    registerFieldsModel.hint
                                ), -1, this, registerFieldsModel.hint
                            )
                        } else {

                            (holder).multiSpinner.setItems(
                                (holder).multiSpinner, cityLists, -1, this, registerFieldsModel.hint
                            )
                            //if user filled value and goto next and return back then set that selected value
                            if (registerFieldsModel.filledValue.length > 0) {
                                (holder).multiSpinner.setSelection(
                                    registerFieldsModel.filledValue
                                )
                            }
                        }
                    }
                } else {
                    if (registerFieldsModel.staticArray == true) {

                        (holder).multiSpinner.setItems(
                            (holder).multiSpinner, Common.getSpinnerListFromArrayWithAll(
                                registerFieldsModel.dropdownListValue
                            ), -1, this, registerFieldsModel.hint
                        )
                    } else {
                        val obj1: JsonObject = jsonObject.get("data").asJsonObject
                        val jsonArr: JsonArray =
                            obj1.get(registerFieldsModel.dropdownListKey).asJsonArray

                        (holder as MultiSpinnerViewHolder).multiSpinner.setItems(
                            (holder).multiSpinner, Common.getSpinnerListFromArray(
                                jsonArr
                            ), -1, this, registerFieldsModel.hint!!
                        )
                    }
                }


                //if user filled value and goto next and return back then set that selected value
                if (registerFieldsModel.filledValue.length > 0) {
                    (holder).multiSpinner.setSelection(
                        registerFieldsModel.filledValue
                    )
                }
            }
            TYPE_COUNTRY_CODE -> {
                //if user filled value and goto next and return back then set that filled value
                if (registerFieldsModel.filledValue.length > 0) {
                    val numberArr = registerFieldsModel.filledValue.split("-").toTypedArray()

                    (holder as CountryCodeViewHolder).countryCodePicker.setCountryForPhoneCode(
                        numberArr[0].toInt()
                    )
                    (holder).etPhoneNumber.setText(
                        numberArr[1]
                    )
                }
            }

            TYPE_RADIO_BUTTON -> {
                (holder as RadioViewHolder).radioGroup.setOrientation(LinearLayout.HORIZONTAL)

                try {
                    val data: JsonArray = registerFieldsModel.dropdownListValue
                    for (i in 0..data.size()) {

                        val obj: JsonObject = data[i].asJsonObject
                        val radioButton = RadioButton(mContext)

                        //set style and
                        radioButton.setTextColor(R.color.radio_text_color)
                        radioButton.setButtonDrawable(null)
                        radioButton.setBackgroundResource(R.drawable.toggle_widget_background)
                        // set margin
                        val params: RadioGroup.LayoutParams = RadioGroup.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT, 1f
                        )
                        radioButton.textAlignment = TEXT_ALIGNMENT_CENTER
                        params.setMargins(0, 10, 10, 10)
                        radioButton.setLayoutParams(params)

                        radioButton.setTag(obj.get("id").asString)
                        radioButton.setText(obj.get("val").asString)
                        (holder).radioGroup.addView(radioButton)

                        //if user filled value and goto next and return back then set that selected value
                        if (registerFieldsModel.filledValue.length > 0) {
                            if (registerFieldsModel.filledValue.equals(
                                    obj.get("id").asString,
                                    false
                                )
                            ) {
                                radioButton.isChecked = true
                            }
                        }

                    }

                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return registerFields.size
    }

    fun getAdapterList(): List<RegisterFieldsModel?>? {
        return registerFields
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        init {
        }
    }

    fun setListener(listener: ItemListener?) {
        myListener = listener
    }

    interface ItemListener {
        fun getDependentList(
            dependentRegisterFieldsModel: RegisterFieldsModel?,
            selectedId: String,
            tag: String,
            position: Int
        )
    }

    internal class LoadHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!)

    class SpinnerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        val spinner: SingleSpinnerSearch
        val mainLayout: RelativeLayout
        override fun onClick(v: View) {
            if (absoluteAdapterPosition != RecyclerView.NO_POSITION) {
                val imageView = v.tag as ImageView
                //myListener.chooseImage(imageView);
            }
        }

        init {
            spinner = itemView.findViewById(R.id.spinner)
            mainLayout = itemView.findViewById(R.id.mainLayout)
        }
    }

    class MultiSpinnerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        val multiSpinner: MultiSpinnerSearch
        override fun onClick(v: View) {
            if (absoluteAdapterPosition != RecyclerView.NO_POSITION) {
                val imageView = v.tag as ImageView
                //myListener.chooseImage(imageView);
            }
        }

        init {
            multiSpinner = itemView.findViewById(R.id.multiSpinner)
        }
    }

    class RadioViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        val radioGroup: RadioGroup
        override fun onClick(v: View) {
            if (absoluteAdapterPosition != RecyclerView.NO_POSITION) {
                val imageView = v.tag as ImageView
                //myListener.chooseImage(imageView);
            }
        }

        init {
            radioGroup = itemView.findViewById(R.id.radioGroup)
        }
    }

    class EditTextViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        val editText: EditText
        val textInputLayout: TextInputLayout
        override fun onClick(v: View) {
            AppDebugLog.print("open dialog here")
        }

        init {
            editText = itemView.findViewById(R.id.editText)
            textInputLayout = itemView.findViewById(R.id.textInputLayout)
        }
    }

    class CountryCodeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        val layoutCountryCode: LinearLayout
        val countryCodePicker: CountryCodePicker
        val etPhoneNumber: EditText
        override fun onClick(v: View) {
            if (absoluteAdapterPosition != RecyclerView.NO_POSITION) {
            }
        }

        init {
            layoutCountryCode = itemView.findViewById(R.id.layoutCountryCode)
            countryCodePicker = itemView.findViewById(R.id.countryCodePicker)
            etPhoneNumber = itemView.findViewById(R.id.etPhoneNumber)
            countryCodePicker.setDialogKeyboardAutoPopup(false)
        }
    }

    class ButtonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        private val btnContinue: Button
        override fun onClick(v: View) {
            if (absoluteAdapterPosition != RecyclerView.NO_POSITION) {
                // myListener.updateClicked();
            }
        }

        init {
            btnContinue = itemView.findViewById(R.id.btnContinue)
            btnContinue.setOnClickListener(this)
        }
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        var monthStr =
            if (month.toString().length < 2) "0" + (month + 1).toString() else (month + 1).toString()
        var dayOfMonthStr =
            if (dayOfMonth.toString().length < 2) "0" + (dayOfMonth).toString() else (dayOfMonth).toString()
        strDOB = year.toString() + "-" + monthStr + "-" + dayOfMonthStr
        notifyItemChanged(dobCellPosition)
    }

    override fun onItemsSelected(multiSpinnerSearch: MultiSpinnerSearch?) {
        AppDebugLog.print("selected ids : " + multiSpinnerSearch!!.selectedIdsInString)

        val selectedId = multiSpinnerSearch.selectedIdsInString

        val position = multiSpinnerSearch.tag as Int
        val registerFieldsModel: RegisterFieldsModel = registerFields.get(position)

        val dependentPosition: Int
        var dependentRegisterFieldsModel: RegisterFieldsModel? = null

        if (registerFields.size != position + 1) {
            dependentPosition = position + 1
            dependentRegisterFieldsModel = registerFields.get(dependentPosition)
        }

        AppDebugLog.print("selected ids registerFieldsModel : " + registerFieldsModel.filledValue)

        if (registerFieldsModel.hasDependent == true) {
            myListener?.getDependentList(
                dependentRegisterFieldsModel,
                selectedId!!,
                registerFieldsModel.depedentListKey,
                position
            )
        }
    }

    override fun onItemsSelected(
        singleSpinnerSearch: SingleSpinnerSearch?,
        item: KeyPairBoolData?
    ) {
        var selectedId: String? = ""
        if (item != null) selectedId = item.id

        val position = singleSpinnerSearch!!.tag as Int
        val registerFieldsModel: RegisterFieldsModel = registerFields.get(position)

        var dependentPosition: Int
        var dependentRegisterFieldsModel: RegisterFieldsModel? = null

        if (registerFields.size != position + 1) {
            dependentPosition = position + 1
            dependentRegisterFieldsModel = registerFields.get(dependentPosition)
        }

        if (registerFieldsModel.hasDependent == true) {
            myListener?.getDependentList(
                dependentRegisterFieldsModel,
                selectedId!!,
                registerFieldsModel.depedentListKey,
                position
            )
        }

        AppDebugLog.print("registerFieldsModel.apiParamKey :" + registerFieldsModel.apiParamKey)
        if (!setListerner) {
            if (registerFieldsModel.apiParamKey.equals("marital_status")) {
                registerFieldsModel.filledValue = selectedId!!
                if (!selectedId.equals("Unmarried")) {

                    //get total_children object for show hide
                    registerFields.get(position + 1).isVisible = true

                    //get status_children object for show hide
                    registerFields.get(position + 2).isVisible = true

                    setListerner = true
                    notifyDataSetChanged()
                } else {
                    //get total_children object for show hide
                    registerFields.get(position + 1).isVisible = false

                    //get status_children object for show hide
                    registerFields.get(position + 2).isVisible = false
                    setListerner = true
                    notifyDataSetChanged()
                }
            }
        }

    }

    fun setCasteList(casteList: List<KeyPairBoolData?>?, position: Int) {
        this.casteLists = casteList as List<KeyPairBoolData>
        notifyItemChanged(position + 1)
    }

    fun setStateList(stateList: List<KeyPairBoolData?>?, position: Int) {
        this.stateLists = stateList as List<KeyPairBoolData>
        notifyItemChanged(position + 1)
    }

    fun setCityList(cityList: List<KeyPairBoolData?>?, position: Int) {
        this.cityLists = cityList as List<KeyPairBoolData>
        notifyItemChanged(position + 1)
    }
}
