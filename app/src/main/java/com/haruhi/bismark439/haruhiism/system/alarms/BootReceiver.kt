package com.haruhi.bismark439.haruhiism.system.alarms

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.haruhi.bismark439.haruhiism.Debugger
import kotlinx.coroutines.DelicateCoroutinesApi

/**
 * Created by Bismark439 on 13/01/2018.
 */
@DelicateCoroutinesApi
class BootReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {

        Debugger.log("action ${intent.action}")
        if (intent.action == Intent.ACTION_BOOT_COMPLETED
            || intent.action == Intent.ACTION_REBOOT
            || intent.action == Intent.ACTION_DATE_CHANGED
            || intent.action == Intent.ACTION_MY_PACKAGE_REPLACED
            || intent.action == Intent.ACTION_AIRPLANE_MODE_CHANGED
        ) {
            val mintent = Intent(context, BootingService::class.java)
            context.startForegroundService(mintent)
        }
    }

}