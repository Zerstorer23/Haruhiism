package com.haruhi.bismark439.haruhiism.system.weather

import com.haruhi.bismark439.haruhiism.model.weatherDB.WeatherResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {
    @GET("2.5/weather")
    fun getWeather(
        @Query("lat")
        lat: Double,
        @Query("lon")
        lon: Double,
        @Query("units")
        units: String?,
        @Query("appid")
        appid: String
    ) : Call<WeatherResponse>

}