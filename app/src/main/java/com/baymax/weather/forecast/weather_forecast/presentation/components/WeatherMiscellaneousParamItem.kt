package com.baymax.weather.forecast.weather_forecast.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.baymax.launcherapp.ui.theme.DullBlue
import com.baymax.launcherapp.ui.theme.FluorescentPink
import com.baymax.weather.forecast.R

@Composable
fun WeatherMiscellaneousParamItem(params: Triple<Int, Int, String>) {
    Column(
        modifier = Modifier.wrapContentSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(
            modifier = Modifier.size(40.dp),
            painter = painterResource(params.first),
            contentDescription = stringResource(params.second),
            tint = DullBlue,
        )
        Text(
            text = stringResource(params.second),
            color = DullBlue,
            fontFamily = FontFamily(Font(R.font.handlee_regular)),
            fontSize = 15.sp,
            modifier = Modifier.wrapContentSize(align = Alignment.Center),
        )
        Text(
            text = params.third,
            color = FluorescentPink,
            fontFamily = FontFamily(Font(R.font.handlee_regular)),
            fontSize = 16.sp,
            modifier = Modifier.wrapContentSize(align = Alignment.Center),
        )
    }
}