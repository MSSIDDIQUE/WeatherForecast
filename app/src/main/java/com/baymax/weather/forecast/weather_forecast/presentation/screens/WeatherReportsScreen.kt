package com.baymax.weather.forecast.weather_forecast.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.TabRowDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.baymax.launcherapp.ui.theme.DarkerBlue
import com.baymax.launcherapp.ui.theme.DullBlue
import com.baymax.launcherapp.ui.theme.FluorescentPink
import com.baymax.launcherapp.ui.theme.JetBlack
import com.baymax.weather.forecast.R
import com.baymax.weather.forecast.weather_forecast.presentation.components.SearchLocationView
import com.baymax.weather.forecast.weather_forecast.presentation.components.TemperatureSwitch
import com.baymax.weather.forecast.weather_forecast.presentation.components.VerticalTabs
import com.baymax.weather.forecast.weather_forecast.presentation.components.WeatherListItem
import com.baymax.weather.forecast.weather_forecast.presentation.components.WeatherMiscellaneousParamItem
import com.baymax.weather.forecast.weather_forecast.presentation.model.WeatherDAO
import com.baymax.weather.forecast.weather_forecast.presentation.model.WeatherReportsDAO

private val scaleSwitchIndexState = mutableStateOf(1)

@Composable
fun WeatherReportsScreen(
    weatherReports: WeatherReportsDAO,
    onSearchClick: () -> Unit,
) = with(weatherReports) {
    val cardShape = RoundedCornerShape(
        topStart = 0.dp,
        topEnd = 0.dp,
        bottomStart = 50.dp,
        bottomEnd = 50.dp,
    )
    val cardModifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight()
        .shadow(
            elevation = 4.dp, shape = cardShape
        )
        .background(
            brush = Brush.verticalGradient(
                colors = listOf(
                    JetBlack,
                    DarkerBlue,
                ),
            ),
            shape = RoundedCornerShape(
                topStart = 0.dp,
                topEnd = 0.dp,
                bottomStart = 50.dp,
                bottomEnd = 50.dp,
            ),
        )
    Column(
        Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Column(
            cardModifier,
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            UpperCardView(cardModifier, this@with, onSearchClick)
            MiscellaneousWeatherInfo(this@with)
        }
        WeatherForecast(
            listOf(
                hourlyWeatherForecast,
                dailyWeatherForecast,
            ),
        )
    }
}

@Composable
fun UpperCardView(
    modifier: Modifier,
    weatherReports: WeatherReportsDAO,
    onSearchClick: () -> Unit,
) = with(weatherReports) {
    Box(
        modifier
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        DarkerBlue,
                        DullBlue,
                    ),
                ),
                shape = RoundedCornerShape(
                    topStart = 0.dp,
                    topEnd = 0.dp,
                    bottomStart = 50.dp,
                    bottomEnd = 50.dp,
                ),
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 5.dp)
                .wrapContentHeight(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            SearchLocationView(city, onSearchClick)
            Text(
                text = if (scaleSwitchIndexState.value == 0) {
                    currentWeather.temperatureF
                } else {
                    currentWeather.temperatureC
                },
                color = Color.White,
                fontFamily = FontFamily(Font(R.font.handlee_regular)),
                fontSize = 120.sp,
                modifier = Modifier
                    .wrapContentSize(align = Alignment.Center)
                    .padding(0.dp),
            )
            Text(
                text = currentWeather.weatherDescription,
                color = FluorescentPink,
                fontFamily = FontFamily(Font(R.font.handlee_regular)),
                fontSize = 24.sp,
                modifier = Modifier.wrapContentSize(align = Alignment.Center),
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp)
                .wrapContentHeight(),
            contentAlignment = Alignment.TopEnd,
        ) { TemperatureSwitch(scaleSwitchIndexState) }
    }
}

@Composable
fun MiscellaneousWeatherInfo(state: WeatherReportsDAO) = with(state) {
    val widgets = listOf(
        Triple(R.drawable.ic_sunrise_sun, R.string.sunrise, sunriseTime),
        Triple(R.drawable.ic_wind_speed, R.string.wind_speed, currentWeather.windSpeed),
        Triple(R.drawable.ic_humidity, R.string.humidity, currentWeather.humidityLevel),
        Triple(R.drawable.ic_sunset_sun, R.string.sunset, sunsetTime),
    )
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Max)
            .padding(vertical = 20.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
    ) {
        widgets.forEachIndexed { idx, placeHolder ->
            WeatherMiscellaneousParamItem(placeHolder)
            if (idx != widgets.lastIndex) {
                TabRowDefaults.Divider(
                    color = DarkerBlue,
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(1.dp),
                )
            }
        }
    }
}

@Composable
fun WeatherForecast(listOfForecastsTypes: List<List<WeatherDAO>>) {
    val listOfTabs = listOf(
        Pair(R.drawable.ic_clock, "Hourly"),
        Pair(R.drawable.ic_calendar, "Weekly"),
    )
    var tabIndexState by remember { mutableIntStateOf(1) }
    Row(
        modifier = Modifier.wrapContentWidth(),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        VerticalTabs(listOfTabs, tabIndexState) { idx ->
            if (tabIndexState != idx) tabIndexState = idx
        }
        Spacer(
            modifier = Modifier
                .background(color = DarkerBlue)
                .width(1.dp)
                .padding(vertical = 180.dp)
                .height(IntrinsicSize.Max),
        )
        LazyColumn(
            contentPadding = PaddingValues(5.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            itemsIndexed(listOfForecastsTypes[tabIndexState]) { idx, item ->
                WeatherListItem(item, scaleSwitchIndexState)
                if (idx != listOfForecastsTypes[tabIndexState].lastIndex) {
                    TabRowDefaults.Divider(
                        color = DarkerBlue,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .padding(horizontal = 40.dp),
                    )
                }
            }
        }
    }
}
