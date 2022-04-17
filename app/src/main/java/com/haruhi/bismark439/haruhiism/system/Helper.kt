@file:Suppress("DEPRECATION")

package com.haruhi.bismark439.haruhiism.system

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import java.util.*

object Helper {

    @SuppressLint("MissingPermission")
    fun vibrate(context:Context, duration:Long = 500){
        val vibrationManager: Vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        vibrationManager.vibrate(VibrationEffect.createOneShot(duration, VibrationEffect.DEFAULT_AMPLITUDE)) // New vibrate method for API Level 26 or higher
    }
    fun convertDec(i: Int): String {
        if (i < 10) {
            return "0$i"
        }
        return i.toString()
    }

}