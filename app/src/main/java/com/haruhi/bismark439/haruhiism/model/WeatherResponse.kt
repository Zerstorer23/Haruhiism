package com.haruhi.bismark439.haruhiism.model

import java.io.Serializable

data class WeatherResponse(
    val coord: Coord,
    val weather: List<Weather>,
    val base: String,
    val main: WeatherMain,
    val wind: WeatherWind,
    val cloud: WeatherCloud,
    val dt: Int,
    val sys: WeatherSys,
    val id: Int,
    val name: String,
    val cod: Int,
) : Serializable

data class Coord(
    val lon: Double,
    val lat: Double,
) : Serializable

data class Weather(
    val id: Int,
    val main: String,
    val description: String,
    val icon: String
) : Serializable

data class WeatherMain(
    val temp: Double,
    val pressure: Double,
    val humidity: Double,
    val temp_min: Double,
    val temp_max: Double,
    val sea_level: Double,
    val grnd_level: Double
) : Serializable

data class WeatherWind(
    val speed: Double,
    val degree: Int
) : Serializable

data class WeatherCloud(
    val all: Int
) : Serializable

data class WeatherSys(
    val type: Int,
    val message: Double,
    val country: String,
    val sunrise: Long,
    val sunset: Long,
) : Serializable
