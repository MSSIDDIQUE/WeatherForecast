package com.baymax.weather.forecast.weather_forecast.presentation.components.previews

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.baymax.weather.forecast.R
import com.baymax.weather.forecast.weather_forecast.presentation.components.WeatherMiscellaneousParamItem

@Preview(
    name = "Weather Param - Sunrise",
    showBackground = true,
    backgroundColor = 0xFF000000
)
@Composable
fun WeatherMiscellaneousParamItemSunrisePreview() {
    Row(
        modifier = Modifier
            .background(Color.Black)
            .padding(16.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        WeatherMiscellaneousParamItem(
            params = Triple(
                R.drawable.ic_sunrise_sun,
                R.string.sunrise,
                "6:30 AM"
            )
        )
    }
}

@Preview(
    name = "Weather Param - Sunset",
    showBackground = true,
    backgroundColor = 0xFF000000
)
@Composable
fun WeatherMiscellaneousParamItemSunsetPreview() {
    Row(
        modifier = Modifier
            .background(Color.Black)
            .padding(16.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        WeatherMiscellaneousParamItem(
            params = Triple(
                R.drawable.ic_sunset_sun,
                R.string.sunset,
                "7:45 PM"
            )
        )
    }
}

@Preview(
    name = "Weather Param - Wind Speed (Low)",
    showBackground = true,
    backgroundColor = 0xFF000000
)
@Composable
fun WeatherMiscellaneousParamItemWindSpeedLowPreview() {
    Row(
        modifier = Modifier
            .background(Color.Black)
            .padding(16.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        WeatherMiscellaneousParamItem(
            params = Triple(
                R.drawable.ic_wind_speed,
                R.string.wind_speed,
                "3 mph"
            )
        )
    }
}

@Preview(
    name = "Weather Param - Wind Speed (High)",
    showBackground = true,
    backgroundColor = 0xFF000000
)
@Composable
fun WeatherMiscellaneousParamItemWindSpeedHighPreview() {
    Row(
        modifier = Modifier
            .background(Color.Black)
            .padding(16.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        WeatherMiscellaneousParamItem(
            params = Triple(
                R.drawable.ic_wind_speed,
                R.string.wind_speed,
                "35 mph"
            )
        )
    }
}

@Preview(
    name = "Weather Param - Humidity (Low)",
    showBackground = true,
    backgroundColor = 0xFF000000
)
@Composable
fun WeatherMiscellaneousParamItemHumidityLowPreview() {
    Row(
        modifier = Modifier
            .background(Color.Black)
            .padding(16.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        WeatherMiscellaneousParamItem(
            params = Triple(
                R.drawable.ic_humidity,
                R.string.humidity,
                "25%"
            )
        )
    }
}

@Preview(
    name = "Weather Param - Humidity (High)",
    showBackground = true,
    backgroundColor = 0xFF000000
)
@Composable
fun WeatherMiscellaneousParamItemHumidityHighPreview() {
    Row(
        modifier = Modifier
            .background(Color.Black)
            .padding(16.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        WeatherMiscellaneousParamItem(
            params = Triple(
                R.drawable.ic_humidity,
                R.string.humidity,
                "95%"
            )
        )
    }
}

@Preview(
    name = "Weather Params - All Together",
    showBackground = true,
    backgroundColor = 0xFF000000,
    widthDp = 400
)
@Composable
fun WeatherMiscellaneousParamItemsAllPreview() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Black)
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        WeatherMiscellaneousParamItem(
            params = Triple(R.drawable.ic_sunrise_sun, R.string.sunrise, "6:30 AM")
        )
        WeatherMiscellaneousParamItem(
            params = Triple(R.drawable.ic_wind_speed, R.string.wind_speed, "12 mph")
        )
        WeatherMiscellaneousParamItem(
            params = Triple(R.drawable.ic_humidity, R.string.humidity, "65%")
        )
        WeatherMiscellaneousParamItem(
            params = Triple(R.drawable.ic_sunset_sun, R.string.sunset, "7:45 PM")
        )
    }
}

