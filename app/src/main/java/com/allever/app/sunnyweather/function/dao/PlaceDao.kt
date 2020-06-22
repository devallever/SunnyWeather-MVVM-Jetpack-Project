package com.allever.app.sunnyweather.function.dao

import android.content.Context
import androidx.core.content.edit
import com.allever.app.sunnyweather.function.model.Place
import com.allever.lib.common.app.App
import com.google.gson.Gson

object PlaceDao {

    private const val SP_KEY_PLACE = "place"

    fun savePlace(place: Place) {
        sharePreference().edit {
            putString(SP_KEY_PLACE, Gson().toJson(place))
        }
    }

    fun getSavedPlace(): Place {
        val placeJson = sharePreference().getString(SP_KEY_PLACE, "")
        return Gson().fromJson(placeJson, Place::class.java)
    }

    fun isPlaceSaved(): Boolean = sharePreference().contains(SP_KEY_PLACE)

    private fun sharePreference() =
        App.context.getSharedPreferences("sunny_weather", Context.MODE_PRIVATE)

}