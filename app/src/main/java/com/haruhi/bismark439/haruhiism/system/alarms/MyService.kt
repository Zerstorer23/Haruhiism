package com.haruhi.bismark439.haruhiism.system.alarms

import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.haruhi.bismark439.haruhiism.R
import com.haruhi.bismark439.haruhiism.activities.MainNavigatorActivity
import com.haruhi.bismark439.haruhiism.model.alarmDB.AlarmDB
import com.haruhi.bismark439.haruhiism.model.alarmDB.AlarmDao
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlin.concurrent.thread
import android.app.NotificationChannel as NotificationChannel1

@DelicateCoroutinesApi
class MyService : Service() {
    companion object {
        const val NOTIFICATION_ID = 10
        const val CHANNEL_ID = "my_haruhiism_notification_channel"
    }

    override fun onCreate() {
        super.onCreate()
        Log.d("HARUHI", "MyService is started")
        startNotification()
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
        val msg = "알람 설정...${AlarmDB.alarmDB.size} "
/*        Handler(Looper.getMainLooper()).post {
             Toast.makeText(
                 this,
                 msg,
                 Toast.LENGTH_SHORT
             ).show()
        }*/
        Log.d("HARUHI", "하루히 $msg")
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
            AlarmDao.instance.selectAll().collect {
                AlarmDB.alarmDB = ArrayList(it)
                onAlarmsLoaded(context)
                //if (AlarmDB.alarmDB.size == 0) return@collect
            }
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}