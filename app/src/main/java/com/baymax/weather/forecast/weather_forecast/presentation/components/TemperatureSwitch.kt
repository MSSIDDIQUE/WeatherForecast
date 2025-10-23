package com.baymax.weather.forecast.weather_forecast.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import kotlinx.coroutines.delay

@Composable
fun TemperatureSwitch(scaleSwitchIndexState: MutableState<Int>) {
    val listOfScales = listOf("F", "C")
    var isExpanded by remember { mutableStateOf(false) }

    // Auto-collapse after 3 seconds when expanded
    LaunchedEffect(isExpanded) {
        if (isExpanded) {
            delay(3000)
            isExpanded = false
        }
    }

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
                .wrapContentHeight()
                .background(
                    color = DarkestBlue,
                    shape = RoundedCornerShape(25.dp),
                )
                .clickable {
                    isExpanded = !isExpanded
                },
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            // Always show the selected option
            listOfScales.forEachIndexed { idx, value ->
                if (idx == scaleSwitchIndexState.value) {
                    Box(
                        modifier = Modifier
                            .width(IntrinsicSize.Max)
                            .wrapContentHeight()
                            .background(
                                color = FluorescentPink,
                                shape = RoundedCornerShape(25.dp),
                            ),
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

            // Show the unselected option when expanded
            AnimatedVisibility(
                visible = isExpanded,
                enter = expandVertically(
                    animationSpec = tween(300),
                    expandFrom = Alignment.Top
                ) + fadeIn(animationSpec = tween(300)),
                exit = shrinkVertically(
                    animationSpec = tween(300),
                    shrinkTowards = Alignment.Top
                ) + fadeOut(animationSpec = tween(300))
            ) {
                listOfScales.forEachIndexed { idx, value ->
                    if (idx != scaleSwitchIndexState.value) {
                        Box(
                            modifier = Modifier
                                .width(IntrinsicSize.Max)
                                .wrapContentHeight()
                                .background(
                                    color = DarkestBlue,
                                    shape = RoundedCornerShape(25.dp),
                                )
                                .clickable {
                                    scaleSwitchIndexState.value = idx
                                    isExpanded = false
                                },
                        ) {
                            Text(
                                text = value,
                                color = Color.White.copy(alpha = 0.6f),
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
    }
}