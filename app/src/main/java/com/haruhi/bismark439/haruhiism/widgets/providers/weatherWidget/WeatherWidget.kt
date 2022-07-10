@file:OptIn(DelicateCoroutinesApi::class)

package com.haruhi.bismark439.haruhiism.widgets.providers.weatherWidget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.haruhi.bismark439.haruhiism.Debugger
import com.haruhi.bismark439.haruhiism.R
import com.haruhi.bismark439.haruhiism.system.weather.WeatherManager
import com.haruhi.bismark439.haruhiism.model.weatherDB.WeatherResponse
import com.haruhi.bismark439.haruhiism.system.Helper
import com.haruhi.bismark439.haruhiism.system.LocationManager
import com.haruhi.bismark439.haruhiism.widgets.providers.WidgetCreater
import kotlinx.coroutines.DelicateCoroutinesApi

/**
 * Implementation of App Widget functionality.
 */
class WeatherWidget : AppWidgetProvider() {
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        println("Weather widget update called")
        // There may be multiple widgets active, so update all of them
        loadWeather(context, appWidgetManager, appWidgetIds)
    }


    companion object {
        val WeatherCache = "WeatherCache"
        fun loadWeather(
            context: Context,
            appWidgetManager: AppWidgetManager,
            appWidgetIds: IntArray
        ) {
            val weather = LocationManager.loadWeather(context)
            if (weather != null) {
                for (appWidgetId in appWidgetIds) {
                    appWidgetManager.updateAppWidget(
                        appWidgetId,
                        createUI(context, weather, appWidgetId)
                    )
                }
            }
            LocationManager.requestLocationData(context) {
                for (appWidgetId in appWidgetIds) {
                    println("Update $appWidgetId")
                    val views = createUI(context, it, appWidgetId)
                    appWidgetManager.updateAppWidget(appWidgetId, views)
                }
            }
        }

        fun createUI(context: Context, weather: WeatherResponse?, widgetId: Int): RemoteViews {
            val views = RemoteViews(context.packageName, R.layout.weather_widget)
            try {
                if (weather != null) {
                    Debugger.log(weather.toString())
                }
                val tempStr =
                    weather?.main?.temp?.let { WeatherManager.getTemperature(context, it) }
                if (weather != null) {
                    views.setTextViewText(R.id.tvTemperatureUnit, WeatherManager.getUnit(context))
                }

                Debugger.log("Temperature $tempStr")
                weather?.main?.temp?.let {
                    WeatherManager.getCharacterByTemperature(it)
                }?.let {
                    views.setImageViewResource(
                        R.id.ivWeatherIconCharacter,
                        it
                    )
                }
                views.setTextViewText(R.id.tvTemperature, tempStr)
                views.setTextViewText(R.id.tvLocation, weather?.name)
                val time = Helper.getTimeString(context, System.currentTimeMillis())
                views.setTextViewText(R.id.tvUpdateTime, time)
                if (weather?.weather?.isNotEmpty() == true) {
                    val firstWeather = weather.weather.first()
                    views.setImageViewResource(
                        R.id.ivWeatherIcon,
                        WeatherManager.getIconById(firstWeather.icon)
                    )
                }
                val intent = Intent(context, WeatherWidget::class.java)
                intent.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
                intent.putExtra(WidgetCreater.THIS_WIDGET_ID, widgetId)
                val pi = PendingIntent.getBroadcast(
                    context, widgetId,
                    intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )
                views.setOnClickPendingIntent(R.id.ivWeatherWidget, pi)
                views.setOnClickPendingIntent(R.id.ivRefresh, pi)
            } catch (e: Exception) {
                Debugger.log("UI Exception!")
                Debugger.log(e.stackTraceToString())
            }

            return views
        }

    }


    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)
        Debugger.log("Received $intent")
        if (intent == null || context == null) return
        val id = intent.getIntExtra(WidgetCreater.THIS_WIDGET_ID, -1)
        if (id == -1) return
        LocationManager.requestLocationData(context) {
            val views = createUI(context, it, id)
            AppWidgetManager.getInstance(context).updateAppWidget(id, views)
        }
    }
}