package com.haruhi.bismark439.haruhiism.system.wallpapers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.haruhi.bismark439.haruhiism.DEBUG
import com.haruhi.bismark439.haruhiism.activities.navigation_ui.wallpaper_setting.MyWallpaperOption

class WallpaperReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val option = MyWallpaperOption.loadData(context)
        option.readFiles(context)
        DEBUG.appendLog("Received wallpaper")
        setWallpaper(context, option)
    }

    private fun setWallpaper(context: Context, option: MyWallpaperOption) {
        val imgUri = option.getNextUri(context) ?: return
        WallpaperHandler.setWallpaper(context, imgUri, option)
    }
}