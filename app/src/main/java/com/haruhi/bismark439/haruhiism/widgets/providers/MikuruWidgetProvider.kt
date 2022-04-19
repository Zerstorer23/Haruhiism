package com.haruhi.bismark439.haruhiism.widgets.providers

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.graphics.BitmapFactory
import com.haruhi.bismark439.haruhiism.R
import com.haruhi.bismark439.haruhiism.widgets.providers.HaruhiWidgetProvider.Companion.MIKURU_WIDGET

/**
 * Created by Bismark439 on 19/01/2018.
 */
class MikuruWidgetProvider : AppWidgetProvider() {

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {

        val calendar = WidgetCreater.getCalendar(2003, 5, 6)
        val title = context.getString(R.string.sinceMelancholy)
        val bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.mikuruw)
        val ui = WidgetCreater.createPresetUI(context,appWidgetIds,title,"#EEc80000",calendar, bitmap,MIKURU_WIDGET)
        for (widgetId in appWidgetIds) {
            appWidgetManager.updateAppWidget(widgetId, ui)
        }

    }

}