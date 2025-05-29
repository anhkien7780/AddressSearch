package com.example.mapsearch.viewmodel

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import com.example.mapsearch.module.LoadingUIState
import com.example.mapsearch.module.PlaceItem
import com.example.mapsearch.network.GeocodingApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class AppViewModel : ViewModel() {
    private val _loadingState: MutableStateFlow<LoadingUIState> =
        MutableStateFlow(LoadingUIState.Idle)
    private val _suggestions = MutableStateFlow<List<PlaceItem>>(emptyList())
    val suggestions = _suggestions.asStateFlow()
    val loadingState = _loadingState.asStateFlow()

    suspend fun getSuggestions(
        query: String,
    ) {
        try {
            _loadingState.value = LoadingUIState.Loading
            val response = GeocodingApi.retrofitService.getSuggestions(query)

            _suggestions.value = response.items.map { it }
            _loadingState.value = LoadingUIState.Success
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            _loadingState.value = LoadingUIState.Idle
        }
    }

    @SuppressLint("QueryPermissionsNeeded")
    fun openGoogleMap(context: Context, latitude: Double, longitude: Double) {
        val uri = "https://www.google.com/maps/dir/?api=1&destination=$latitude,$longitude".toUri()
        val intent = Intent(Intent.ACTION_VIEW, uri)
        if (intent.resolveActivity(context.packageManager) != null) {
            context.startActivity(intent)
        } else {
            Toast.makeText(context, "Không tìm thấy ứng dụng Google Maps", Toast.LENGTH_SHORT)
                .show()
        }
    }
}