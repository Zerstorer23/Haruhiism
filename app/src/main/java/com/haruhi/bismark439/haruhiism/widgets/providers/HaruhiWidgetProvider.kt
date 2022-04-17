package com.haruhi.bismark439.haruhiism.widgets.providers

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.MediaPlayer
import com.haruhi.bismark439.haruhiism.R
import com.haruhi.bismark439.haruhiism.widgets.providers.WidgetCreater.SRC_WIDGET
import java.io.IOException

/**
 * Created by Bismark439 on 12/01/2018.
 */
class HaruhiWidgetProvider : AppWidgetProvider() {
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        val calendar = WidgetCreater.getCalendar(2020, 10, 25)
        val title = context.getString(R.string.beforelast)
        val bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.haruhi1)
        val ui = WidgetCreater.createPresetUI(
            context, appWidgetIds, title, "#eeed7e10", calendar, bitmap,
            KYON_KUN_DENWA
        )
        for (widgetId in appWidgetIds) {
            appWidgetManager.updateAppWidget(widgetId, ui)
        }
    }


    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        val folderName: String
        val listName: String
        when(intent.getStringExtra(SRC_WIDGET)){
            KYON_KUN_DENWA->{
                folderName = "haruhi"
                listName = "haruhi"
            }
            MIKURU_WIDGET->{
                folderName = "mikuru"
                listName = "mikuru"
            }
            else->{
                return
            }
        }
        val assetManager = context.assets
        try {
            HRHvoice = assetManager.list(listName) as Array<String>
        } catch (e: IOException) {
            e.printStackTrace()
            return
        }
        playSound(context, folderName)
    }


    private fun playSound(context: Context, folder: String) {
        val mMediaPlayer = MediaPlayer()
        try {
            val rand = (Math.random() * HRHvoice.size).toInt()
            println("RANDOM " + rand + " / " + HRHvoice.size)
            val afd = context.assets.openFd(folder + "/" + HRHvoice[rand])
            mMediaPlayer.setDataSource(afd.fileDescriptor, afd.startOffset, afd.length)
            mMediaPlayer.prepare()
            mMediaPlayer.start()
            mMediaPlayer.setOnCompletionListener { mediaPlayer -> mediaPlayer.release() }
        } catch (e: IOException) {
            e.localizedMessage
            e.printStackTrace()
        }
    }

    companion object {
        var KYON_KUN_DENWA = "KyonKunDenwa"
        var MIKURU_WIDGET = "MikuruWidget"
        private lateinit var HRHvoice: Array<String>
    }
}