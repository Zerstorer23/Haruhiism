package com.haruhi.bismark439.haruhiism.widgets.providers

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import com.haruhi.bismark439.haruhiism.widgets.providers.WidgetCreater.createPresetUI
import com.haruhi.bismark439.haruhiism.widgets.providers.WidgetCreater.onCounterClicked
import java.util.*

open class IWidgetProvider(
    private val calendar: Calendar,
    private val titleStringId: Int,
    private val drawableId: Int,
    private val colorString: String,
    private val srcFolder: String
) : AppWidgetProvider() {
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        val title = context.getString(titleStringId)
        val bitmap = BitmapFactory.decodeResource(context.resources, drawableId)// R.drawable.haruhi1)
        val intent = Intent(context, IWidgetProvider::class.java)
        val ui = createPresetUI(context, appWidgetIds, title, colorString, calendar, bitmap, srcFolder, intent)
        for (widgetId in appWidgetIds) {
            appWidgetManager.updateAppWidget(widgetId, ui)
        }
    }


    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        onCounterClicked(context, intent)
    }


}