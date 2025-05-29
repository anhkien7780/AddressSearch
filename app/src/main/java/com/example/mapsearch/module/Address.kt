package com.example.mapsearch.module

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Address(
    val label: String
)
