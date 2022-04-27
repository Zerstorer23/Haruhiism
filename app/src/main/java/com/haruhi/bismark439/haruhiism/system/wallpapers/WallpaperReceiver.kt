package com.haruhi.bismark439.haruhiism.system.wallpapers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.haruhi.bismark439.haruhiism.Debugger
import com.haruhi.bismark439.haruhiism.activities.navigation_ui.wallpaper_setting.MyWallpaperOption
import com.haruhi.bismark439.haruhiism.activities.navigation_ui.wallpaper_setting.MyWallpaperOption.Companion.LAST_SET
import com.haruhi.bismark439.haruhiism.system.StorageManager
import com.haruhi.bismark439.haruhiism.system.TimeUnit

class WallpaperReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val option = MyWallpaperOption.loadData(context)
       // if (!needUpdateNow(context)) return
        option.readFiles(context)
        Debugger.log("Received wallpaper")
        setWallpaper(context, option)
    }

    private fun needUpdateNow(context: Context): Boolean {
        val reader = StorageManager.getPrefReader(context)
        val lastTime = reader.getLong(LAST_SET, 0L)
        val timeVal = reader.getInt(MyWallpaperOption.TIME_VAL, 1)
        val timeUnit = TimeUnit.values()[reader.getInt(MyWallpaperOption.TIME_UNIT, 0)]
        val timeInterval = MyWallpaperOption.getTimeInterval(timeUnit, timeVal)
        val diff = kotlin.math.abs(System.currentTimeMillis() - (lastTime + timeInterval))
        if (diff >= (60 * 1000L) ) return true
        return false
    }

    private fun setWallpaper(context: Context, option: MyWallpaperOption) {
        val imgUri = option.getNextUri(context) ?: return
        WallpaperHandler.setWallpaper(context, imgUri, option)
    }
}