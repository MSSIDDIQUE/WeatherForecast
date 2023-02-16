package com.baymax.weather.forecast.weather_forecast.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.TabRowDefaults
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CalendarMonth
import androidx.compose.material.icons.rounded.HourglassBottom
import androidx.compose.material.icons.rounded.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.baymax.launcherapp.ui.theme.DarkBlue
import com.baymax.launcherapp.ui.theme.DarkerBlue
import com.baymax.launcherapp.ui.theme.DarkestBlue
import com.baymax.launcherapp.ui.theme.DullBlue
import com.baymax.launcherapp.ui.theme.FluorescentPink
import com.baymax.launcherapp.ui.theme.JetBlack
import com.baymax.weather.forecast.R
import com.baymax.weather.forecast.weather_forecast.presentation.model.WeatherDAO
import com.baymax.weather.forecast.weather_forecast.presentation.model.WeatherReportsDAO

private val scaleSwitchIndexState = mutableStateOf(1)

@Composable
fun WeatherReportsScreen(
    weatherReports: WeatherReportsDAO,
    onSearchClick: () -> Unit,
) = with(weatherReports) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        DarkBlue,
                        DarkestBlue,
                    ),
                ),
            ),
    ) {
        Column(
            Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                elevation = 4.dp,
                shape = RoundedCornerShape(
                    topStart = 0.dp,
                    topEnd = 0.dp,
                    bottomStart = 50.dp,
                    bottomEnd = 50.dp,
                ),
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    JetBlack,
                                    DarkerBlue,
                                ),
                            ),
                        ),
                    Alignment.TopCenter,
                ) {
                    Column(
                        Modifier
                            .fillMaxWidth()
                            .wrapContentHeight(),
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        UpperCardView(this@with, onSearchClick)
                        LowerCardView(this@with)
                    }
                }
            }
            WeatherForecast(
                listOf(
                    hourlyWeatherForecast,
                    dailyWeatherForecast,
                ),
            )
        }
    }
}

@Composable
fun UpperCardView(
    weatherReports: WeatherReportsDAO,
    onSearchClick: () -> Unit,
) = with(weatherReports) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        elevation = 4.dp,
        shape = RoundedCornerShape(
            topStart = 0.dp,
            topEnd = 0.dp,
            bottomStart = 50.dp,
            bottomEnd = 50.dp,
        ),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            DarkBlue,
                            DullBlue,
                        ),
                    ),
                ),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                contentAlignment = Alignment.TopCenter,
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 5.dp)
                        .wrapContentHeight(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    CurrentWeatherLocationView(city, onSearchClick)
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
            }
            Box(
                modifier = Modifier.fillMaxWidth()
                    .padding(5.dp)
                    .wrapContentHeight(),
                contentAlignment = Alignment.TopEnd,
            ) { TemperatureScaleTypeView() }
        }
    }
}

