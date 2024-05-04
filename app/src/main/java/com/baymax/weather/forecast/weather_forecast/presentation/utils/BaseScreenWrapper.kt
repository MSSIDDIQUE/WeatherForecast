package com.baymax.weather.forecast.weather_forecast.presentation.utils

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

@Composable
fun BaseScreenWrapper(
    content: @Composable BoxScope.(ScaffoldState) -> Unit,
) {
    val scaffoldState = rememberScaffoldState()
    Scaffold(
        scaffoldState = scaffoldState,
        content = { paddingValue ->
            Box(
                modifier = Modifier
                    .padding(paddingValue)
                    .fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color(0xFF2E335A),
                                Color(0xFF1C1B33),
                            ),
                        ),
                    ),
                content = { content(scaffoldState) },
                contentAlignment = Alignment.Center,
            )
        },
    )
}
