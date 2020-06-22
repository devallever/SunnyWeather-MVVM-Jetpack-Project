package com.allever.app.sunnyweather.function

import androidx.lifecycle.liveData
import com.allever.app.sunnyweather.function.dao.PlaceDao
import com.allever.app.sunnyweather.function.model.Place
import com.allever.app.sunnyweather.function.model.Weather
import com.allever.app.sunnyweather.function.network.NetworkManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlin.coroutines.CoroutineContext

object Repository {

    private const val STATUS_OK = "ok"

    //liveData()函数自动构建并返回一个LiveData对象，然后在其代码块中提供挂起函数的上下文，这样可以在代码块中调用挂起函数
    //Dispatchers.IO指定代码块在子线程运行
    fun searchPlaces(query: String) = fire(Dispatchers.IO) {
        val placeResponse = NetworkManager.searchPlaces(query)
        if (placeResponse.status == STATUS_OK) {
            val places = placeResponse.places
            Result.success<List<Place>>(places)
        } else {
            Result.failure<List<Place>>(RuntimeException("response status is ${placeResponse.status}"))
        }
    }

    fun fetchWeather(lng: String, lat: String) = fire(Dispatchers.IO) {
        // 4.使用coroutineScope()创建子作用域(非子协程)
        // coroutineScope()是一个挂起函数，继承外部协程作用域，并创建一个子作用域
        // coroutineScope()外部必须是协程作用域
        // coroutineScope()使作用域内所有代码和子协程全部执行完之前阻塞当前协程
        // coroutineScope()只能在协程作用域或挂起函数中使用
        coroutineScope {
            //多个协程并发执行
            val deferredRealtime = async {
                NetworkManager.getRealtimeWeather(lng, lat)
            }
            val deferredDaily = async {
                NetworkManager.getDailyWeather(lng, lat)
            }

            val realtimeResponse = deferredRealtime.await()
            val dailyResponse = deferredDaily.await()

            if (realtimeResponse.status == STATUS_OK && dailyResponse.status == STATUS_OK) {
                val weather =
                    Weather(realtimeResponse.result.realtime, dailyResponse.result.daily)
                Result.success(weather)
            } else {
                Result.failure<Weather>(
                    RuntimeException(
                        "realtime response status is ${realtimeResponse.status}" +
                                " daily response status is ${dailyResponse.status}"
                    )
                )
            }
        }
    }

    fun savePlace(place: Place) = PlaceDao.savePlace(place)

    fun getSavedPlace(): Place = PlaceDao.getSavedPlace()

    fun isPlaceSaved(): Boolean = PlaceDao.isPlaceSaved()

    private fun <T> fire(context: CoroutineContext, block: suspend () -> Result<T>) =
        liveData<Result<T>>(context) {
            val result = try {
                block()
            } catch (e: Exception) {
                Result.failure<T>(e)
            }
            //emit()类似liveData的setValue()通知数据变化
            emit(result)
        }

}