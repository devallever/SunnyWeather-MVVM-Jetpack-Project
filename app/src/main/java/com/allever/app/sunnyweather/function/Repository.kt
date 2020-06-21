package com.allever.app.sunnyweather.function

import androidx.lifecycle.liveData
import com.allever.app.sunnyweather.function.model.Place
import com.allever.app.sunnyweather.function.network.NetworkManager
import kotlinx.coroutines.Dispatchers
import java.lang.RuntimeException

object Repository {

    //liveData()函数自动构建并返回一个LiveData对象，然后在其代码块中提供挂起函数的上下文，这样可以在代码块中调用挂起函数
    //Dispatchers.IO指定代码块在子线程运行
    fun searchPlaces(query: String) = liveData(Dispatchers.IO) {
        val result = try {
            val placeResponse = NetworkManager.searchPlaces(query)
            if (placeResponse.status == "ok") {
                val places = placeResponse.places
                Result.success<List<Place>>(places)
            } else {
                Result.failure<List<Place>>(RuntimeException("response status is ${placeResponse.status}"))
            }
        } catch (e: Exception) {
            Result.failure<List<Place>>(e)
        }

        //emit()类似liveData的setValue()通知数据变化
        emit(result)
    }

}