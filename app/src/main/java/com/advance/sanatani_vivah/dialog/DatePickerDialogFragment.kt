package com.advance.sanatani_vivah.dialog

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.advance.sanatani_vivah.R
import com.advance.sanatani_vivah.utility.AppConstants
import com.advance.sanatani_vivah.utility.Common
import java.util.*

class DatePickerDialogFragment : DialogFragment() {
    var onDateSet: DatePickerDialog.OnDateSetListener? = null
    fun setCallBack(ondate: DatePickerDialog.OnDateSetListener?) {
        onDateSet = ondate
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val bundle = arguments
        val dateStr = bundle!!.getString("date")
        val date: Date? = Common.getDateFromDateString(AppConstants.serverBirthDateFormat, dateStr)
        val calendar = Calendar.getInstance()
        calendar.time = date
        val calendar1 = Calendar.getInstance()
        //For above 18 years date
        calendar1.add(Calendar.YEAR, -18)
        calendar1.add(Calendar.MONTH, -1)
        calendar1.add(Calendar.DATE, -1)
        val day = calendar[Calendar.DAY_OF_MONTH]
        val month = calendar[Calendar.MONTH]
        val year = calendar[Calendar.YEAR]
        val datePickerDialog =
            DatePickerDialog(requireContext(), R.style.DialogTheme, onDateSet, year, month, day)
        datePickerDialog.datePicker.maxDate = calendar1.time.time
        datePickerDialog.setOnShowListener { arg0: DialogInterface? ->
            datePickerDialog.getButton(DatePickerDialog.BUTTON_NEGATIVE).setTextColor(
                resources.getColor(R.color.colorAccent)
            )
            datePickerDialog.getButton(DatePickerDialog.BUTTON_POSITIVE).setTextColor(
                resources.getColor(R.color.colorAccent)
            )
        }
        return datePickerDialog
    }
}