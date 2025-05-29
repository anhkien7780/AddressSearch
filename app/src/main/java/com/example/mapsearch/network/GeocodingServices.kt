package com.example.mapsearch.network

import com.example.mapsearch.BuildConfig
import com.example.mapsearch.module.AutoSuggestResponse
import com.squareup.moshi.Moshi
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private const val BASE_URL = "https://autosuggest.search.hereapi.com/v1/"

private val moshi = Moshi.Builder().build()

private val retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .build()


interface GeocodingApiService {
    @GET("autosuggest")
    suspend fun getSuggestions(
        @Query("q") query: String,
        @Query("at") at: String = "21.0227346,105.7957638",
        @Query("limit") limit: Int = 5,
        @Query("lang") lang: String = "vi",
        @Query("apiKey") apiKey: String = BuildConfig.HERE_API_KEY
    ): AutoSuggestResponse
}

object GeocodingApi {
    val retrofitService: GeocodingApiService by lazy {
        retrofit.create(GeocodingApiService::class.java)
    }
}