package com.example.mapsearch.module

sealed class LoadingUIState {
    data object Loading: LoadingUIState()
    data object Idle: LoadingUIState()
    data object Success: LoadingUIState()
    data object Failed: LoadingUIState()
}