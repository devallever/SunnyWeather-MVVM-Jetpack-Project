package com.allever.app.sunnyweather.function.network

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

object NetworkManager {

    private val mPlaceService = ServiceCreator.create<PlaceService>()

    suspend fun searchPlaces(query: String) = mPlaceService.searchPlaces(query).result()

    private val mWeatherService = ServiceCreator.create<WeatherService>()

    suspend fun getRealtimeWeather(lng: String, lat: String) =
        mWeatherService.getRealtimeWeather(lng, lat).result()

    suspend fun getDailyWeather(lng: String, lat: String) =
        mWeatherService.getDailyWeather(lng, lat).result()

    private suspend fun <T> Call<T>.result(): T {
        return suspendCoroutine { continuation ->
            enqueue(object : Callback<T> {
                override fun onResponse(call: Call<T>, response: Response<T>) {
                    val body = response.body()
                    if (body != null) {
                        continuation.resume(body)
                    } else {
                        continuation.resumeWithException(RuntimeException("response body is null"))
                    }
                }

                override fun onFailure(call: Call<T>, t: Throwable) {
                    continuation.resumeWithException(t)
                }
            })
        }
    }

}