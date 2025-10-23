package com.baymax.weather.forecast.weather_forecast.presentation.components.previews

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.baymax.weather.forecast.R
import com.baymax.weather.forecast.weather_forecast.presentation.components.VerticalTabs

@Preview(
    name = "Vertical Tabs - First Tab Selected (Hourly)",
    showBackground = true,
    backgroundColor = 0xFF000000,
    heightDp = 400
)
@Composable
fun VerticalTabsHourlySelectedPreview() {
    Box(
        modifier = Modifier
            .background(Color.Black)
            .padding(16.dp)
    ) {
        VerticalTabs(
            listOfTabs = listOf(
                Pair(R.drawable.ic_clock, "Hourly"),
                Pair(R.drawable.ic_calendar, "Weekly"),
            ),
            tabIndexState = 0,
            onTabClick = {}
        )
    }
}

@Preview(
    name = "Vertical Tabs - Second Tab Selected (Weekly)",
    showBackground = true,
    backgroundColor = 0xFF000000,
    heightDp = 400
)
@Composable
fun VerticalTabsWeeklySelectedPreview() {
    Box(
        modifier = Modifier
            .background(Color.Black)
            .padding(16.dp)
    ) {
        VerticalTabs(
            listOfTabs = listOf(
                Pair(R.drawable.ic_clock, "Hourly"),
                Pair(R.drawable.ic_calendar, "Weekly"),
            ),
            tabIndexState = 1,
            onTabClick = {}
        )
    }
}

@Preview(
    name = "Vertical Tabs - Three Tabs (First Selected)",
    showBackground = true,
    backgroundColor = 0xFF000000,
    heightDp = 500
)
@Composable
fun VerticalTabsThreeTabsPreview() {
    Box(
        modifier = Modifier
            .background(Color.Black)
            .padding(16.dp)
    ) {
        VerticalTabs(
            listOfTabs = listOf(
                Pair(R.drawable.ic_clock, "Hourly"),
                Pair(R.drawable.ic_calendar, "Daily"),
                Pair(R.drawable.ic_calendar, "Monthly"),
            ),
            tabIndexState = 0,
            onTabClick = {}
        )
    }
}

@Preview(
    name = "Vertical Tabs - Three Tabs (Middle Selected)",
    showBackground = true,
    backgroundColor = 0xFF000000,
    heightDp = 500
)
@Composable
fun VerticalTabsThreeTabsMiddleSelectedPreview() {
    Box(
        modifier = Modifier
            .background(Color.Black)
            .padding(16.dp)
    ) {
        VerticalTabs(
            listOfTabs = listOf(
                Pair(R.drawable.ic_clock, "Hourly"),
                Pair(R.drawable.ic_calendar, "Daily"),
                Pair(R.drawable.ic_calendar, "Monthly"),
            ),
            tabIndexState = 1,
            onTabClick = {}
        )
    }
}

