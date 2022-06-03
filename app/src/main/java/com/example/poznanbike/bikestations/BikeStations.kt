package com.example.poznanbike.bikestations

import com.example.poznanbike.bikestations.BikeStation
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class BikeStations(
   @Json(name = "features")
   val items: List<BikeStation>
)