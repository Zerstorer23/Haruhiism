package com.haruhi.bismark439.haruhiism.system.ui

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.haruhi.bismark439.haruhiism.R
import com.haruhi.bismark439.haruhiism.model.alarmDB.AlarmData
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

}