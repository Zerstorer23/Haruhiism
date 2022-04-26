package com.haruhi.bismark439.haruhiism.widgets.providers

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import com.haruhi.bismark439.haruhiism.R
import com.haruhi.bismark439.haruhiism.system.ui.Toaster
import com.haruhi.bismark439.haruhiism.widgets.providers.WidgetCreater.onCounterClicked
import java.util.*

open class IWidgetProvider(
    private val calendar: Calendar,
    private val titleStringId: Int,
    private val drawableId: Int,
    private val colorString: String,
    private val srcFolder: String,
    private val cls: Class<*>,
) : AppWidgetProvider() {
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        val title = context.getString(titleStringId)
        val bitmap =
            BitmapFactory.decodeResource(context.resources, drawableId)// R.drawable.haruhi1)
        val color = Color.parseColor(colorString)
        for (widgetId in appWidgetIds) {
            val view = WidgetCreater.createGeneralUI(context, title, color, calendar)
            WidgetCreater.setOnClickIntent(context, view, cls, srcFolder, widgetId)
            view.setImageViewBitmap(R.id.widgetImage, bitmap)
            appWidgetManager.updateAppWidget(widgetId, view)
        }
    }

    private fun updateUI(context: Context, widgetId: Int) {
        val title = context.getString(titleStringId)
        val bitmap = BitmapFactory.decodeResource(context.resources, drawableId)// R.drawable.haruhi1)
        val color = Color.parseColor(colorString)
        val view = WidgetCreater.createGeneralUI(context, title, color, calendar)
        WidgetCreater.setOnClickIntent(context, view, cls, srcFolder, widgetId)
        view.setImageViewBitmap(R.id.widgetImage, bitmap)
        AppWidgetManager.getInstance(context).updateAppWidget(widgetId, view)
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        onCounterClicked(context, intent)
        WidgetCreater.incrementView(context)
        val id = intent.getIntExtra(WidgetCreater.THIS_WIDGET_ID, -1)
        if (id == -1) return
        updateUI(context, id)
    }
}