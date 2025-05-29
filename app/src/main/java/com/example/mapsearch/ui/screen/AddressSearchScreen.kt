package com.example.mapsearch.ui.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mapsearch.R
import com.example.mapsearch.module.LoadingUIState
import com.example.mapsearch.module.PlaceItem
import com.example.mapsearch.viewmodel.AppViewModel
import kotlinx.coroutines.delay


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AddressSearchScreen(
    appViewModel: AppViewModel
) {
    var query by remember { mutableStateOf("") }
    val context = LocalContext.current
    val suggestions by appViewModel.suggestions.collectAsState()
    val loadingState by appViewModel.loadingState.collectAsState()
    val debounceTime = 1000L
    LaunchedEffect(query) {
        delay(debounceTime)
        if (query.length >= 3)
            appViewModel.getSuggestions(query)
    }
    Scaffold {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 30.dp, start = 10.dp, end = 10.dp)
        ) {
            MapSearchBar(
                modifier = Modifier.fillMaxWidth(),
                loadingUIState = loadingState,
                onValueChange = {
                    query = it
                }
            )
            LazyColumn {
                items(suggestions.size) { index ->
                    LocationItem(
                        modifier = Modifier.fillMaxWidth(),
                        placeItem = suggestions[index],
                        keyword = query,
                        onDirectionClick = {
                            appViewModel.openGoogleMap(
                                context = context,
                                latitude = suggestions[index].position?.lat ?: 0.0,
                                longitude = suggestions[index].position?.lng ?: 0.0
                            )
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun MapSearchBar(
    modifier: Modifier = Modifier,
    loadingUIState: LoadingUIState = LoadingUIState.Idle,
    onValueChange: (String) -> Unit = {}
) {
    var value by remember { mutableStateOf("") }
    OutlinedTextField(
        modifier = modifier,
        value = value,
        onValueChange = {
            value = it
            onValueChange(it)
        },
        placeholder = { Text("Enter keyword") },
        singleLine = true,
        shape = RoundedCornerShape(100.dp),
        prefix = {
            if (loadingUIState != LoadingUIState.Loading)
                Icon(Icons.Filled.Search, contentDescription = "Search Icon")
            else
                CircularProgressIndicator()
        },
        suffix = {
            IconButton(onClick = {
                value = ""
                onValueChange("")
            }) {
                Icon(Icons.Filled.Clear, contentDescription = "Clear Icon")
            }
        }

    )
}

@Composable
fun LocationItem(
    modifier: Modifier = Modifier,
    keyword: String,
    placeItem: PlaceItem,
    onDirectionClick: () -> Unit = {}
) {
    val fullText = "${placeItem.address?.label} (${placeItem.title})"
    val buildAnnotationString = buildAnnotatedString {
        val keywordLower = keyword.lowercase()
        val fullTextLower = fullText.lowercase()
        val startIndex = fullTextLower.indexOf(keywordLower)
        if (startIndex >= 0) {
            append(fullText.substring(0, startIndex))
            withStyle(style = SpanStyle(color = Color.Black, fontWeight = FontWeight.Bold)) {
                append(fullText.substring(startIndex, startIndex + keyword.length))
            }
            append(fullText.substring(startIndex + keyword.length))
        } else {
            append(fullText)
        }
    }
    Row(
        modifier = modifier.padding(vertical = 5.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(Icons.Outlined.LocationOn, contentDescription = "Location Icon")
        Text(modifier = Modifier.weight(1f), text = buildAnnotationString)
        IconButton(
            onClick = onDirectionClick,
        ) {
            Icon(
                painter = painterResource(R.drawable.directions),
                contentDescription = "Directions Icon"
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LocationItemPreview() {
    LocationItem(
        keyword = "Title",
        placeItem = PlaceItem(
            title = "Title",
            address = null,
            position = null
        )
    )
}


@Preview
@Composable
fun MapSearchScreenPreview() {
    AddressSearchScreen(appViewModel = AppViewModel())
}

@Preview(showBackground = true)
@Composable
fun MapSearchBarPreview() {
    MapSearchBar()
}