package com.allever.app.sunnyweather.function.model

data class Weather(
    val realtime: RealtimeResponse.Realtime,
    val daily: DailyResponse.Daily
)