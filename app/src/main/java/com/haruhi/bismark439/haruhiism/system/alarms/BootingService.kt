package com.haruhi.bismark439.haruhiism.system.alarms

import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.haruhi.bismark439.haruhiism.R
import com.haruhi.bismark439.haruhiism.activities.MainNavigatorActivity
import com.haruhi.bismark439.haruhiism.activities.navigation_ui.wallpaper_setting.MyWallpaperOption
import com.haruhi.bismark439.haruhiism.model.alarmDB.AlarmDB
import com.haruhi.bismark439.haruhiism.model.alarmDB.AlarmDao
import com.haruhi.bismark439.haruhiism.system.LocationManager
import com.haruhi.bismark439.haruhiism.system.wallpapers.WallpaperBroadcastManager
import com.haruhi.bismark439.haruhiism.widgets.providers.weatherWidget.WeatherWidget
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.concurrent.thread
import android.app.NotificationChannel as NotificationChannel1

//http://awesomeprojectsxyz.blogspot.com/2016/04/android-development-how-to-use-adb.html
//ADB BROADCAST
//am broadcast -a android.intent.action.ACTION_BOOT_COMPLETED -p com.haruhi.bismark439.haruhiism
//adb shell am broadcast -a android.intent.action.ACTION_BOOT_COMPLETED
@DelicateCoroutinesApi
class BootingService : Service() {
    companion object {
        const val NOTIFICATION_ID = 10
        const val CHANNEL_ID = "my_haruhiism_notification_channel"
    }

    override fun onCreate() {
        super.onCreate()
        Log.d("HARUHI", "MyService is started")
        startNotification()
        loadWallpapers(applicationContext)
        loadWidgetWeathers(applicationContext)
        loadAlarms(applicationContext)
    }

    private fun startNotification() {
        thread(start = true) {
            sendNotification(
                resources.getString(R.string.app_name),
                getString(R.string.settting_alarms_msg)
            )
            Thread.sleep(10 * 1000)
            stopNotification()
        }
    }

    private fun stopNotification() {
        val notificationManager = getSystemService(
            Context.NOTIFICATION_SERVICE
        ) as NotificationManager
        notificationManager.cancel(NOTIFICATION_ID)
    }

    @DelicateCoroutinesApi
    private fun onAlarmsLoaded(context: Context) {
        val msg = "?????? ??????...${AlarmDB.alarmDB.size} "
/*        Handler(Looper.getMainLooper()).post {
             Toast.makeText(
                 this,
                 msg,
                 Toast.LENGTH_SHORT
             ).show()
        }*/
        Log.d("HARUHI", "????????? $msg")

        AlarmDB.safeRegisterAllAlarms(context)
        stopNotification()
        stopSelf()
    }

    private fun createNotificationChannel() {
        val notificationChannel = NotificationChannel1(
            CHANNEL_ID,
            resources.getString(R.string.app_name),
            NotificationManager.IMPORTANCE_HIGH
        )
        notificationChannel.enableLights(true)
        notificationChannel.lightColor = Color.RED
        notificationChannel.enableVibration(true)
        notificationChannel.description = "AppApp Tests"

        val notificationManager = applicationContext.getSystemService(
            Context.NOTIFICATION_SERVICE
        ) as NotificationManager
        notificationManager.createNotificationChannel(
            notificationChannel
        )
    }

    private fun sendNotification(title: String, messageBody: String) {
        val intent = Intent(this, MainNavigatorActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        val notificationBuilder = NotificationCompat.Builder(
            this, CHANNEL_ID
        )
            .setSmallIcon(R.drawable.appicoharuhi)
            .setContentText(title)
            .setContentText(messageBody)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)

        createNotificationChannel()


        val notification = notificationBuilder.build()
        startForeground(NOTIFICATION_ID, notification)

//        notificationManager.notify(0, notification)
    }

    @DelicateCoroutinesApi
    private fun loadAlarms(context: Context) {
        AlarmDao.initDao(context)
        GlobalScope.launch {
            val it = AlarmDao.instance.selectAllOnce()
            AlarmDB.alarmDB = ArrayList(it)
            onAlarmsLoaded(context)
        }
    }

    private fun loadWallpapers(context: Context) {
        val option = MyWallpaperOption.loadData(context)
        if (!option.isEnabled) return
        option.readFiles(context)
        println("Load wallpaper")
        WallpaperBroadcastManager.updateWallpaper(context, option)
    }

    private fun loadWidgetWeathers(context: Context) {
        println("Load Weathers")
        val widgetManager = AppWidgetManager.getInstance(context)
        val widgetComponent = ComponentName(context, WeatherWidget::class.java)
        val appWidgetIds = widgetManager.getAppWidgetIds(widgetComponent)
        val weather = LocationManager.loadWeather(context) ?: return
        for (appWidgetId in appWidgetIds) {
            println("Update $appWidgetId")
            val views = WeatherWidget.createUI(context, weather, appWidgetId)
            widgetManager.updateAppWidget(appWidgetId, views)
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}