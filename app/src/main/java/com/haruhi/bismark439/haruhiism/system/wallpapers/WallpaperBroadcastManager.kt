package com.haruhi.bismark439.haruhiism.system.wallpapers

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.haruhi.bismark439.haruhiism.Debugger
import com.haruhi.bismark439.haruhiism.activities.navigation_ui.wallpaper_setting.MyWallpaperOption
import com.haruhi.bismark439.haruhiism.system.Helper

object WallpaperBroadcastManager {
    private const val wallpaperCode = 346765
    private val receiverClass = WallpaperReceiver::class.java

    fun updateWallpaper(context: Context, option: MyWallpaperOption) {
        removeWallpaperChanger(context)
        if (option.isEnabled) {
            addWallpaperChanger(context, option)
        }
    }

    private fun addWallpaperChanger(context: Context, option: MyWallpaperOption) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, receiverClass)
        val pi = PendingIntent.getBroadcast(
            context,
            wallpaperCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val timeInterval: Long =
            MyWallpaperOption.getTimeInterval(option.timeUnit, option.timeVal)
        val nextTrigger: Long = if(option.lastSet == 0L){
            System.currentTimeMillis()
        }else{
            option.lastSet + timeInterval
        }
        Debugger.log("Set intevnal ${(timeInterval / 1000).toDouble() / 60} minutes")
        Debugger.log("Next change at${Helper.getTimeString(context, nextTrigger)}")
        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            nextTrigger,
            timeInterval,
            pi
        )
    }

    private fun removeWallpaperChanger(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, receiverClass)
        val pi = PendingIntent.getBroadcast(
            context, wallpaperCode, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        pi.cancel()
        alarmManager.cancel(pi)
    }

}