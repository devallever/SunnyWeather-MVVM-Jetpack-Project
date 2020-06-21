package com.allever.app.sunnyweather.function.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ServiceCreator {

    private const val BASE_URL = "https://api.caiyunapp.com/"

    private val mRetrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun <T> create(serviceClass: Class<T>): T = mRetrofit.create(serviceClass)

    //泛型实化：使用inline 和 reified
    inline fun <reified T> create(): T = create(T::class.java)

}