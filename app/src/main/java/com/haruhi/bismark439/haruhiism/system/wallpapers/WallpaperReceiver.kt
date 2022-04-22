package com.haruhi.bismark439.haruhiism.system.wallpapers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.haruhi.bismark439.haruhiism.activities.navigation_ui.wallpaper_setting.MyWallpaperOption
import com.haruhi.bismark439.haruhiism.activities.navigation_ui.wallpaper_setting.WallpaperSettingFragment

class WallpaperReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val option = MyWallpaperOption.loadData(context)
        option.readFiles(context)
        println("Received wallpaper")
        setWallpaper(context, option)
    }

    private fun setWallpaper(context: Context, option: MyWallpaperOption) {
        val imgUri = option.getNextUri(context)
        val bitmap = WallpaperHandler.loadBitmapFromUri(context, imgUri)
        WallpaperHandler.setWallpaper(context, bitmap)
    }
}