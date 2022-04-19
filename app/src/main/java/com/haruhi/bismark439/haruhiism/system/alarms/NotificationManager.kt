package com.haruhi.bismark439.haruhiism.system.alarms

import android.annotation.SuppressLint
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import com.haruhi.bismark439.haruhiism.R
import com.haruhi.bismark439.haruhiism.system.Constants.NOTIFICATION_CHANNEL_ID

object NotificationManager {
    private const val ALARM_NOTIFICATION_CHANNEL_NUMBER = 1


    fun sendNotification(context: Context) {//sendClass: Class<*>?
        val mNotifyMgr = context.getSystemService(AppCompatActivity.NOTIFICATION_SERVICE) as NotificationManager
        val contentIntent = PendingIntent.getActivity(
            context,
            0,
            Intent(context, context::class.java).setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        // Build Notification , setOngoing keeps the notification always in status bar
        val notificationBuilder = NotificationCompat.Builder(
            context, NOTIFICATION_CHANNEL_ID
        ).setSmallIcon(R.drawable.haruhi_3)
            .setContentTitle(context.getString(R.string.haruhialarm))
            .setContentText(context.getString(R.string.clicktodisarm))
            .setOngoing(true)
            .setContentIntent(contentIntent)

        val notification = notificationBuilder.build()
        // Gets an instance of the NotificationManager service

        // Builds the notification and issues it.
        mNotifyMgr.notify(ALARM_NOTIFICATION_CHANNEL_NUMBER, notification)
    }
/*
    fun releaseNotification(context: Context) {
        val mNotifyMgr = context.getSystemService(AppCompatActivity.NOTIFICATION_SERVICE) as NotificationManager
        // Builds the notification and issues it.
        mNotifyMgr.cancel(ALARM_NOTIFICATION_CHANNEL_NUMBER)
    }
*/
}