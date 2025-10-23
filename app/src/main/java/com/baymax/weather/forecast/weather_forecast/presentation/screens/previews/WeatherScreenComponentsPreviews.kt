package com.baymax.weather.forecast.weather_forecast.presentation.screens.previews

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.baymax.weather.forecast.weather_forecast.presentation.model.WeatherDAO
import com.baymax.weather.forecast.weather_forecast.presentation.model.WeatherReportsDAO
import com.baymax.weather.forecast.weather_forecast.presentation.screens.MiscellaneousWeatherInfo
import com.baymax.weather.forecast.weather_forecast.presentation.screens.UpperCardView
import com.baymax.weather.forecast.weather_forecast.presentation.screens.WeatherForecast

@Preview(
    name = "Upper Card View - Sunny Weather",
    showBackground = true,
    backgroundColor = 0xFF000000,
    widthDp = 400,
    heightDp = 400
)
@Composable
fun UpperCardViewSunnyPreview() {
    UpperCardView(
        modifier = Modifier,
        weatherReports = WeatherReportsDAO(
            city = "Los Angeles",
            country = "USA",
            sunriseTime = "6:15 AM",
            sunsetTime = "7:30 PM",
            currentWeather = WeatherDAO(
                day = "Today",
                dateTime = "2:00 PM",
                temperatureF = "85°F",
                temperatureC = "29°C",
                weatherDescription = "Clear Sky",
                windSpeed = "8 mph",
                humidityLevel = "40%"
            )
        ),
        isExpanded = false,
        onClick = {}
    )
}

@Preview(
    name = "Upper Card View - Rainy Weather",
    showBackground = true,
    backgroundColor = 0xFF000000,
    widthDp = 400,
    heightDp = 400
)
@Composable
fun UpperCardViewRainyPreview() {
    UpperCardView(
        modifier = Modifier,
        weatherReports = WeatherReportsDAO(
            city = "London",
            country = "UK",
            sunriseTime = "7:45 AM",
            sunsetTime = "5:30 PM",
            currentWeather = WeatherDAO(
                day = "Today",
                dateTime = "11:00 AM",
                temperatureF = "55°F",
                temperatureC = "13°C",
                weatherDescription = "Light Rain",
                windSpeed = "15 mph",
                humidityLevel = "85%"
            )
        ),
        isExpanded = false,
        onClick = {}
    )
}

@Preview(
    name = "Upper Card View - Cold Weather",
    showBackground = true,
    backgroundColor = 0xFF000000,
    widthDp = 400,
    heightDp = 400
)
@Composable
fun UpperCardViewColdPreview() {
    UpperCardView(
        modifier = Modifier,
        weatherReports = WeatherReportsDAO(
            city = "Anchorage",
            country = "USA",
            sunriseTime = "9:30 AM",
            sunsetTime = "3:45 PM",
            currentWeather = WeatherDAO(
                day = "Today",
                dateTime = "12:00 PM",
                temperatureF = "15°F",
                temperatureC = "-9°C",
                weatherDescription = "Snow",
                windSpeed = "20 mph",
                humidityLevel = "75%"
            )
        ),
        isExpanded = false,
        onClick = {}
    )
}

@Preview(
    name = "Upper Card View - Very Hot",
    showBackground = true,
    backgroundColor = 0xFF000000,
    widthDp = 400,
    heightDp = 400
)
@Composable
fun UpperCardViewVeryHotPreview() {
    UpperCardView(
        modifier = Modifier,
        weatherReports = WeatherReportsDAO(
            city = "Dubai",
            country = "UAE",
            sunriseTime = "5:45 AM",
            sunsetTime = "7:15 PM",
            currentWeather = WeatherDAO(
                day = "Today",
                dateTime = "3:00 PM",
                temperatureF = "115°F",
                temperatureC = "46°C",
                weatherDescription = "Clear Sky",
                windSpeed = "5 mph",
                humidityLevel = "20%"
            )
        ),
        isExpanded = false,
        onClick = {}
    )
}

@Preview(
    name = "Upper Card View - Stormy",
    showBackground = true,
    backgroundColor = 0xFF000000,
    widthDp = 400,
    heightDp = 400
)
@Composable
fun UpperCardViewStormyPreview() {
    UpperCardView(
        modifier = Modifier,
        weatherReports = WeatherReportsDAO(
            city = "Houston",
            country = "USA",
            sunriseTime = "6:30 AM",
            sunsetTime = "7:45 PM",
            currentWeather = WeatherDAO(
                day = "Today",
                dateTime = "6:00 PM",
                temperatureF = "75°F",
                temperatureC = "24°C",
                weatherDescription = "Thunderstorm with Heavy Rain",
                windSpeed = "30 mph",
                humidityLevel = "95%"
            )
        ),
        isExpanded = false,
        onClick = {}
    )
}

