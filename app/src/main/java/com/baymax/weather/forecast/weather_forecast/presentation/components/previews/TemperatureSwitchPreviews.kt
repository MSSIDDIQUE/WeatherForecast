package com.baymax.weather.forecast.weather_forecast.presentation.components.previews

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
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.baymax.launcherapp.ui.theme.DarkestBlue
import com.baymax.launcherapp.ui.theme.FluorescentPink
import com.baymax.weather.forecast.R
import com.baymax.weather.forecast.weather_forecast.presentation.components.TemperatureSwitch
import kotlinx.coroutines.delay

// Helper composable to show the TemperatureSwitch in a specific state for previews
@Composable
private fun TemperatureSwitchStaticState(
    selectedIndex: Int,
    isExpanded: Boolean
) {
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
                .wrapContentHeight()
                .background(
                    color = DarkestBlue,
                    shape = RoundedCornerShape(25.dp),
                ),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            // Always show the selected option
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
                    text = listOfScales[selectedIndex],
                    color = Color.White,
                    fontFamily = FontFamily(Font(R.font.handlee_regular)),
                    fontSize = 24.sp,
                    modifier = Modifier
                        .width(IntrinsicSize.Max)
                        .padding(5.dp),
                )
            }

            // Show the unselected option if expanded
            if (isExpanded) {
                val unselectedIndex = if (selectedIndex == 0) 1 else 0
                Box(
                    modifier = Modifier
                        .width(IntrinsicSize.Max)
                        .wrapContentHeight()
                        .background(
                            color = DarkestBlue,
                            shape = RoundedCornerShape(25.dp),
                        ),
                ) {
                    Text(
                        text = listOfScales[unselectedIndex],
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

// Animated preview that continuously cycles through states
@Composable
private fun TemperatureSwitchAnimatedDemo(initialSelection: Int) {
    val listOfScales = listOf("F", "C")
    var isExpanded by remember { mutableStateOf(false) }
    var selectedIndex by remember { mutableIntStateOf(initialSelection) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(1000) // Wait 1 second in collapsed state
            isExpanded = true
            delay(2000) // Wait 2 seconds in expanded state
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
                .clickable { isExpanded = !isExpanded },
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            // Always show the selected option
            Box(
                modifier = Modifier
                    .width(IntrinsicSize.Max)
                    .wrapContentHeight()
                    .background(
                        color = FluorescentPink,
                        shape = RoundedCornerShape(25.dp),
                    )
                    .clickable {
                        if (isExpanded) {
                            isExpanded = false
                        } else {
                            isExpanded = true
                        }
                    },
            ) {
                Text(
                    text = listOfScales[selectedIndex],
                    color = Color.White,
                    fontFamily = FontFamily(Font(R.font.handlee_regular)),
                    fontSize = 24.sp,
                    modifier = Modifier
                        .width(IntrinsicSize.Max)
                        .padding(5.dp),
                )
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
                val unselectedIndex = if (selectedIndex == 0) 1 else 0
                Box(
                    modifier = Modifier
                        .width(IntrinsicSize.Max)
                        .wrapContentHeight()
                        .background(
                            color = DarkestBlue,
                            shape = RoundedCornerShape(25.dp),
                        )
                        .clickable {
                            selectedIndex = unselectedIndex
                            isExpanded = false
                        },
                ) {
                    Text(
                        text = listOfScales[unselectedIndex],
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

// ============ Collapsed State Previews ============

@Preview(
    name = "Collapsed - Fahrenheit Selected",
    showBackground = true,
    backgroundColor = 0xFF001f3f
)
@Composable
fun TemperatureSwitchCollapsedFahrenheitPreview() {
    Box(
        modifier = Modifier
            .background(Color(0xFF001f3f))
            .padding(16.dp)
    ) {
        TemperatureSwitchStaticState(
            selectedIndex = 0,
            isExpanded = false
        )
    }
}

@Preview(
    name = "Collapsed - Celsius Selected",
    showBackground = true,
    backgroundColor = 0xFF001f3f
)
@Composable
fun TemperatureSwitchCollapsedCelsiusPreview() {
    Box(
        modifier = Modifier
            .background(Color(0xFF001f3f))
            .padding(16.dp)
    ) {
        TemperatureSwitchStaticState(
            selectedIndex = 1,
            isExpanded = false
        )
    }
}

// ============ Expanded State Previews ============

@Preview(
    name = "Expanded - Fahrenheit Selected",
    showBackground = true,
    backgroundColor = 0xFF001f3f
)
@Composable
fun TemperatureSwitchExpandedFahrenheitPreview() {
    Box(
        modifier = Modifier
            .background(Color(0xFF001f3f))
            .padding(16.dp)
    ) {
        TemperatureSwitchStaticState(
            selectedIndex = 0,
            isExpanded = true
        )
    }
}

@Preview(
    name = "Expanded - Celsius Selected",
    showBackground = true,
    backgroundColor = 0xFF001f3f
)
@Composable
fun TemperatureSwitchExpandedCelsiusPreview() {
    Box(
        modifier = Modifier
            .background(Color(0xFF001f3f))
            .padding(16.dp)
    ) {
        TemperatureSwitchStaticState(
            selectedIndex = 1,
            isExpanded = true
        )
    }
}

// ============ Animated Previews ============

@Preview(
    name = "Animation Demo - Fahrenheit Selected",
    showBackground = true,
    backgroundColor = 0xFF001f3f
)
@Composable
fun TemperatureSwitchAnimatedFahrenheitPreview() {
    Box(
        modifier = Modifier
            .background(Color(0xFF001f3f))
            .padding(16.dp)
    ) {
        TemperatureSwitchAnimatedDemo(initialSelection = 0)
    }
}

@Preview(
    name = "Animation Demo - Celsius Selected",
    showBackground = true,
    backgroundColor = 0xFF001f3f
)
@Composable
fun TemperatureSwitchAnimatedCelsiusPreview() {
    Box(
        modifier = Modifier
            .background(Color(0xFF001f3f))
            .padding(16.dp)
    ) {
        TemperatureSwitchAnimatedDemo(initialSelection = 1)
    }
}

// ============ Interactive Preview (Standard Component) ============

@Preview(
    name = "Interactive - Fahrenheit",
    showBackground = true,
    backgroundColor = 0xFF001f3f
)
@Composable
fun TemperatureSwitchInteractiveFahrenheitPreview() {
    val scaleSwitchState = remember { mutableIntStateOf(0) }
    Box(
        modifier = Modifier
            .background(Color(0xFF001f3f))
            .padding(16.dp)
    ) {
        TemperatureSwitch(scaleSwitchIndexState = scaleSwitchState)
    }
}

@Preview(
    name = "Interactive - Celsius",
    showBackground = true,
    backgroundColor = 0xFF001f3f
)
@Composable
fun TemperatureSwitchInteractiveCelsiusPreview() {
    val scaleSwitchState = remember { mutableIntStateOf(1) }
    Box(
        modifier = Modifier
            .background(Color(0xFF001f3f))
            .padding(16.dp)
    ) {
        TemperatureSwitch(scaleSwitchIndexState = scaleSwitchState)
    }
}
