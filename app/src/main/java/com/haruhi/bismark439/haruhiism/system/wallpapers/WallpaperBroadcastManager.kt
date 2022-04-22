package com.haruhi.bismark439.haruhiism.system.wallpapers

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.haruhi.bismark439.haruhiism.activities.navigation_ui.wallpaper_setting.MyWallpaperOption
import com.haruhi.bismark439.haruhiism.activities.navigation_ui.wallpaper_setting.WallpaperSettingFragment

object WallpaperBroadcastManager {
    private const val wallpaperCode = 346
    fun updateWallpaper(context: Context, option: MyWallpaperOption, immediate: Boolean) {
        if (option.isEnabled) {
            addWallpaperChanger(context)
            if(immediate){
                WallpaperHandler.setWallpaper(context,option.getNextUri(context))
            }
        } else {
            removeWallpaperChanger(context)
        }
    }

    private fun addWallpaperChanger(mContext: Context) {
        val alarmManager = mContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(mContext, WallpaperReceiver::class.java)
        val pi = PendingIntent.getBroadcast(
            mContext,
            wallpaperCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val timeInterval : Long =  10*1000L//option.getTimeUnitInMills()
        alarmManager.setInexactRepeating(
            AlarmManager.RTC,
            System.currentTimeMillis()+timeInterval,
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