package com.haruhi.bismark439.haruhiism

import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import com.haruhi.bismark439.haruhiism.system.alarms.BootReceiver

class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext

    }

    companion object {
        lateinit var appContext: Context
        fun setContext(context: Context) {
            appContext = context
        }
    }
}