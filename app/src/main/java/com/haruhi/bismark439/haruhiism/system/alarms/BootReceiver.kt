package com.haruhi.bismark439.haruhiism.system.alarms

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import com.haruhi.bismark439.haruhiism.activities.MainActivity
import com.haruhi.bismark439.haruhiism.model.alarmDB.AlarmDB
import com.haruhi.bismark439.haruhiism.model.alarmDB.AlarmDao
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/**
 * Created by Bismark439 on 13/01/2018.
 */
@DelicateCoroutinesApi
class BootReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        Handler(Looper.getMainLooper()).post {
            Toast.makeText(
                context,
                "action ${intent.action}",
                Toast.LENGTH_SHORT
            ).show()
        }
        println("action ${intent.action}")
        if (intent.action == Intent.ACTION_BOOT_COMPLETED
            || intent.action == Intent.ACTION_REBOOT
            || intent.action == Intent.ACTION_DATE_CHANGED
            || intent.action == Intent.ACTION_MY_PACKAGE_REPLACED
            || intent.action == Intent.ACTION_AIRPLANE_MODE_CHANGED
        ) {
            val mintent = Intent(context, MyService::class.java)
            context.startForegroundService(mintent)
        }
    }

}