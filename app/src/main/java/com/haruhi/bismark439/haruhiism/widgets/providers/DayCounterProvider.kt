package com.haruhi.bismark439.haruhiism.widgets.providers

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import com.haruhi.bismark439.haruhiism.DEBUG
import com.haruhi.bismark439.haruhiism.model.widgetDB.WidgetCharacter
import com.haruhi.bismark439.haruhiism.model.widgetDB.WidgetDB
import com.haruhi.bismark439.haruhiism.model.widgetDB.WidgetDB.Companion.loadWidgets
import com.haruhi.bismark439.haruhiism.model.widgetDB.toCharacterFolder
import com.haruhi.bismark439.haruhiism.widgets.providers.WidgetCreater.onCounterClicked
import kotlinx.coroutines.DelicateCoroutinesApi

/*

*
 * Created by Bismark439 on 28/02/2018.

*/

@DelicateCoroutinesApi
class DayCounterProvider : AppWidgetProvider() {
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        loadWidgets(context) { updateUI(context, appWidgetIds) }
    }

    private fun updateUI(
        context: Context,
        appWidgetIds: IntArray
    ) {
        DEBUG.appendLog("Widget DB: " + WidgetDB.getSize() + " installed: " + appWidgetIds.size)
        for (widgetId in appWidgetIds) {
            DEBUG.appendLog("Widget ID: $widgetId")
            val widgetData = WidgetDB.get(widgetId) ?: continue
            DEBUG.appendLog("Found: $widgetId $widgetData")
            val ui = WidgetCreater.createUI(context, widgetData)
            val intent = Intent(context, DayCounterProvider::class.java)
            intent.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds)
            intent.putExtra(WidgetCreater.SRC_WIDGET, widgetData.widgetCharacter.toCharacterFolder())
            AppWidgetManager.getInstance(context).updateAppWidget(widgetId, ui)
        }
    }


    override fun onDeleted(context: Context, appWidgetIds: IntArray) {
        super.onDeleted(context, appWidgetIds)
        loadWidgets(context) { removeMissingWidgets(context, appWidgetIds) }
    }

    private fun removeMissingWidgets(
        context: Context,
        appWidgetIds: IntArray
    ) {
        for (id in appWidgetIds) {
            val widgetData = WidgetDB.get(id)
            if (widgetData != null) {
                WidgetDB.deleteWidget(context, widgetData)
            }
        }
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        onCounterClicked(context, intent)
    }
}