@Preview(
    name = "Miscellaneous Weather Info - Normal Conditions",
    showBackground = true,
    backgroundColor = 0xFF000000,
    widthDp = 400
)
@Composable
fun MiscellaneousWeatherInfoNormalPreview() {
    MiscellaneousWeatherInfo(
        state = WeatherReportsDAO(
            city = "Boston",
            country = "USA",
            sunriseTime = "6:30 AM",
            sunsetTime = "7:45 PM",
            currentWeather = WeatherDAO(
                windSpeed = "12 mph",
                humidityLevel = "65%"
            )
        ),
        isExpanded = true
    )
}

@Preview(
    name = "Miscellaneous Weather Info - Early Sunrise",
    showBackground = true,
    backgroundColor = 0xFF000000,
    widthDp = 400
)
@Composable
fun MiscellaneousWeatherInfoEarlySunrisePreview() {
    MiscellaneousWeatherInfo(
        state = WeatherReportsDAO(
            city = "Stockholm",
            country = "Sweden",
            sunriseTime = "3:45 AM",
            sunsetTime = "10:30 PM",
            currentWeather = WeatherDAO(
                windSpeed = "8 mph",
                humidityLevel = "55%"
            )
        ),
        isExpanded = true
    )
}

@Preview(
    name = "Miscellaneous Weather Info - Late Sunrise",
    showBackground = true,
    backgroundColor = 0xFF000000,
    widthDp = 400
)
@Composable
fun MiscellaneousWeatherInfoLateSunrisePreview() {
    MiscellaneousWeatherInfo(
        state = WeatherReportsDAO(
            city = "Reykjavik",
            country = "Iceland",
            sunriseTime = "11:00 AM",
            sunsetTime = "3:30 PM",
            currentWeather = WeatherDAO(
                windSpeed = "25 mph",
                humidityLevel = "78%"
            )
        ),
        isExpanded = true
    )
}

@Preview(
    name = "Miscellaneous Weather Info - High Wind",
    showBackground = true,
    backgroundColor = 0xFF000000,
    widthDp = 400
)
@Composable
fun MiscellaneousWeatherInfoHighWindPreview() {
    MiscellaneousWeatherInfo(
        state = WeatherReportsDAO(
            city = "Wellington",
            country = "New Zealand",
            sunriseTime = "6:45 AM",
            sunsetTime = "7:15 PM",
            currentWeather = WeatherDAO(
                windSpeed = "45 mph",
                humidityLevel = "72%"
            )
        ),
        isExpanded = true
    )
}

@Preview(
    name = "Miscellaneous Weather Info - High Humidity",
    showBackground = true,
    backgroundColor = 0xFF000000,
    widthDp = 400
)
@Composable
fun MiscellaneousWeatherInfoHighHumidityPreview() {
    MiscellaneousWeatherInfo(
        state = WeatherReportsDAO(
            city = "Singapore",
            country = "Singapore",
            sunriseTime = "7:00 AM",
            sunsetTime = "7:15 PM",
            currentWeather = WeatherDAO(
                windSpeed = "5 mph",
                humidityLevel = "98%"
            )
        ),
        isExpanded = true
    )
}

@Preview(
    name = "Miscellaneous Weather Info - Low Humidity",
    showBackground = true,
    backgroundColor = 0xFF000000,
    widthDp = 400
)
@Composable
fun MiscellaneousWeatherInfoLowHumidityPreview() {
    MiscellaneousWeatherInfo(
        state = WeatherReportsDAO(
            city = "Las Vegas",
            country = "USA",
            sunriseTime = "6:00 AM",
            sunsetTime = "8:00 PM",
            currentWeather = WeatherDAO(
                windSpeed = "7 mph",
                humidityLevel = "15%"
            )
        ),
        isExpanded = true
    )
}

