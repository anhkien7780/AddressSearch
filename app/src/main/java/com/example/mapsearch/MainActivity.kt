package com.example.mapsearch

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.mapsearch.ui.screen.AddressSearchScreen
import com.example.mapsearch.ui.theme.MapSearchTheme
import com.example.mapsearch.viewmodel.AppViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val appViewModel = AppViewModel()
            MapSearchTheme {
                AddressSearchScreen(
                    appViewModel = appViewModel
                )
            }
        }
    }
}
