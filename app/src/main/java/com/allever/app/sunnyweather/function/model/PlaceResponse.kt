package com.allever.app.sunnyweather.function.model

import com.google.gson.annotations.SerializedName

data class PlaceResponse(val status: String, val place: List<Place>)

data class Place(
    val name: String,
    val location: Location,
    @SerializedName("formatted_address") val address: String
)

data class Location(val lng: String, val lat: String)