@Preview(
    name = "Weather Forecast - Hourly & Daily (Hourly Tab)",
    showBackground = true,
    backgroundColor = 0xFF000000,
    widthDp = 400,
    heightDp = 600
)
@Composable
fun WeatherForecastHourlyTabPreview() {
    WeatherForecast(
        listOfForecastsTypes = listOf(
            // Hourly forecast
            listOf(
                WeatherDAO(
                    dateTime = "3:00 PM",
                    temperatureF = "75°F",
                    temperatureC = "24°C",
                    weatherDescription = "Clear",
                    iconLarge = "https://openweathermap.org/img/wn/01d@2x.png"
                ),
                WeatherDAO(
                    dateTime = "4:00 PM",
                    temperatureF = "77°F",
                    temperatureC = "25°C",
                    weatherDescription = "Clear",
                    iconLarge = "https://openweathermap.org/img/wn/01d@2x.png"
                ),
                WeatherDAO(
                    dateTime = "5:00 PM",
                    temperatureF = "76°F",
                    temperatureC = "24°C",
                    weatherDescription = "Few Clouds",
                    iconLarge = "https://openweathermap.org/img/wn/02d@2x.png"
                ),
                WeatherDAO(
                    dateTime = "6:00 PM",
                    temperatureF = "73°F",
                    temperatureC = "23°C",
                    weatherDescription = "Partly Cloudy",
                    iconLarge = "https://openweathermap.org/img/wn/02d@2x.png"
                ),
                WeatherDAO(
                    dateTime = "7:00 PM",
                    temperatureF = "70°F",
                    temperatureC = "21°C",
                    weatherDescription = "Cloudy",
                    iconLarge = "https://openweathermap.org/img/wn/04d@2x.png"
                )
            ),
            // Daily forecast
            listOf(
                WeatherDAO(
                    day = "Monday",
                    dateTime = "Mon",
                    temperatureF = "80°F",
                    temperatureC = "27°C",
                    weatherDescription = "Sunny",
                    iconLarge = "https://openweathermap.org/img/wn/01d@2x.png"
                ),
                WeatherDAO(
                    day = "Tuesday",
                    dateTime = "Tue",
                    temperatureF = "78°F",
                    temperatureC = "26°C",
                    weatherDescription = "Partly Cloudy",
                    iconLarge = "https://openweathermap.org/img/wn/02d@2x.png"
                ),
                WeatherDAO(
                    day = "Wednesday",
                    dateTime = "Wed",
                    temperatureF = "75°F",
                    temperatureC = "24°C",
                    weatherDescription = "Cloudy",
                    iconLarge = "https://openweathermap.org/img/wn/04d@2x.png"
                )
            )
        )
    )
}

@Preview(
    name = "Weather Forecast - Mixed Conditions",
    showBackground = true,
    backgroundColor = 0xFF000000,
    widthDp = 400,
    heightDp = 600
)
@Composable
fun WeatherForecastMixedConditionsPreview() {
    WeatherForecast(
        listOfForecastsTypes = listOf(
            // Hourly forecast with varying conditions
            listOf(
                WeatherDAO(
                    dateTime = "Now",
                    temperatureF = "68°F",
                    temperatureC = "20°C",
                    weatherDescription = "Partly Cloudy",
                    iconLarge = "https://openweathermap.org/img/wn/02d@2x.png"
                ),
                WeatherDAO(
                    dateTime = "1:00 PM",
                    temperatureF = "70°F",
                    temperatureC = "21°C",
                    weatherDescription = "Light Rain",
                    iconLarge = "https://openweathermap.org/img/wn/10d@2x.png"
                ),
                WeatherDAO(
                    dateTime = "2:00 PM",
                    temperatureF = "65°F",
                    temperatureC = "18°C",
                    weatherDescription = "Moderate Rain",
                    iconLarge = "https://openweathermap.org/img/wn/10d@2x.png"
                ),
                WeatherDAO(
                    dateTime = "3:00 PM",
                    temperatureF = "63°F",
                    temperatureC = "17°C",
                    weatherDescription = "Thunderstorm",
                    iconLarge = "https://openweathermap.org/img/wn/11d@2x.png"
                ),
                WeatherDAO(
                    dateTime = "4:00 PM",
                    temperatureF = "67°F",
                    temperatureC = "19°C",
                    weatherDescription = "Partly Cloudy",
                    iconLarge = "https://openweathermap.org/img/wn/02d@2x.png"
                )
            ),
            // Daily forecast
            listOf(
                WeatherDAO(
                    day = "Tomorrow",
                    dateTime = "Tomorrow",
                    temperatureF = "72°F",
                    temperatureC = "22°C",
                    weatherDescription = "Rainy",
                    iconLarge = "https://openweathermap.org/img/wn/10d@2x.png"
                ),
                WeatherDAO(
                    day = "Friday",
                    dateTime = "Fri",
                    temperatureF = "75°F",
                    temperatureC = "24°C",
                    weatherDescription = "Partly Cloudy",
                    iconLarge = "https://openweathermap.org/img/wn/02d@2x.png"
                ),
                WeatherDAO(
                    day = "Saturday",
                    dateTime = "Sat",
                    temperatureF = "80°F",
                    temperatureC = "27°C",
                    weatherDescription = "Sunny",
                    iconLarge = "https://openweathermap.org/img/wn/01d@2x.png"
                ),
                WeatherDAO(
                    day = "Sunday",
                    dateTime = "Sun",
                    temperatureF = "82°F",
                    temperatureC = "28°C",
                    weatherDescription = "Sunny",
                    iconLarge = "https://openweathermap.org/img/wn/01d@2x.png"
                )
            )
        )
    )
}

