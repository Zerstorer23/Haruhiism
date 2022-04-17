package com.haruhi.bismark439.haruhiism.widgets.providers

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.widget.RemoteViews
import com.haruhi.bismark439.haruhiism.R
import com.haruhi.bismark439.haruhiism.model.widgetDB.WidgetData
import java.util.*
import kotlin.math.abs

object WidgetCreater {
    fun createUI(context: Context, widgetData: WidgetData): RemoteViews {
        val remoteViews = RemoteViews(context.packageName, R.layout.widget_haruhi)
        println("UI update called on ${remoteViews.layoutId}")
        var days: Long = getDays(getCalendar(widgetData.yy, widgetData.mmMod, widgetData.dd))
        remoteViews.setTextViewText(R.id.sinceWhen, widgetData.name)
        if (days == 0L) {
            remoteViews.setTextViewText(R.id.daysText, "Today!")
            remoteViews.setTextViewText(R.id.days2, " ")
        } else {
            days = abs(days)
            remoteViews.setTextViewText(R.id.daysText, days.toString() + "")
            remoteViews.setTextViewText(R.id.days2, context.getString(R.string.days))
        }
        //val today = Calendar.getInstance()
       // val txt = "${today.get(Calendar.HOUR_OF_DAY)} : ${today.get(Calendar.MINUTE)}"
       // remoteViews.setTextViewText(R.id.daysText, txt) println("Set text $txt")
        remoteViews.setTextColor(R.id.daysText, widgetData.color)
        remoteViews.setTextColor(R.id.days2, widgetData.color)
        remoteViews.setTextColor(R.id.sinceWhen, widgetData.color)
        remoteViews.setImageViewResource(R.id.widgetImage, widgetData.picture)
        return remoteViews
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    fun createPresetUI(
        context: Context,
        appWidgetIds: IntArray,
        title:String,
        colorString:String,
        time:Calendar,
        bitmap:Bitmap,
        srcName:String
    ): RemoteViews {
        val remoteViews = RemoteViews(context.packageName, R.layout.widget_haruhi)
        var days = getDays(time) //
        println(days.toString() + "일 측정")
        when {
            days < 0 -> {
                println("아직 남았음")
                days = abs(days)
                remoteViews.setTextViewText(R.id.daysText, days.toString())
                remoteViews.setTextViewText(R.id.sinceWhen, title)
            }
            days == 0L -> {
                remoteViews.setTextViewText(R.id.daysText, context.getString(R.string.todaydesu))
                remoteViews.setTextViewText(R.id.days2, " ")
            }
            else -> {
                remoteViews.setTextViewText(R.id.daysText, days.toString())
                remoteViews.setTextViewText(R.id.sinceWhen, context.getString(R.string.sincelast))
                remoteViews.setTextViewText(R.id.days2, context.getString(R.string.days))
            }
        }
        remoteViews.setTextColor(R.id.daysText, Color.parseColor(colorString)) //
        remoteViews.setTextColor(R.id.days2, Color.parseColor(colorString))
        remoteViews.setTextColor(R.id.sinceWhen, Color.parseColor(colorString))
        remoteViews.setImageViewBitmap(R.id.widgetImage, bitmap)
        val intent = Intent(
            context,
            HaruhiWidgetProvider::class.java
        )
        intent.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds)
        intent.putExtra(SRC_WIDGET, srcName)
        val pi = PendingIntent.getBroadcast(context, 0, intent, 0)
        remoteViews.setOnClickPendingIntent(R.id.widgetImage, pi)
        return remoteViews
    }

    private fun getDays(target : Calendar): Long {
        val today = Calendar.getInstance() //TimeZone.getTimeZone("GMT"));
        println("Target Cal: " + target[Calendar.YEAR] + "Y / " + (target[Calendar.MONTH] + 1) + "M / " + target[Calendar.DATE] + " D")
        println("Today Cal: " + today[Calendar.YEAR] + "Y / " + (today[Calendar.MONTH] + 1) + "M / " + today[Calendar.DATE] + " D")
        if (today[Calendar.YEAR] == target[Calendar.YEAR]
            && today[Calendar.MONTH] == target[Calendar.YEAR]
            && today[Calendar.DATE] == target[Calendar.YEAR]) {
            return 0
        }
        return (today.timeInMillis - target.timeInMillis) / DAY_IN_MILLI
    }
    fun getCalendar(y: Int, m: Int, d: Int):Calendar{
        val calendar = Calendar.getInstance() //TimeZone.getTimeZone("GMT") );
        calendar.set(y,m,d, 0, 0)
        return calendar
    }
    private const val DAY_IN_MILLI =  (1000 * 60 * 60 * 24)
    const val SRC_WIDGET =  "SourceWidget"
}

