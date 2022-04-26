@file:Suppress("DEPRECATION")

package com.haruhi.bismark439.haruhiism.system

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.VibrationEffect
import android.os.Vibrator
import java.security.MessageDigest

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

    fun checkInternet(context: Context, vararg types:Int ): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            for(t in types){
                if(capabilities.hasTransport(t)) return true
            }
        }
        return false
    }
}