package com.baymax.weather.forecast.weather_forecast.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.baymax.launcherapp.ui.theme.DarkestBlue
import com.baymax.launcherapp.ui.theme.FluorescentPink
import com.baymax.weather.forecast.R

@Composable
fun TemperatureSwitch(scaleSwitchIndexState: MutableState<Int>) {
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