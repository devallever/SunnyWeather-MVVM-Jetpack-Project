package com.allever.app.sunnyweather.function.network

import com.allever.app.sunnyweather.app.MyApp
import com.allever.app.sunnyweather.function.model.DailyResponse
import com.allever.app.sunnyweather.function.model.RealtimeResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface WeatherService {

    @GET("V2.5/${MyApp.TOKEN}/{lng},{lat}/realtime.json")
    fun getRealtimeWeather(
        @Path("lng") lng: String,
        @Path("lat") lat: String
    ): Call<RealtimeResponse>

    @GET("V2.5/${MyApp.TOKEN}/{lng},{lat}/daily.json")
    fun getDailyWeather(
        @Path("lng") lng: String,
        @Path("lat") lat: String
    ): Call<DailyResponse>

}