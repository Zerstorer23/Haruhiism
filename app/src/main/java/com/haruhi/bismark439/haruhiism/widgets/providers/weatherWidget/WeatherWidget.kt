@file:OptIn(DelicateCoroutinesApi::class)

package com.haruhi.bismark439.haruhiism.widgets.providers.weatherWidget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.haruhi.bismark439.haruhiism.Debugger
import com.haruhi.bismark439.haruhiism.R
import com.haruhi.bismark439.haruhiism.system.weather.WeatherManager
import com.haruhi.bismark439.haruhiism.model.WeatherResponse
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
        // There may be multiple widgets active, so update all of them
        loadWeather(context, appWidgetManager, appWidgetIds)
    }

    private fun loadWeather(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        LocationManager.requestLocationData(context) {
            for (appWidgetId in appWidgetIds) {
                val views = createUI(context, it, appWidgetId)
                appWidgetManager.updateAppWidget(appWidgetId, views)
            }
        }
    }

    private fun createUI(context: Context, weather: WeatherResponse, widgetId: Int): RemoteViews {
        val views = RemoteViews(context.packageName, R.layout.weather_widget)
        Debugger.log(weather.toString())
        val tempStr = WeatherManager.getTemperature(context, weather.main.temp)
        Debugger.log("Temperature $tempStr")
        views.setImageViewResource(
            R.id.ivWeatherIconCharacter,
            WeatherManager.getCharacterByTemperature(weather.main.temp)
        )
        views.setTextViewText(R.id.tvTemperature, tempStr)
        views.setTextViewText(R.id.tvTemperatureUnit, WeatherManager.getUnit(context))
        views.setTextViewText(R.id.tvLocation, weather.name)
        val time = Helper.getTimeString(context, System.currentTimeMillis())
        views.setTextViewText(R.id.tvUpdateTime, time)
        if (weather.weather.isNotEmpty()) {
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
/*
        binding.tvMain.text = weather.main
        binding.tvMainDescription.text = weather.description
        binding.tvTemp.text = "${response.main.temp}${MyApp.getUnit(this)}"
        binding.tvCountry.text = response.sys.country
        binding.tvMax.text = "${response.main.temp_max}${MyApp.getUnit(this)} ↑"
        binding.tvMin.text = "${response.main.temp_min}${MyApp.getUnit(this)} ↓"
        binding.tvSunriseTime.text = MyApp.getTime(response.sys.sunrise)
        binding.tvSunsetTime.text = MyApp.getTime(response.sys.sunset)
        val kmh = response.wind.speed * 1.609
        binding.tvSpeed.text = "${kmh.roundToInt()} km/h"
        binding.tvSpeedUnit.text = response.wind.degree.toString() + "º"
        binding.tvName.text = response.name
        binding.tvHumidity.text = response.main.humidity.toString() + "%"
        binding.ivMain.setImageResource(MyApp.getIconById(weather.icon))
*/
        return views
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)
        if (intent == null || context == null) return
        Debugger.log("Received $intent")
        val id = intent.getIntExtra(WidgetCreater.THIS_WIDGET_ID, -1)
        if (id == -1) return
        LocationManager.requestLocationData(context) {
            val views = createUI(context, it, id)
            AppWidgetManager.getInstance(context).updateAppWidget(id, views)
        }
    }
}