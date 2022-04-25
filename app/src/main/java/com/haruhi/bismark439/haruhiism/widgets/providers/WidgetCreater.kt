package com.haruhi.bismark439.haruhiism.widgets.providers

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.media.MediaPlayer
import android.widget.RemoteViews
import com.haruhi.bismark439.haruhiism.DEBUG
import com.haruhi.bismark439.haruhiism.R
import com.haruhi.bismark439.haruhiism.model.widgetDB.WidgetData
import com.haruhi.bismark439.haruhiism.model.widgetDB.toCharacterFolder
import com.haruhi.bismark439.haruhiism.system.StorageManager
import java.io.IOException
import java.util.*
import kotlin.math.abs
import kotlin.reflect.KClass

object WidgetCreater {
    fun onCounterClicked(context: Context, intent: Intent) {
        val src = intent.getStringExtra(SRC_WIDGET)
        if (src == null || src.isEmpty()) {
            return
        }
        try {
            val fileList = StorageManager.readFilesFromAsset(context, src)
            playSound(context, src, fileList)
        } catch (e: IOException) {
            e.printStackTrace()
            return
        }
    }

    fun playSound(context: Context, folder: String, fileList: Array<String>) {
        val mMediaPlayer = MediaPlayer()
        try {
            val rand = (Math.random() * fileList.size).toInt()
            val afd = context.assets.openFd(folder + "/" + fileList[rand])
            mMediaPlayer.setDataSource(afd.fileDescriptor, afd.startOffset, afd.length)
            mMediaPlayer.prepare()
            mMediaPlayer.start()
            mMediaPlayer.setOnCompletionListener { mediaPlayer -> mediaPlayer.release() }
        } catch (e: IOException) {
            e.localizedMessage
            e.printStackTrace()
        }
    }

    fun createPresetUI(
        context: Context,
        appWidgetIds: IntArray,
        title: String,
        colorString: String,
        time: Calendar,
        bitmap: Bitmap,
        srcName: String,
        intent: Intent
    ): RemoteViews {
        val remoteViews = RemoteViews(context.packageName, R.layout.widget_default_counter)
        val days = getDays(time) //
        val textColor = Color.parseColor(colorString)
        setDdayText(remoteViews, context, days, title)
        setColors(remoteViews, textColor)
        remoteViews.setImageViewBitmap(R.id.widgetImage, bitmap)
        setOnClickIntent(context, intent, appWidgetIds, srcName, remoteViews)
        return remoteViews
    }

    fun createUI(context: Context, widgetData: WidgetData): RemoteViews {
        val remoteViews = RemoteViews(context.packageName, R.layout.widget_default_counter)
        DEBUG.appendLog("UI update called on ${remoteViews.layoutId}")
        val days: Long = getDays(getCalendar(widgetData.yy, widgetData.mmMod, widgetData.dd))
        setDdayText(remoteViews, context, days, widgetData.name)
        setColors(remoteViews, widgetData.color)
        remoteViews.setImageViewResource(R.id.widgetImage, widgetData.picture)
        val intent = Intent(context, DayCounterProvider::class.java)
        setOnClickIntent(
            context,
            intent,
            null,
            widgetData.widgetCharacter.toCharacterFolder(),
            remoteViews
        )
        return remoteViews
    }

    private fun setOnClickIntent(
        context: Context,
        intent: Intent,
        appWidgetIds: IntArray?,
        srcName: String,
        remoteViews: RemoteViews
    ) {
        intent.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
        if (appWidgetIds != null) {
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds)
        }
        intent.putExtra(SRC_WIDGET, srcName)
        val pi = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        remoteViews.setOnClickPendingIntent(R.id.widgetImage, pi)
    }

    private fun setDdayText(remoteViews: RemoteViews, context: Context, days: Long, title: String) {
        val absDays = abs(days)
        when {
            days < 0 -> {
                remoteViews.setTextViewText(R.id.daysText, absDays.toString())
                remoteViews.setTextViewText(
                    R.id.sinceWhen,
                    context.getString(R.string.beforelast, title)
                )
            }
            (days == 0L) -> {
                remoteViews.setTextViewText(R.id.daysText, context.getString(R.string.todaydesu))
                remoteViews.setTextViewText(R.id.days2, " ")
                remoteViews.setTextViewText(
                    R.id.sinceWhen, title
                )
            }
            else -> {
                remoteViews.setTextViewText(R.id.daysText, "$days")
                remoteViews.setTextViewText(
                    R.id.sinceWhen,
                    context.getString(R.string.sincelast, title)
                )
            }
        }
    }

    private fun setColors(remoteViews: RemoteViews, color: Int) {
        remoteViews.setTextColor(R.id.daysText, color) //
        remoteViews.setTextColor(R.id.days2, color)
        remoteViews.setTextColor(R.id.sinceWhen, color)
    }

    private fun getDays(target: Calendar): Long {
        val today = Calendar.getInstance() //TimeZone.getTimeZone("GMT"));
        DEBUG.appendLog("Target Cal: " + target[Calendar.YEAR] + "Y / " + (target[Calendar.MONTH] + 1) + "M / " + target[Calendar.DATE] + " D")
        DEBUG.appendLog("Today Cal: " + today[Calendar.YEAR] + "Y / " + (today[Calendar.MONTH] + 1) + "M / " + today[Calendar.DATE] + " D")
        if (today[Calendar.YEAR] == target[Calendar.YEAR]
            && today[Calendar.MONTH] == target[Calendar.YEAR]
            && today[Calendar.DATE] == target[Calendar.YEAR]
        ) {
            return 0
        }
        return (today.timeInMillis - target.timeInMillis) / DAY_IN_MILLI
    }

    fun getCalendar(y: Int, m: Int, d: Int): Calendar {
        val calendar = Calendar.getInstance() //TimeZone.getTimeZone("GMT") );
        calendar.set(y, m, d, 0, 0)
        return calendar
    }

    private const val DAY_IN_MILLI = (1000 * 60 * 60 * 24)
    const val SRC_WIDGET = "SourceWidget"

}

