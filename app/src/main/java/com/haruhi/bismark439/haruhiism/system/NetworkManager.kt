package com.haruhi.bismark439.haruhiism.system

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities

object NetworkManager {
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
    fun hasWifi(context: Context):Boolean{
        return checkInternet(context, NetworkCapabilities.TRANSPORT_WIFI)
    }
    fun hasInternet(context: Context): Boolean {
        return checkInternet(context, NetworkCapabilities.TRANSPORT_WIFI, NetworkCapabilities.TRANSPORT_CELLULAR, NetworkCapabilities.TRANSPORT_ETHERNET)
    }
}