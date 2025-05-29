package com.example.mapsearch.module

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Position(
    val lat: Double,
    val lng: Double
)
