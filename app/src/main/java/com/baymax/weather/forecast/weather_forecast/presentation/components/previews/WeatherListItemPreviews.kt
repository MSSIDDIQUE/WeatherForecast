package com.baymax.weather.forecast.weather_forecast.presentation.components.previews

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.baymax.weather.forecast.weather_forecast.presentation.components.WeatherListItem
import com.baymax.weather.forecast.weather_forecast.presentation.model.WeatherDAO

@Preview(
    name = "Weather List Item - Sunny Day (Fahrenheit)",
    showBackground = true,
    backgroundColor = 0xFF000000
)
@Composable
fun WeatherListItemSunnyFahrenheitPreview() {
    val scaleSwitchState = remember { mutableStateOf(0) } // 0 = Fahrenheit
    Column(
        modifier = Modifier
            .background(Color.Black)
            .padding(16.dp)
    ) {
        WeatherListItem(
            item = WeatherDAO(
                day = "Monday",
                dateTime = "10:00 AM",
                time = "10:00",
                iconSmall = "https://openweathermap.org/img/wn/01d.png",
                iconLarge = "https://openweathermap.org/img/wn/01d@2x.png",
                windSpeed = "5 mph",
                humidityLevel = "45%",
                temperatureF = "75°F",
                temperatureC = "24°C",
                minTemperatureF = "68°F",
                minTemperatureC = "20°C",
                maxTemperatureF = "82°F",
                maxTemperatureC = "28°C",
                weatherDescription = "Clear Sky",
                isTodayWeatherInfo = true,
                isRecentWeatherInfo = true
            ),
            scaleSwitchIndexState = scaleSwitchState
        )
    }
}

@Preview(
    name = "Weather List Item - Sunny Day (Celsius)",
    showBackground = true,
    backgroundColor = 0xFF000000
)
@Composable
fun WeatherListItemSunnyCelsiusPreview() {
    val scaleSwitchState = remember { mutableStateOf(1) } // 1 = Celsius
    Column(
        modifier = Modifier
            .background(Color.Black)
            .padding(16.dp)
    ) {
        WeatherListItem(
            item = WeatherDAO(
                day = "Monday",
                dateTime = "10:00 AM",
                time = "10:00",
                iconSmall = "https://openweathermap.org/img/wn/01d.png",
                iconLarge = "https://openweathermap.org/img/wn/01d@2x.png",
                windSpeed = "5 mph",
                humidityLevel = "45%",
                temperatureF = "75°F",
                temperatureC = "24°C",
                minTemperatureF = "68°F",
                minTemperatureC = "20°C",
                maxTemperatureF = "82°F",
                maxTemperatureC = "28°C",
                weatherDescription = "Clear Sky",
                isTodayWeatherInfo = true,
                isRecentWeatherInfo = true
            ),
            scaleSwitchIndexState = scaleSwitchState
        )
    }
}

@Preview(
    name = "Weather List Item - Rainy",
    showBackground = true,
    backgroundColor = 0xFF000000
)
@Composable
fun WeatherListItemRainyPreview() {
    val scaleSwitchState = remember { mutableStateOf(1) }
    Column(
        modifier = Modifier
            .background(Color.Black)
            .padding(16.dp)
    ) {
        WeatherListItem(
            item = WeatherDAO(
                day = "Tuesday",
                dateTime = "2:00 PM",
                time = "14:00",
                iconSmall = "https://openweathermap.org/img/wn/10d.png",
                iconLarge = "https://openweathermap.org/img/wn/10d@2x.png",
                windSpeed = "12 mph",
                humidityLevel = "85%",
                temperatureF = "62°F",
                temperatureC = "17°C",
                minTemperatureF = "58°F",
                minTemperatureC = "14°C",
                maxTemperatureF = "65°F",
                maxTemperatureC = "18°C",
                weatherDescription = "Moderate Rain",
                isTodayWeatherInfo = false,
                isRecentWeatherInfo = false
            ),
            scaleSwitchIndexState = scaleSwitchState
        )
    }
}

