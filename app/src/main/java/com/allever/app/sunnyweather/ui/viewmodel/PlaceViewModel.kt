package com.allever.app.sunnyweather.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.allever.app.sunnyweather.function.Repository
import com.allever.app.sunnyweather.function.model.Place

class PlaceViewModel : ViewModel() {

    private val searchLiveData = MutableLiveData<String>()

    val placeList = ArrayList<Place>()

    val placeLiveData = Transformations.switchMap(searchLiveData) { place ->
        Repository.searchPlaces(place)
    }

    //调用此方法，switchMAP的转换函数会调用，然后执行网络请求
    fun searchPlaces(query: String) {
        searchLiveData.value = query
    }

    fun savePlace(place: Place) = Repository.savePlace(place)

    fun getSavedPlace(): Place = Repository.getSavedPlace()

    fun isPlaceSaved(): Boolean = Repository.isPlaceSaved()

}