@file:OptIn(DelicateCoroutinesApi::class)

package com.haruhi.bismark439.haruhiism.widgets.providers

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.widget.RemoteViews
import com.haruhi.bismark439.haruhiism.Debugger
import com.haruhi.bismark439.haruhiism.R
import com.haruhi.bismark439.haruhiism.activities.navigation_ui.click_ranks.RankingFragment.Companion.CLICKS_MINE
import com.haruhi.bismark439.haruhiism.model.widgetDB.WidgetData
import com.haruhi.bismark439.haruhiism.model.widgetDB.toCharacterFolder
import com.haruhi.bismark439.haruhiism.system.StorageManager
import com.haruhi.bismark439.haruhiism.system.firebase_manager.FirebaseHandler
import com.haruhi.bismark439.haruhiism.widgets.providers.DayCounterProvider.Companion.ACTION_TOUCH
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.IOException
import java.util.*
import kotlin.math.abs

object WidgetCreater {
    private const val DAY_IN_MILLI = (1000 * 60 * 60 * 24)
    const val SRC_WIDGET = "SourceWidget"
    const val THIS_WIDGET_ID = "MyAppIdWidget"

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

    private fun playSound(context: Context, folder: String, fileList: Array<String>) {
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


    fun createFromWidgetData(context: Context, widgetData: WidgetData): RemoteViews {
        val calendar = getCalendar(widgetData.yy, widgetData.mmMod, widgetData.dd)
        val srcName = widgetData.widgetCharacter.toCharacterFolder()
        val view = createGeneralUI(context, widgetData.name, widgetData.color, calendar)
        setOnClickIntent(
            context,
            view,
            DayCounterProvider::class.java,
            srcName,
            widgetData.appWidgetId
        )
        view.setImageViewResource(R.id.widgetImage, widgetData.picture)
        Debugger.log(widgetData.toString())
        return view
    }

    fun createGeneralUI(
        context: Context, title: String, color: Int,
        time: Calendar
    ): RemoteViews {
        val remoteViews = RemoteViews(context.packageName, R.layout.widget_default_counter)
        val days = getDays(time)
        setDdayText(remoteViews, context, days, title)
        setColors(remoteViews, color)
        return remoteViews
    }

    fun setOnClickIntent(
        context: Context,
        remoteViews: RemoteViews,
        cls: Class<*>,
        srcName: String,
        widgetId: Int = -1
    ) {
        val intent = Intent(context, cls)
        intent.action = ACTION_TOUCH
        intent.putExtra(SRC_WIDGET, srcName)
        intent.putExtra(THIS_WIDGET_ID, widgetId)
        val pi = PendingIntent.getBroadcast(
            context,
            widgetId,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
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

    fun incrementView(context: Context) {
        GlobalScope.launch(Dispatchers.Default) {
            val reader = StorageManager.getPrefReader(context)
            val clicks = reader.getInt(CLICKS_MINE, 0) + 1
            val writer = StorageManager.getPrefWriter(context)
            writer.putInt(CLICKS_MINE, clicks)
            writer.apply()
            Debugger.log("Clicks : $clicks")
            FirebaseHandler.incrementCounts()
        }
    }


}

