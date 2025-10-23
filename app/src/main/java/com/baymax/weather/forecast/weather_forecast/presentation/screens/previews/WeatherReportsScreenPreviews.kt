package com.baymax.weather.forecast.weather_forecast.presentation.screens.previews

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.baymax.weather.forecast.weather_forecast.presentation.model.WeatherDAO
import com.baymax.weather.forecast.weather_forecast.presentation.model.WeatherReportsDAO
import com.baymax.weather.forecast.weather_forecast.presentation.screens.WeatherReportsScreen

@Preview(
    name = "Weather Reports - Sunny Day",
    showBackground = true,
    backgroundColor = 0xFF000000,
    heightDp = 800
)
@Composable
fun WeatherReportsScreenSunnyPreview() {
    WeatherReportsScreen(
        weatherReports = WeatherReportsDAO(
            city = "San Francisco",
            country = "USA",
            sunriseTime = "6:30 AM",
            sunsetTime = "7:45 PM",
            currentWeather = WeatherDAO(
                day = "Today",
                dateTime = "2:00 PM",
                time = "14:00",
                iconSmall = "https://openweathermap.org/img/wn/01d.png",
                iconLarge = "https://openweathermap.org/img/wn/01d@2x.png",
                windSpeed = "8 mph",
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
            hourlyWeatherForecast = listOf(
                WeatherDAO(
                    dateTime = "3:00 PM",
                    temperatureF = "77°F",
                    temperatureC = "25°C",
                    weatherDescription = "Clear",
                    iconLarge = "https://openweathermap.org/img/wn/01d@2x.png"
                ),
                WeatherDAO(
                    dateTime = "4:00 PM",
                    temperatureF = "78°F",
                    temperatureC = "26°C",
                    weatherDescription = "Clear",
                    iconLarge = "https://openweathermap.org/img/wn/01d@2x.png"
                ),
                WeatherDAO(
                    dateTime = "5:00 PM",
                    temperatureF = "76°F",
                    temperatureC = "24°C",
                    weatherDescription = "Few Clouds",
                    iconLarge = "https://openweathermap.org/img/wn/02d@2x.png"
                )
            ),
            dailyWeatherForecast = listOf(
                WeatherDAO(
                    day = "Tomorrow",
                    dateTime = "Tomorrow",
                    temperatureF = "80°F",
                    temperatureC = "27°C",
                    weatherDescription = "Sunny",
                    iconLarge = "https://openweathermap.org/img/wn/01d@2x.png"
                ),
                WeatherDAO(
                    day = "Wednesday",
                    dateTime = "Wed",
                    temperatureF = "73°F",
                    temperatureC = "23°C",
                    weatherDescription = "Partly Cloudy",
                    iconLarge = "https://openweathermap.org/img/wn/02d@2x.png"
                ),
                WeatherDAO(
                    day = "Thursday",
                    dateTime = "Thu",
                    temperatureF = "71°F",
                    temperatureC = "22°C",
                    weatherDescription = "Cloudy",
                    iconLarge = "https://openweathermap.org/img/wn/04d@2x.png"
                )
            )
        )
    )
}

@Preview(
    name = "Weather Reports - Rainy Day",
    showBackground = true,
    backgroundColor = 0xFF000000,
    heightDp = 800
)
@Composable
fun WeatherReportsScreenRainyPreview() {
    WeatherReportsScreen(
        weatherReports = WeatherReportsDAO(
            city = "Seattle",
            country = "USA",
            sunriseTime = "7:15 AM",
            sunsetTime = "6:30 PM",
            currentWeather = WeatherDAO(
                day = "Today",
                dateTime = "11:00 AM",
                time = "11:00",
                iconSmall = "https://openweathermap.org/img/wn/10d.png",
                iconLarge = "https://openweathermap.org/img/wn/10d@2x.png",
                windSpeed = "15 mph",
                humidityLevel = "85%",
                temperatureF = "58°F",
                temperatureC = "14°C",
                minTemperatureF = "54°F",
                minTemperatureC = "12°C",
                maxTemperatureF = "62°F",
                maxTemperatureC = "17°C",
                weatherDescription = "Moderate Rain",
                isTodayWeatherInfo = true,
                isRecentWeatherInfo = true
            ),
            hourlyWeatherForecast = listOf(
                WeatherDAO(
                    dateTime = "12:00 PM",
                    temperatureF = "59°F",
                    temperatureC = "15°C",
                    weatherDescription = "Light Rain",
                    iconLarge = "https://openweathermap.org/img/wn/10d@2x.png"
                ),
                WeatherDAO(
                    dateTime = "1:00 PM",
                    temperatureF = "60°F",
                    temperatureC = "16°C",
                    weatherDescription = "Moderate Rain",
                    iconLarge = "https://openweathermap.org/img/wn/10d@2x.png"
                ),
                WeatherDAO(
                    dateTime = "2:00 PM",
                    temperatureF = "61°F",
                    temperatureC = "16°C",
                    weatherDescription = "Heavy Rain",
                    iconLarge = "https://openweathermap.org/img/wn/10d@2x.png"
                )
            ),
            dailyWeatherForecast = listOf(
                WeatherDAO(
                    day = "Tomorrow",
                    dateTime = "Tomorrow",
                    temperatureF = "62°F",
                    temperatureC = "17°C",
                    weatherDescription = "Rainy",
                    iconLarge = "https://openweathermap.org/img/wn/10d@2x.png"
                ),
                WeatherDAO(
                    day = "Wednesday",
                    dateTime = "Wed",
                    temperatureF = "56°F",
                    temperatureC = "13°C",
                    weatherDescription = "Showers",
                    iconLarge = "https://openweathermap.org/img/wn/09d@2x.png"
                ),
                WeatherDAO(
                    day = "Thursday",
                    dateTime = "Thu",
                    temperatureF = "55°F",
                    temperatureC = "13°C",
                    weatherDescription = "Light Rain",
                    iconLarge = "https://openweathermap.org/img/wn/10d@2x.png"
                )
            )
        )
    )
}

@Preview(
    name = "Weather Reports - Cold/Snowy",
    showBackground = true,
    backgroundColor = 0xFF000000,
    heightDp = 800
)
@Composable
fun WeatherReportsScreenColdPreview() {
    WeatherReportsScreen(
        weatherReports = WeatherReportsDAO(
            city = "Chicago",
            country = "USA",
            sunriseTime = "7:00 AM",
            sunsetTime = "4:30 PM",
            currentWeather = WeatherDAO(
                day = "Today",
                dateTime = "9:00 AM",
                time = "09:00",
                iconSmall = "https://openweathermap.org/img/wn/13d.png",
                iconLarge = "https://openweathermap.org/img/wn/13d@2x.png",
                windSpeed = "22 mph",
                humidityLevel = "72%",
                temperatureF = "28°F",
                temperatureC = "-2°C",
                minTemperatureF = "22°F",
                minTemperatureC = "-6°C",
                maxTemperatureF = "32°F",
                maxTemperatureC = "0°C",
                weatherDescription = "Light Snow",
                isTodayWeatherInfo = true,
                isRecentWeatherInfo = true
            ),
            hourlyWeatherForecast = listOf(
                WeatherDAO(
                    dateTime = "10:00 AM",
                    temperatureF = "29°F",
                    temperatureC = "-2°C",
                    weatherDescription = "Light Snow",
                    iconLarge = "https://openweathermap.org/img/wn/13d@2x.png"
                ),
                WeatherDAO(
                    dateTime = "11:00 AM",
                    temperatureF = "30°F",
                    temperatureC = "-1°C",
                    weatherDescription = "Snow",
                    iconLarge = "https://openweathermap.org/img/wn/13d@2x.png"
                ),
                WeatherDAO(
                    dateTime = "12:00 PM",
                    temperatureF = "31°F",
                    temperatureC = "-1°C",
                    weatherDescription = "Heavy Snow",
                    iconLarge = "https://openweathermap.org/img/wn/13d@2x.png"
                )
            ),
            dailyWeatherForecast = listOf(
                WeatherDAO(
                    day = "Tomorrow",
                    dateTime = "Tomorrow",
                    temperatureF = "25°F",
                    temperatureC = "-4°C",
                    weatherDescription = "Snowy",
                    iconLarge = "https://openweathermap.org/img/wn/13d@2x.png"
                ),
                WeatherDAO(
                    day = "Wednesday",
                    dateTime = "Wed",
                    temperatureF = "20°F",
                    temperatureC = "-7°C",
                    weatherDescription = "Blizzard",
                    iconLarge = "https://openweathermap.org/img/wn/13d@2x.png"
                ),
                WeatherDAO(
                    day = "Thursday",
                    dateTime = "Thu",
                    temperatureF = "18°F",
                    temperatureC = "-8°C",
                    weatherDescription = "Clear & Cold",
                    iconLarge = "https://openweathermap.org/img/wn/01d@2x.png"
                )
            )
        )
    )
}

@Preview(
    name = "Weather Reports - Hot Summer Day",
    showBackground = true,
    backgroundColor = 0xFF000000,
    heightDp = 800
)
@Composable
fun WeatherReportsScreenHotPreview() {
    WeatherReportsScreen(
        weatherReports = WeatherReportsDAO(
            city = "Phoenix",
            country = "USA",
            sunriseTime = "5:30 AM",
            sunsetTime = "8:15 PM",
            currentWeather = WeatherDAO(
                day = "Today",
                dateTime = "3:00 PM",
                time = "15:00",
                iconSmall = "https://openweathermap.org/img/wn/01d.png",
                iconLarge = "https://openweathermap.org/img/wn/01d@2x.png",
                windSpeed = "5 mph",
                humidityLevel = "15%",
                temperatureF = "108°F",
                temperatureC = "42°C",
                minTemperatureF = "98°F",
                minTemperatureC = "37°C",
                maxTemperatureF = "112°F",
                maxTemperatureC = "44°C",
                weatherDescription = "Clear Sky",
                isTodayWeatherInfo = true,
                isRecentWeatherInfo = true
            ),
            hourlyWeatherForecast = listOf(
                WeatherDAO(
                    dateTime = "4:00 PM",
                    temperatureF = "110°F",
                    temperatureC = "43°C",
                    weatherDescription = "Clear",
                    iconLarge = "https://openweathermap.org/img/wn/01d@2x.png"
                ),
                WeatherDAO(
                    dateTime = "5:00 PM",
                    temperatureF = "109°F",
                    temperatureC = "43°C",
                    weatherDescription = "Clear",
                    iconLarge = "https://openweathermap.org/img/wn/01d@2x.png"
                ),
                WeatherDAO(
                    dateTime = "6:00 PM",
                    temperatureF = "105°F",
                    temperatureC = "41°C",
                    weatherDescription = "Clear",
                    iconLarge = "https://openweathermap.org/img/wn/01d@2x.png"
                )
            ),
            dailyWeatherForecast = listOf(
                WeatherDAO(
                    day = "Tomorrow",
                    dateTime = "Tomorrow",
                    temperatureF = "110°F",
                    temperatureC = "43°C",
                    weatherDescription = "Sunny & Hot",
                    iconLarge = "https://openweathermap.org/img/wn/01d@2x.png"
                ),
                WeatherDAO(
                    day = "Wednesday",
                    dateTime = "Wed",
                    temperatureF = "112°F",
                    temperatureC = "44°C",
                    weatherDescription = "Very Hot",
                    iconLarge = "https://openweathermap.org/img/wn/01d@2x.png"
                ),
                WeatherDAO(
                    day = "Thursday",
                    dateTime = "Thu",
                    temperatureF = "108°F",
                    temperatureC = "42°C",
                    weatherDescription = "Hot",
                    iconLarge = "https://openweathermap.org/img/wn/01d@2x.png"
                )
            )
        )
    )
}

@Preview(
    name = "Weather Reports - Stormy Weather",
    showBackground = true,
    backgroundColor = 0xFF000000,
    heightDp = 800
)
@Composable
fun WeatherReportsScreenStormyPreview() {
    WeatherReportsScreen(
        weatherReports = WeatherReportsDAO(
            city = "Miami",
            country = "USA",
            sunriseTime = "6:45 AM",
            sunsetTime = "7:30 PM",
            currentWeather = WeatherDAO(
                day = "Today",
                dateTime = "5:00 PM",
                time = "17:00",
                iconSmall = "https://openweathermap.org/img/wn/11d.png",
                iconLarge = "https://openweathermap.org/img/wn/11d@2x.png",
                windSpeed = "35 mph",
                humidityLevel = "92%",
                temperatureF = "78°F",
                temperatureC = "26°C",
                minTemperatureF = "75°F",
                minTemperatureC = "24°C",
                maxTemperatureF = "82°F",
                maxTemperatureC = "28°C",
                weatherDescription = "Thunderstorm",
                isTodayWeatherInfo = true,
                isRecentWeatherInfo = true
            ),
            hourlyWeatherForecast = listOf(
                WeatherDAO(
                    dateTime = "6:00 PM",
                    temperatureF = "77°F",
                    temperatureC = "25°C",
                    weatherDescription = "Thunderstorm with Rain",
                    iconLarge = "https://openweathermap.org/img/wn/11d@2x.png"
                ),
                WeatherDAO(
                    dateTime = "7:00 PM",
                    temperatureF = "76°F",
                    temperatureC = "24°C",
                    weatherDescription = "Heavy Thunderstorm",
                    iconLarge = "https://openweathermap.org/img/wn/11d@2x.png"
                ),
                WeatherDAO(
                    dateTime = "8:00 PM",
                    temperatureF = "75°F",
                    temperatureC = "24°C",
                    weatherDescription = "Thunderstorm",
                    iconLarge = "https://openweathermap.org/img/wn/11d@2x.png"
                )
            ),
            dailyWeatherForecast = listOf(
                WeatherDAO(
                    day = "Tomorrow",
                    dateTime = "Tomorrow",
                    temperatureF = "80°F",
                    temperatureC = "27°C",
                    weatherDescription = "Storms",
                    iconLarge = "https://openweathermap.org/img/wn/11d@2x.png"
                ),
                WeatherDAO(
                    day = "Wednesday",
                    dateTime = "Wed",
                    temperatureF = "82°F",
                    temperatureC = "28°C",
                    weatherDescription = "Scattered Storms",
                    iconLarge = "https://openweathermap.org/img/wn/11d@2x.png"
                ),
                WeatherDAO(
                    day = "Thursday",
                    dateTime = "Thu",
                    temperatureF = "79°F",
                    temperatureC = "26°C",
                    weatherDescription = "Partly Cloudy",
                    iconLarge = "https://openweathermap.org/img/wn/02d@2x.png"
                )
            )
        )
    )
}

@Preview(
    name = "Weather Reports - Long City Name",
    showBackground = true,
    backgroundColor = 0xFF000000,
    heightDp = 800
)
@Composable
fun WeatherReportsScreenLongCityNamePreview() {
    WeatherReportsScreen(
        weatherReports = WeatherReportsDAO(
            city = "São Paulo",
            country = "Brazil",
            sunriseTime = "6:00 AM",
            sunsetTime = "6:30 PM",
            currentWeather = WeatherDAO(
                day = "Today",
                dateTime = "1:00 PM",
                time = "13:00",
                iconSmall = "https://openweathermap.org/img/wn/02d.png",
                iconLarge = "https://openweathermap.org/img/wn/02d@2x.png",
                windSpeed = "10 mph",
                humidityLevel = "60%",
                temperatureF = "72°F",
                temperatureC = "22°C",
                minTemperatureF = "68°F",
                minTemperatureC = "20°C",
                maxTemperatureF = "77°F",
                maxTemperatureC = "25°C",
                weatherDescription = "Partly Cloudy",
                isTodayWeatherInfo = true,
                isRecentWeatherInfo = true
            ),
            hourlyWeatherForecast = listOf(
                WeatherDAO(
                    dateTime = "2:00 PM",
                    temperatureF = "74°F",
                    temperatureC = "23°C",
                    weatherDescription = "Partly Cloudy",
                    iconLarge = "https://openweathermap.org/img/wn/02d@2x.png"
                ),
                WeatherDAO(
                    dateTime = "3:00 PM",
                    temperatureF = "75°F",
                    temperatureC = "24°C",
                    weatherDescription = "Partly Cloudy",
                    iconLarge = "https://openweathermap.org/img/wn/02d@2x.png"
                )
            ),
            dailyWeatherForecast = listOf(
                WeatherDAO(
                    day = "Tomorrow",
                    dateTime = "Tomorrow",
                    temperatureF = "76°F",
                    temperatureC = "24°C",
                    weatherDescription = "Partly Cloudy",
                    iconLarge = "https://openweathermap.org/img/wn/02d@2x.png"
                ),
                WeatherDAO(
                    day = "Wednesday",
                    dateTime = "Wed",
                    temperatureF = "73°F",
                    temperatureC = "23°C",
                    weatherDescription = "Cloudy",
                    iconLarge = "https://openweathermap.org/img/wn/04d@2x.png"
                )
            )
        )
    )
}

