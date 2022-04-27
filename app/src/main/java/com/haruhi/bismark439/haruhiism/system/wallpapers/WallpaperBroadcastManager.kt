package com.haruhi.bismark439.haruhiism.system.wallpapers

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.haruhi.bismark439.haruhiism.Debugger
import com.haruhi.bismark439.haruhiism.activities.navigation_ui.wallpaper_setting.MyWallpaperOption

object WallpaperBroadcastManager {
    private const val wallpaperCode = 346765
    fun updateWallpaper(context: Context, option: MyWallpaperOption, immediate: Boolean) {
        if (option.isEnabled) {
            addWallpaperChanger(context, option)
            if (immediate) {
                val uri = option.getNextUri(context) ?: return
                WallpaperHandler.setWallpaper(context, uri, option)
            }
        } else {
            removeWallpaperChanger(context)
        }
    }

    private fun addWallpaperChanger(context: Context, option: MyWallpaperOption) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, WallpaperReceiver::class.java)
        val pi = PendingIntent.getBroadcast(
            context,
            wallpaperCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val timeInterval: Long = option.getTimeUnitInMills() * option.timeVal
        Debugger.log("Set internal ${(timeInterval/1000).toDouble() / 60} minutes")
        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            System.currentTimeMillis() + timeInterval,
            timeInterval,
            pi
        )
    }

    private fun removeWallpaperChanger(mContext: Context) {
        val alarmManager = mContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(mContext, WallpaperReceiver::class.java)
        val pi = PendingIntent.getBroadcast(
            mContext,
            wallpaperCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        pi.cancel()
        alarmManager.cancel(pi)
    }

}