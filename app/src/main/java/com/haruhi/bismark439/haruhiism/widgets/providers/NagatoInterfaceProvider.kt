package com.haruhi.bismark439.haruhiism.widgets.providers

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.haruhi.bismark439.haruhiism.R

/**
 * Created by Bismark439 on 28/02/2018.
 */
class NagatoInterfaceProvider : AppWidgetProvider() {
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        val count = appWidgetIds.size
        println("NAGATO $count")
        for (i in 0 until count) {
            val widgetId = appWidgetIds[i]
            println("NAGATO ID$widgetId")
            val remoteViews = RemoteViews(context.packageName, R.layout.widget_nagato)
            val intent = Intent(context, NagatoInterfaceProvider::class.java)
            intent.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds)
            appWidgetManager.updateAppWidget(widgetId, remoteViews)
        }
    }
}