package com.haruhi.bismark439.haruhiism.system.weather

import android.content.Context
import com.google.gson.Gson
import com.haruhi.bismark439.haruhiism.R
import com.haruhi.bismark439.haruhiism.model.WeatherResponse
import com.haruhi.bismark439.haruhiism.system.StorageManager
import java.text.SimpleDateFormat
import java.util.*

object WeatherManager {
    private const val WEATHER_DATA = "weather_response_data"

    fun getTemperature(context: Context, temp: Double): String {
        val isF = isFarenheit(context)
        var t = temp
        if (isF) {
            t = t * 9 / 5 + 32
        }
        return String.format("%.1f", t)
    }

    private fun isFarenheit(context: Context): Boolean {
        val locale =
            (context.resources.configuration.locales[0].country.toString())
        return when {
            (locale) == "US" ||
                    (locale) == "LR" ||
                    (locale) == "MM" -> {
                true
            }
            else -> {
                false
            }
        }
    }

    fun getUnit(context: Context): String {
        val locale =
            (context.resources.configuration.locales[0].country.toString())
        return when {
            (locale) == "US" ||
                    (locale) == "LR" ||
                    (locale) == "MM" -> {
                "ºF"
            }
            else -> {
                "ºC"
            }
        }
    }



    fun getIconById(id: String): Int {
        return when (id) {
            "01d" -> R.drawable.sunny
            "02d" -> R.drawable.cloud
            "03d" -> R.drawable.cloud
            "04d" -> R.drawable.cloud
            "04n" -> R.drawable.cloud
            "10d" -> R.drawable.rain
            "11d" -> R.drawable.storm
            "13d" -> R.drawable.snowflake
            "01n" -> R.drawable.cloud
            "02n" -> R.drawable.cloud
            "03n" -> R.drawable.cloud
            "10n" -> R.drawable.cloud
            "11n" -> R.drawable.rain
            "13n" -> R.drawable.snowflake
            else -> R.drawable.sunny
        }
    }

    private fun saveWeather(context: Context, response: WeatherResponse) {
        val weatherJson = Gson().toJson(response)
        val editor = StorageManager.getPrefWriter(context)
        editor.putString(WEATHER_DATA, weatherJson)
        editor.apply()

    }

    private fun loadWeather(context: Context): WeatherResponse? {
        val jsonString = StorageManager.getPrefReader(context).getString(WEATHER_DATA, "")
        return if (!jsonString.isNullOrEmpty()) {
            Gson().fromJson(jsonString, WeatherResponse::class.java)
        } else {
            null
        }
    }

    fun getCharacterByTemperature(temp: Double): Int {
        return when {
            temp <= 0 -> {
                R.drawable.asakura_frozen
            }
            temp <= 10 ->{
                R.drawable.asakura_cold
            }
            temp <= 20 ->{
                R.drawable.asakura_normal
            }
            else ->{
                R.drawable.asakura_hot
            }

        }
    }

}