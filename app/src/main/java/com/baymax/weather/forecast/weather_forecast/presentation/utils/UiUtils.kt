package com.baymax.weather.forecast.weather_forecast.presentation.utils

import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layout

object UiUtils {
    fun Modifier.vertical(enabled: Boolean = true) =
        if (enabled)
            layout { measurable, constraints ->
                val placeable = measurable.measure(constraints.copy(maxWidth = Int.MAX_VALUE))
                layout(placeable.height, placeable.width) {
                    placeable.place(
                        x = -(placeable.width / 2 - placeable.height / 2),
                        y = -(placeable.height / 2 - placeable.width / 2)
                    )
                }
            } else this
}