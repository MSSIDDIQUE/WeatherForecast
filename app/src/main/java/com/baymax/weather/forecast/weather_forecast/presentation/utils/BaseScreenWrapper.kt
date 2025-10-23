package com.baymax.weather.forecast.weather_forecast.presentation.utils

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import com.baymax.launcherapp.ui.theme.DarkBlue
import com.baymax.launcherapp.ui.theme.DarkestBlue
import com.baymax.weather.forecast.weather_forecast.presentation.components.SearchLocationView

@Composable
fun BaseScreenWrapper(
    city: String? = null,
    onSearchClick: (() -> Unit)? = null,
    mainContent: @Composable BoxScope.() -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = Brush.verticalGradient(colors = listOf(DarkBlue, DarkestBlue)))
            .windowInsetsPadding(WindowInsets.statusBars),
        contentAlignment = Alignment.Center,
    ) {
        mainContent()

        if (city != null && onSearchClick != null) {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.TopStart
            ) {
                SearchLocationView(
                    city = city,
                    onSearchClick = onSearchClick
                )
            }
        }
    }
}