package com.baymax.weather.forecast.weather_forecast.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.baymax.launcherapp.ui.theme.FluorescentPink
import com.baymax.weather.forecast.R
import com.baymax.weather.forecast.weather_forecast.presentation.model.WeatherDAO

@Composable
fun WeatherListItem(item: WeatherDAO, scaleSwitchIndexState: MutableState<Int>) {
    Row(
        Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly,
    ) {
        Image(
            modifier = Modifier.size(60.dp),
            painter = rememberAsyncImagePainter(item.iconLarge),
            contentDescription = item.weatherDescription,
        )
        Text(
            modifier = Modifier
                .wrapContentSize()
                .padding(horizontal = 5.dp),
            text = item.dateTime,
            color = Color.White,
            fontFamily = FontFamily(Font(R.font.handlee_regular)),
            fontSize = 16.sp,
            textAlign = TextAlign.Center,
        )
        Text(
            modifier = Modifier
                .width(150.dp)
                .wrapContentHeight()
                .padding(horizontal = 5.dp),
            text = item.weatherDescription,
            color = FluorescentPink,
            fontFamily = FontFamily(Font(R.font.handlee_regular)),
            fontSize = 20.sp,
            maxLines = 2,
            textAlign = TextAlign.Center
        )
        Text(
            modifier = Modifier
                .wrapContentSize()
                .padding(horizontal = 5.dp),
            text = if (scaleSwitchIndexState.value == 0) item.temperatureF else item.temperatureC,
            color = Color.White,
            fontFamily = FontFamily(Font(R.font.handlee_regular)),
            fontSize = 22.sp,
            maxLines = 1,
        )
    }
}