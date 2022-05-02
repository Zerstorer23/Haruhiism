package com.haruhi.bismark439.haruhiism.model.weatherDB

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class WeatherResponse(
    @SerializedName("coord")
    val coord: Coord,
    @SerializedName("weather")
    val weather: List<Weather>,
    @SerializedName("base")
    val base: String,
    @SerializedName("main")
    val main: WeatherMain,
    @SerializedName("wind")
    val wind: WeatherWind,
    @SerializedName("cloud")
    val cloud: WeatherCloud,
    @SerializedName("dt")
    val dt: Int,
    @SerializedName("sys")
    val sys: WeatherSys,
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("cod")
    val cod: Int,
) : Serializable

data class Coord(
    @SerializedName("lon")
    val lon: Double,
    @SerializedName("lat")
    val lat: Double,
) : Serializable

data class Weather(
    @SerializedName("id")
    val id: Int,
    @SerializedName("main")
    val main: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("icon")
    val icon: String
) : Serializable

data class WeatherMain(
    @SerializedName("temp")
    val temp: Double,
    @SerializedName("feels_like")
    val feels_like:Double,
    @SerializedName("pressure")
    val pressure: Double,
    @SerializedName("humidity")
    val humidity: Double,
    @SerializedName("temp_min")
    val temp_min: Double,
    @SerializedName("temp_max")
    val temp_max: Double,
    @SerializedName("sea_level")
    val sea_level: Double,
    @SerializedName("grnd_level")
    val grnd_level: Double
) : Serializable

data class WeatherWind(
    @SerializedName("speed")
    val speed: Double,
    @SerializedName("degree")
    val degree: Int
) : Serializable

data class WeatherCloud(
    @SerializedName("all")
    val all: Int
) : Serializable

data class WeatherSys(
    @SerializedName("type")
    val type: Int,
    @SerializedName("message")
    val message: Double,
    @SerializedName("country")
    val country: String,
    @SerializedName("sunrise")
    val sunrise: Long,
    @SerializedName("sunset")
    val sunset: Long,
) : Serializable
