package com.allever.app.sunnyweather.function.network

import com.allever.app.sunnyweather.app.MyApp
import com.allever.app.sunnyweather.function.model.PlaceResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface PlaceService {

    @GET("v2/place?token=${MyApp.TOKEN}&lang=zh_CN")
    fun searchPlaces(@Query("query") query: String): Call<PlaceResponse>

}