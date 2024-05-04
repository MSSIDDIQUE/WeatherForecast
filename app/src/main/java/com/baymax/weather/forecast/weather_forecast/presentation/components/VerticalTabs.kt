package com.baymax.weather.forecast.weather_forecast.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.baymax.launcherapp.ui.theme.FluorescentPink
import com.baymax.weather.forecast.R
import com.baymax.weather.forecast.weather_forecast.presentation.utils.UiUtils.vertical

@Composable
fun VerticalTabs(
    listOfTabs: List<Pair<Int, String>>,
    tabIndexState: Int,
    onTabClick: (Int) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .wrapContentWidth()
            .height(IntrinsicSize.Max)
            .padding(horizontal = 8.dp)
    ) {
        listOfTabs.forEachIndexed { idx, value ->
            Column(
                modifier = Modifier
                    .wrapContentSize()
                    .clickable { onTabClick(idx) },
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Text(
                    modifier = Modifier
                        .vertical(true)
                        .rotate(-90f)
                        .wrapContentSize(),
                    text = value.second,
                    color = if (tabIndexState == idx) FluorescentPink else Color.White,
                    fontFamily = FontFamily(Font(R.font.handlee_regular)),
                    fontSize = 20.sp,
                )
                Icon(
                    painterResource(value.first),
                    contentDescription = "Hourly",
                    tint = if (tabIndexState == idx) FluorescentPink else Color.White,
                    modifier = Modifier
                        .vertical(true)
                        .rotate(-90f)
                )
            }
        }
    }
}