@Preview(
    name = "Weather List Item - Thunderstorm",
    showBackground = true,
    backgroundColor = 0xFF000000
)
@Composable
fun WeatherListItemThunderstormPreview() {
    val scaleSwitchState = remember { mutableIntStateOf(0) }
    Column(
        modifier = Modifier
            .background(Color.Black)
            .padding(16.dp)
    ) {
        WeatherListItem(
            item = WeatherDAO(
                day = "Wednesday",
                dateTime = "8:00 PM",
                time = "20:00",
                iconSmall = "https://openweathermap.org/img/wn/11d.png",
                iconLarge = "https://openweathermap.org/img/wn/11d@2x.png",
                windSpeed = "25 mph",
                humidityLevel = "92%",
                temperatureF = "68°F",
                temperatureC = "20°C",
                minTemperatureF = "64°F",
                minTemperatureC = "18°C",
                maxTemperatureF = "70°F",
                maxTemperatureC = "21°C",
                weatherDescription = "Thunderstorm with Heavy Rain",
                isTodayWeatherInfo = false,
                isRecentWeatherInfo = true
            ),
            scaleSwitchIndexState = scaleSwitchState
        )
    }
}

@Preview(
    name = "Weather List Item - Snowy",
    showBackground = true,
    backgroundColor = 0xFF000000
)
@Composable
fun WeatherListItemSnowyPreview() {
    val scaleSwitchState = remember { mutableStateOf(0) }
    Column(
        modifier = Modifier
            .background(Color.Black)
            .padding(16.dp)
    ) {
        WeatherListItem(
            item = WeatherDAO(
                day = "Thursday",
                dateTime = "6:00 AM",
                time = "06:00",
                iconSmall = "https://openweathermap.org/img/wn/13d.png",
                iconLarge = "https://openweathermap.org/img/wn/13d@2x.png",
                windSpeed = "8 mph",
                humidityLevel = "78%",
                temperatureF = "28°F",
                temperatureC = "-2°C",
                minTemperatureF = "25°F",
                minTemperatureC = "-4°C",
                maxTemperatureF = "32°F",
                maxTemperatureC = "0°C",
                weatherDescription = "Light Snow",
                isTodayWeatherInfo = false,
                isRecentWeatherInfo = false
            ),
            scaleSwitchIndexState = scaleSwitchState
        )
    }
}

@Preview(
    name = "Weather List Item - Cloudy",
    showBackground = true,
    backgroundColor = 0xFF000000
)
@Composable
fun WeatherListItemCloudyPreview() {
    val scaleSwitchState = remember { mutableStateOf(1) }
    Column(
        modifier = Modifier
            .background(Color.Black)
            .padding(16.dp)
    ) {
        WeatherListItem(
            item = WeatherDAO(
                day = "Friday",
                dateTime = "12:00 PM",
                time = "12:00",
                iconSmall = "https://openweathermap.org/img/wn/04d.png",
                iconLarge = "https://openweathermap.org/img/wn/04d@2x.png",
                windSpeed = "7 mph",
                humidityLevel = "65%",
                temperatureF = "70°F",
                temperatureC = "21°C",
                minTemperatureF = "67°F",
                minTemperatureC = "19°C",
                maxTemperatureF = "73°F",
                maxTemperatureC = "23°C",
                weatherDescription = "Overcast Clouds",
                isTodayWeatherInfo = true,
                isRecentWeatherInfo = false
            ),
            scaleSwitchIndexState = scaleSwitchState
        )
    }
}

@Preview(
    name = "Weather List Item - Long Description",
    showBackground = true,
    backgroundColor = 0xFF000000
)
@Composable
fun WeatherListItemLongDescriptionPreview() {
    val scaleSwitchState = remember { mutableStateOf(1) }
    Column(
        modifier = Modifier
            .background(Color.Black)
            .padding(16.dp)
    ) {
        WeatherListItem(
            item = WeatherDAO(
                day = "Saturday",
                dateTime = "9:30 AM",
                time = "09:30",
                iconSmall = "https://openweathermap.org/img/wn/50d.png",
                iconLarge = "https://openweathermap.org/img/wn/50d@2x.png",
                windSpeed = "3 mph",
                humidityLevel = "88%",
                temperatureF = "56°F",
                temperatureC = "13°C",
                minTemperatureF = "52°F",
                minTemperatureC = "11°C",
                maxTemperatureF = "59°F",
                maxTemperatureC = "15°C",
                weatherDescription = "Very Dense Fog with Reduced Visibility",
                isTodayWeatherInfo = false,
                isRecentWeatherInfo = false
            ),
            scaleSwitchIndexState = scaleSwitchState
        )
    }
}

