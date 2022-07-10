package com.haruhi.bismark439.haruhiism.system

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationManager
import android.os.Looper
import android.provider.Settings
import android.util.Log
import com.haruhi.bismark439.haruhiism.system.weather.WeatherService
import com.google.android.gms.location.*
import com.google.gson.Gson
import com.haruhi.bismark439.haruhiism.Debugger
import com.haruhi.bismark439.haruhiism.model.weatherDB.WeatherResponse
import com.haruhi.bismark439.haruhiism.system.firebase_manager.BaseReturn
import com.haruhi.bismark439.haruhiism.system.ui.Toaster
import com.haruhi.bismark439.haruhiism.widgets.providers.weatherWidget.WeatherWidget
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.URL

object LocationManager {
    const val API_KEY: String = "1cfefaf08b0b70f22b04a90ce82cbdcc"
    const val BASE_URL: String = "https://api.openweathermap.org/data/"
    const val BASE_URL_REST: String =
        " https://api.openweathermap.org/data/2.5/weather"//?lat={lat}&lon={lon}&appid={API key}"
    private const val METRIC_UNIT: String = "metric"


    fun checkLocationPermission(context: Context): Boolean {
        return if (!isLocationEnabled(context)) {
            Toaster.show(context, "Location is off")
            val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            context.startActivity(intent)
            false
        } else {
            true
        }
    }


    private fun isLocationEnabled(context: Context): Boolean {
        val manager: LocationManager =
            context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return manager.isProviderEnabled(LocationManager.GPS_PROVIDER) || manager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    @Suppress("DEPRECATION")
    @SuppressLint("MissingPermission")
    fun requestLocationData(context: Context, onResult: BaseReturn<WeatherResponse>) {
        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                val lastLocation: Location = result.lastLocation
                val latitude = lastLocation.latitude
                val longitude = lastLocation.longitude
                Debugger.log("Location $latitude / $longitude")
                getLocationWeatherDetailsRest(context, latitude, longitude, onResult)
            }
        }

        val locationRequest = LocationRequest()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        val fusedLocationClient: FusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(context)
        fusedLocationClient.requestLocationUpdates(
            locationRequest, locationCallback,
            Looper.myLooper()!!
        )
    }

    private fun getLocationWeatherDetails(
        context: Context,
        lat: Double,
        lon: Double,
        onResult: BaseReturn<WeatherResponse>
    ) {
        if (!NetworkManager.hasInternet(context)) return
        val callback = object : Callback<WeatherResponse> {
            override fun onResponse(
                call: Call<WeatherResponse>,
                response: Response<WeatherResponse>
            ) {
                if (response.isSuccessful) {
                    val weather: WeatherResponse? = response.body()
                    if (weather != null) {
                        Debugger.log("Response: $weather")
                        onResult(weather)
                    }
                } else {
                    when (response.code()) {
                        400 -> {
                            Debugger.log("Error: Bad Connection")
                        }
                        404 -> {
                            Debugger.log("Error: Not found")
                        }
                        else -> {
                            Debugger.log("Error")
                        }
                    }
                }
            }

            override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                Log.e("Error", t.message.toString())

            }

        }
        Debugger.log("URL $BASE_URL")
        Debugger.log("API KEY $API_KEY")
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            //val service: WeatherService = retrofit
            .create(WeatherService::class.java)
            //val listCall: Call<WeatherResponse> = service
            .getWeather(
                lat, lon, METRIC_UNIT, API_KEY
            ).enqueue(callback)
    }

    private fun getLocationWeatherDetailsRest(
        context: Context,
        lat: Double,
        lon: Double,
        onResult: BaseReturn<WeatherResponse>
    ) {
        if (!NetworkManager.hasInternet(context)) {
            val weather = loadWeather(context) ?: return
            onResult(weather)
            return
        }
        GlobalScope.launch {
            Debugger.runSafe {
                val url =
                    "${BASE_URL_REST}?lat=${lat}&lon=${lon}&appid=${API_KEY}&units=${METRIC_UNIT}"
                Debugger.log("rest URL: $url")
                val apiResponse =
                    URL(url).readText().trim()
                Debugger.log("Text: $apiResponse")
                var weather = Gson().fromJson(apiResponse, WeatherResponse::class.java)
                if (weather == null) {
                    weather = loadWeather(context)
                }
                if (weather != null) {
                    saveWeather(context, weather)
                    Debugger.log("Response: $weather")
                    onResult(weather)
                }
            }
        }

    }

    fun saveWeather(context: Context, response: WeatherResponse) {
        val weatherJson = Gson().toJson(response)
        val writer = StorageManager.getPrefWriter(context)
        writer.putString(WeatherWidget.WeatherCache, weatherJson)
        println("Write $weatherJson")
        writer.apply()
    }

    fun loadWeather(context: Context): WeatherResponse? {
        val reader = StorageManager.getPrefReader(context)
        val jsonString = reader.getString(WeatherWidget.WeatherCache, "")
        return if (!jsonString.isNullOrEmpty()) {
            Gson().fromJson(jsonString, WeatherResponse::class.java)
        } else {
            null
        }
    }
}
