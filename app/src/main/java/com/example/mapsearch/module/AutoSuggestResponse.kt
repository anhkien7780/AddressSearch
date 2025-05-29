package com.example.mapsearch.module

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AutoSuggestResponse(
    val items: List<PlaceItem>
)
