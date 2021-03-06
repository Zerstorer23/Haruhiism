@file:Suppress("DEPRECATION")

package com.haruhi.bismark439.haruhiism.system

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.VibrationEffect
import android.os.Vibrator
import java.security.MessageDigest
import java.text.SimpleDateFormat
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
    private fun hash(text:String): String {
        val bytes = text.toByteArray()
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(bytes)
        return digest.fold("") { str, it -> str + "%02x".format(it) }
    }
    fun String.toHash():String{
        return hash(this)
    }
    fun getTimeString(context: Context, timex: Long): String {
        val date = Date(timex)
        val locale = (context.resources.configuration.locales[0])
        val sdf = SimpleDateFormat("M/d hh:mm aa", locale)
        sdf.timeZone = TimeZone.getDefault()
        return sdf.format(date)
    }

}