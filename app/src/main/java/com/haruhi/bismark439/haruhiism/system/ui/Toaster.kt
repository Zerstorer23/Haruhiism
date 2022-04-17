package com.haruhi.bismark439.haruhiism.system.ui

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.haruhi.bismark439.haruhiism.R
import java.util.*

object Toaster {
    fun show(context: Context, msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }

    fun showSnackBar(activity: Activity, msg: String) {
        val snackBar = Snackbar.make(
            activity.findViewById(android.R.id.content),
            msg, Snackbar.LENGTH_SHORT
        )
        val snView = snackBar.view
        snView.setBackgroundColor(
            ContextCompat.getColor(
                activity.applicationContext,
                R.color.light_blue_600
            )
        )

        snackBar.show()

    }

    fun showDatePicker(context:Context, setter: (l:String)->Unit) {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        val dpd = DatePickerDialog(
            context,
            { _, mYear, mMonth, dayOfMonth ->
                var sDay = dayOfMonth.toString()
                if (dayOfMonth < 10) sDay = "0${sDay}"
                var sMonth = month.toString()
                if (mMonth < 10) sMonth = "0${sMonth}"
                val selectedDate = "$sDay/$sMonth/$mYear"
                setter(selectedDate)
            }, year, month, day
        )
        dpd.show()
    }
}