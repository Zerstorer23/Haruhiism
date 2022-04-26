package com.haruhi.bismark439.haruhiism.widgets.providers

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import com.haruhi.bismark439.haruhiism.Debugger
import com.haruhi.bismark439.haruhiism.model.widgetDB.WidgetDB
import com.haruhi.bismark439.haruhiism.model.widgetDB.WidgetDB.Companion.loadWidgets
import com.haruhi.bismark439.haruhiism.model.widgetDB.WidgetDao
import com.haruhi.bismark439.haruhiism.widgets.providers.WidgetCreater.onCounterClicked
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/*

*
 * Created by Bismark439 on 28/02/2018.

*/

@DelicateCoroutinesApi
class DayCounterProvider : AppWidgetProvider() {
    companion object {
        const val ACTION_TOUCH = "com.haruhi.bismark439.haruhiism.action.TOUCH_WIDGET"
    }

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        loadWidgets(context) {
            updateUI(context, appWidgetManager, appWidgetIds)
        }
    }

    private fun updateUI(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
       // Debugger.log("Widget DB: " + WidgetDB.getSize() + " installed: " + appWidgetIds.size)
        for (widgetId in appWidgetIds) {
            val widgetData = WidgetDB.get(widgetId) ?: continue
            val ui = WidgetCreater.createFromWidgetData(context, widgetData)
            appWidgetManager.updateAppWidget(widgetId, ui)
        }
    }

    private fun updateUI(
        context: Context,
        appWidgetId: Int
    ) {
        WidgetDao.initDao(context)
        GlobalScope.launch {
            val widgetData = WidgetDao.instance.selectOnce(appWidgetId) ?: return@launch
            Debugger.log("Update on $appWidgetId called")
            val ui = WidgetCreater.createFromWidgetData(context, widgetData)
            AppWidgetManager.getInstance(context).updateAppWidget(appWidgetId, ui)
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
            WidgetDB.deleteWidget(context, id)
        }
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        onCounterClicked(context, intent)
        WidgetCreater.incrementView(context)
        val id = intent.getIntExtra(WidgetCreater.THIS_WIDGET_ID, -1)
        if (id != -1) {
            updateUI(context,id)
        }
    }
}


