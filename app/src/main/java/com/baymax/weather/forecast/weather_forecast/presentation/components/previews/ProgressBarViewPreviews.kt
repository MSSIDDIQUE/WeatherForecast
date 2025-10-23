package com.baymax.weather.forecast.weather_forecast.presentation.components.previews

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.baymax.weather.forecast.weather_forecast.presentation.components.ProgressBarView

@Preview(
    name = "Progress Bar - Default Loading",
    showBackground = true,
    backgroundColor = 0xFF001f3f
)
@Composable
fun ProgressBarDefaultPreview() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF001f3f))
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        ProgressBarView()
    }
}

@Preview(
    name = "Progress Bar - Custom Message",
    showBackground = true,
    backgroundColor = 0xFF001f3f
)
@Composable
fun ProgressBarCustomMessagePreview() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF001f3f))
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        ProgressBarView("Hello Navratan")
    }
}

