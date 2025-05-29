package com.example.mapsearch.module

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PlaceItem(
    val title: String,
    val address: Address?,
    val position: Position?
)
