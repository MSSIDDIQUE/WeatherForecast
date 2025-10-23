package com.baymax.weather.forecast.weather_forecast.presentation.screens.previews

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.baymax.weather.forecast.fetch_location.presentation.model.PredictionDAO
import com.baymax.weather.forecast.weather_forecast.presentation.components.ProgressBarView
import com.baymax.weather.forecast.weather_forecast.presentation.screens.SearchLocationBottomSheetView

@Preview(
    name = "Progress Bar - Default Loading",
    showBackground = true,
    backgroundColor = 0xFF000000
)
@Composable
fun ProgressBarViewDefaultPreview() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp)
    ) {
        ProgressBarView()
    }
}

@Preview(
    name = "Progress Bar - Custom Loading Message",
    showBackground = true,
    backgroundColor = 0xFF000000
)
@Composable
fun ProgressBarViewCustomMessagePreview() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp)
    ) {
        ProgressBarView("Fetching Weather Data...")
    }
}

@Preview(
    name = "Progress Bar - Short Message",
    showBackground = true,
    backgroundColor = 0xFF000000
)
@Composable
fun ProgressBarViewShortMessagePreview() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp)
    ) {
        ProgressBarView("Please Wait")
    }
}

@Preview(
    name = "Progress Bar - Long Message",
    showBackground = true,
    backgroundColor = 0xFF000000
)
@Composable
fun ProgressBarViewLongMessagePreview() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp)
    ) {
        ProgressBarView("Updating location and fetching latest forecast...")
    }
}

@Preview(
    name = "Search Location Bottom Sheet - Empty Query",
    showBackground = true,
    backgroundColor = 0xFF000000,
    heightDp = 600
)
@Composable
fun SearchLocationBottomSheetEmptyQueryPreview() {
    SearchLocationBottomSheetView(
        initSearchQuery = "",
        listOfPredictions = emptyList(),
        onPredictionClick = {},
        onSearchQueryUpdate = {},
        onCurrentLocationClick = {}
    )
}

@Preview(
    name = "Search Location Bottom Sheet - With Query, No Results",
    showBackground = true,
    backgroundColor = 0xFF000000,
    heightDp = 600
)
@Composable
fun SearchLocationBottomSheetNoResultsPreview() {
    SearchLocationBottomSheetView(
        initSearchQuery = "Xyz",
        listOfPredictions = emptyList(),
        onPredictionClick = {},
        onSearchQueryUpdate = {},
        onCurrentLocationClick = {}
    )
}

@Preview(
    name = "Search Location Bottom Sheet - With Few Predictions",
    showBackground = true,
    backgroundColor = 0xFF000000,
    heightDp = 600
)
@Composable
fun SearchLocationBottomSheetFewPredictionsPreview() {
    SearchLocationBottomSheetView(
        initSearchQuery = "New",
        listOfPredictions = listOf(
            PredictionDAO(
                placeId = "1",
                description = "New York, NY, USA"
            ),
            PredictionDAO(
                placeId = "2",
                description = "New Delhi, India"
            ),
            PredictionDAO(
                placeId = "3",
                description = "New Orleans, LA, USA"
            )
        ),
        onPredictionClick = {},
        onSearchQueryUpdate = {},
        onCurrentLocationClick = {}
    )
}

@Preview(
    name = "Search Location Bottom Sheet - Many Predictions",
    showBackground = true,
    backgroundColor = 0xFF000000,
    heightDp = 600
)
@Composable
fun SearchLocationBottomSheetManyPredictionsPreview() {
    SearchLocationBottomSheetView(
        initSearchQuery = "San",
        listOfPredictions = listOf(
            PredictionDAO(
                placeId = "1",
                description = "San Francisco, CA, USA"
            ),
            PredictionDAO(
                placeId = "2",
                description = "San Diego, CA, USA"
            ),
            PredictionDAO(
                placeId = "3",
                description = "San Jose, CA, USA"
            ),
            PredictionDAO(
                placeId = "4",
                description = "San Antonio, TX, USA"
            ),
            PredictionDAO(
                placeId = "5",
                description = "Santiago, Chile"
            ),
            PredictionDAO(
                placeId = "6",
                description = "Santa Clara, CA, USA"
            ),
            PredictionDAO(
                placeId = "7",
                description = "San Salvador, El Salvador"
            ),
            PredictionDAO(
                placeId = "8",
                description = "Santa Monica, CA, USA"
            )
        ),
        onPredictionClick = {},
        onSearchQueryUpdate = {},
        onCurrentLocationClick = {}
    )
}

@Preview(
    name = "Search Location Bottom Sheet - Long City Names",
    showBackground = true,
    backgroundColor = 0xFF000000,
    heightDp = 600
)
@Composable
fun SearchLocationBottomSheetLongNamesPreview() {
    SearchLocationBottomSheetView(
        initSearchQuery = "Saint",
        listOfPredictions = listOf(
            PredictionDAO(
                placeId = "1",
                description = "Saint Petersburg, Florida, United States of America"
            ),
            PredictionDAO(
                placeId = "2",
                description = "Saint-Étienne, Auvergne-Rhône-Alpes, France"
            ),
            PredictionDAO(
                placeId = "3",
                description = "Saint John's, Newfoundland and Labrador, Canada"
            )
        ),
        onPredictionClick = {},
        onSearchQueryUpdate = {},
        onCurrentLocationClick = {}
    )
}

@Preview(
    name = "Search Location Bottom Sheet - International Characters",
    showBackground = true,
    backgroundColor = 0xFF000000,
    heightDp = 600
)
@Composable
fun SearchLocationBottomSheetInternationalPreview() {
    SearchLocationBottomSheetView(
        initSearchQuery = "北京",
        listOfPredictions = listOf(
            PredictionDAO(
                placeId = "1",
                description = "北京市, 中国 (Beijing, China)"
            ),
            PredictionDAO(
                placeId = "2",
                description = "München, Deutschland (Munich, Germany)"
            ),
            PredictionDAO(
                placeId = "3",
                description = "São Paulo, Brasil"
            ),
            PredictionDAO(
                placeId = "4",
                description = "Москва, Россия (Moscow, Russia)"
            )
        ),
        onPredictionClick = {},
        onSearchQueryUpdate = {},
        onCurrentLocationClick = {}
    )
}

