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
import com.haruhi.bismark439.haruhiism.model.WeatherResponse
import com.haruhi.bismark439.haruhiism.system.firebase_manager.BaseReturn
import com.haruhi.bismark439.haruhiism.system.ui.Toaster
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object LocationManager {
    private const val API_KEY: String = "1cfefaf08b0b70f22b04a90ce82cbdcc"
    private const val BASE_URL: String = "https://api.openweathermap.org/data/"
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
                Log.d("Location ", "$latitude $longitude")
                getLocationWeatherDetails(context, latitude, longitude, onResult)
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
                        onResult(weather)
                    }
                    Log.d("Response", "$weather")
                } else {
                    when (response.code()) {
                        400 -> {
                            Log.e("Error", "Bad Connection")
                        }
                        404 -> {
                            Log.e("Error", "Not found")
                        }
                        else -> {
                            Log.e("Error", "Error")
                        }
                    }
                }
            }

            override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                Log.e("Error", t.message.toString())

            }

        }

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
}