@Composable
fun LowerCardView(
    state: WeatherReportsDAO,
) = with(state) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Max)
            .padding(vertical = 20.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
    ) {
        val widgets = listOf(
            Pair(R.drawable.ic_sunrise_sun, R.string.sunrise),
            Pair(R.drawable.ic_wind_speed, R.string.wind_speed),
            Pair(R.drawable.ic_humidity, R.string.humidity),
            Pair(R.drawable.ic_sunset_sun, R.string.sunset),
        )
        widgets.forEachIndexed { idx, placeHolder ->
            val value = when (placeHolder.second) {
                R.string.sunrise -> sunriseTime
                R.string.wind_speed -> currentWeather.windSpeed
                R.string.humidity -> currentWeather.humidityLevel
                R.string.sunset -> sunsetTime
                else -> ""
            }
            ExtraWeatherDetailsView(placeHolder, value)
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
fun CurrentWeatherLocationView(
    city: String,
    onSearchClick: () -> Unit,
) {
    Row(
        modifier = Modifier.width(IntrinsicSize.Max)
            .wrapContentHeight()
            .background(
                color = DarkestBlue,
                shape = RoundedCornerShape(25.dp),
            ).clickable {
                onSearchClick()
            },
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(
            modifier = Modifier
                .wrapContentWidth()
                .wrapContentHeight()
                .background(
                    color = DarkestBlue,
                    shape = RoundedCornerShape(25.dp),
                )
                .padding(horizontal = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_location_pin),
                contentDescription = stringResource(R.string.location),
                modifier = Modifier.padding(5.dp),
                tint = FluorescentPink,
            )
            Text(
                text = city,
                color = Color.White,
                fontFamily = FontFamily(Font(R.font.handlee_regular)),
                fontSize = 24.sp,
                style = TextStyle(
                    textDecoration = TextDecoration.Underline,
                ),
                modifier = Modifier.padding(horizontal = 5.dp),
            )
        }
        Row(
            modifier = Modifier
                .wrapContentWidth()
                .wrapContentHeight()
                .background(
                    color = JetBlack,
                    shape = RoundedCornerShape(25.dp),
                )
                .padding(6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            Icon(
                Icons.Rounded.Search,
                contentDescription = stringResource(R.string.search),
                tint = FluorescentPink,
            )
        }
    }
}

@Composable
fun ExtraWeatherDetailsView(
    widgetPlaceholder: Pair<Int, Int>,
    widgetValue: String,
) {
    Column(
        modifier = Modifier.wrapContentSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(
            modifier = Modifier.size(40.dp),
            painter = painterResource(widgetPlaceholder.first),
            contentDescription = stringResource(widgetPlaceholder.second),
            tint = DullBlue,
        )
        Text(
            text = stringResource(widgetPlaceholder.second),
            color = DullBlue,
            fontFamily = FontFamily(Font(R.font.handlee_regular)),
            fontSize = 15.sp,
            modifier = Modifier.wrapContentSize(align = Alignment.Center),
        )
        Text(
            text = widgetValue,
            color = FluorescentPink,
            fontFamily = FontFamily(Font(R.font.handlee_regular)),
            fontSize = 16.sp,
            modifier = Modifier.wrapContentSize(align = Alignment.Center),
        )
    }
}

@Composable
fun TemperatureScaleTypeView() {
    val listOfScales = listOf("F", "C")
    Row(
        modifier = Modifier.wrapContentSize(),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.SpaceAround,
    ) {
        Text(
            text = "°",
            color = Color.White,
            fontFamily = FontFamily(Font(R.font.handlee_regular)),
            fontSize = 24.sp,
            modifier = Modifier
                .width(IntrinsicSize.Max)
                .padding(5.dp)
                .align(Alignment.Top),
        )
        Column(
            modifier = Modifier
                .wrapContentWidth()
                .height(IntrinsicSize.Min)
                .background(
                    color = DarkestBlue,
                    shape = RoundedCornerShape(25.dp),
                ),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            listOfScales.forEachIndexed { idx, value ->
                Box(
                    modifier = Modifier
                        .width(IntrinsicSize.Max)
                        .wrapContentHeight()
                        .background(
                            color = if (scaleSwitchIndexState.value == idx) {
                                FluorescentPink
                            } else {
                                DarkestBlue
                            },
                            shape = RoundedCornerShape(25.dp),
                        )
                        .clickable {
                            if (scaleSwitchIndexState.value != idx) {
                                scaleSwitchIndexState.value = idx
                            }
                        },
                ) {
                    Text(
                        text = value,
                        color = Color.White,
                        fontFamily = FontFamily(Font(R.font.handlee_regular)),
                        fontSize = 24.sp,
                        modifier = Modifier
                            .width(IntrinsicSize.Max)
                            .padding(5.dp),
                    )
                }
            }
        }
    }
}

@Composable
fun WeatherForecast(listOfForecastsTypes: List<List<WeatherDAO>>) {
    val listOfTabs = listOf(
        Pair(Icons.Rounded.HourglassBottom, "Hourly"),
        Pair(Icons.Rounded.CalendarMonth, "Weekly"),
    )
    var tabIndexState by remember { mutableStateOf(1) }
    Column(
        modifier = Modifier.fillMaxWidth()
            .wrapContentHeight()
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        Row(
            modifier = Modifier
                .wrapContentWidth()
                .padding(vertical = 5.dp, horizontal = 10.dp)
                .height(IntrinsicSize.Min)
                .background(
                    color = DarkestBlue,
                    shape = RoundedCornerShape(12.dp),
                ),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            listOfTabs.forEachIndexed { idx, value ->
                Box(
                    modifier = Modifier
                        .width(IntrinsicSize.Max)
                        .wrapContentHeight()
                        .background(
                            color = if (tabIndexState == idx) {
                                DullBlue
                            } else {
                                DarkestBlue
                            },
                            shape = RoundedCornerShape(12.dp),
                        )
                        .padding(5.dp)
                        .clickable {
                            if (tabIndexState != idx) tabIndexState = idx
                        },
                ) {
                    Row(
                        modifier = Modifier.wrapContentSize().padding(horizontal = 15.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(
                            value.first,
                            contentDescription = "Hourly",
                            tint = Color.White,
                        )
                        Text(
                            text = value.second,
                            color = Color.White,
                            fontFamily = FontFamily(Font(R.font.handlee_regular)),
                            fontSize = 20.sp,
                            modifier = Modifier
                                .width(IntrinsicSize.Max)
                                .padding(5.dp),
                        )
                    }
                }
            }
        }
        LazyColumn(
            contentPadding = PaddingValues(5.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            itemsIndexed(listOfForecastsTypes[tabIndexState]) { idx, item ->
                ForecastListItemView(item)
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

@Composable
fun ForecastListItemView(item: WeatherDAO) {
    Row(
        Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Image(
            modifier = Modifier.size(60.dp),
            painter = rememberAsyncImagePainter(item.iconLarge),
            contentDescription = item.weatherDescription,
        )
        Text(
            modifier = Modifier.wrapContentSize()
                .padding(horizontal = 5.dp),
            text = item.dateTime,
            color = Color.White,
            fontFamily = FontFamily(Font(R.font.handlee_regular)),
            fontSize = 16.sp,
            textAlign = TextAlign.Center,
        )
        Text(
            modifier = Modifier.wrapContentSize()
                .padding(horizontal = 5.dp),
            text = item.weatherDescription,
            color = FluorescentPink,
            fontFamily = FontFamily(Font(R.font.handlee_regular)),
            fontSize = 20.sp,
            maxLines = 2,
        )
        Text(
            modifier = Modifier.wrapContentSize()
                .padding(horizontal = 5.dp),
            text = if (scaleSwitchIndexState.value == 0) item.temperatureF else item.temperatureC,
            color = Color.White,
            fontFamily = FontFamily(Font(R.font.handlee_regular)),
            fontSize = 22.sp,
            maxLines = 1,
        )
    }
}
