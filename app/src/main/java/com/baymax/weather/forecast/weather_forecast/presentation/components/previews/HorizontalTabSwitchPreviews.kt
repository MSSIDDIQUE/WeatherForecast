package com.baymax.weather.forecast.weather_forecast.presentation.components.previews

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.baymax.launcherapp.ui.theme.JetBlack
import com.baymax.weather.forecast.R
import com.baymax.weather.forecast.weather_forecast.presentation.components.HorizontalTabSwitch
import kotlinx.coroutines.delay

@Preview(name = "Hourly Selected", showBackground = true)
@Composable
private fun HorizontalTabSwitchPreview_HourlySelected() {
    val listOfTabs = listOf(
        Pair(R.drawable.ic_clock, "Hourly"),
        Pair(R.drawable.ic_calendar, "Weekly"),
    )
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(JetBlack)
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HorizontalTabSwitch(listOfTabs, 0) { }
    }
}

@Preview(name = "Weekly Selected", showBackground = true)
@Composable
private fun HorizontalTabSwitchPreview_WeeklySelected() {
    val listOfTabs = listOf(
        Pair(R.drawable.ic_clock, "Hourly"),
        Pair(R.drawable.ic_calendar, "Weekly"),
    )
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(JetBlack)
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HorizontalTabSwitch(listOfTabs, 1) { }
    }
}

@Preview(name = "Animated Demo", showBackground = true)
@Composable
private fun HorizontalTabSwitchPreview_AnimatedDemo() {
    val listOfTabs = listOf(
        Pair(R.drawable.ic_clock, "Hourly"),
        Pair(R.drawable.ic_calendar, "Weekly"),
    )
    var selectedIndex by remember { mutableIntStateOf(0) }
    
    LaunchedEffect(Unit) {
        while (true) {
            delay(2000)
            selectedIndex = if (selectedIndex == 0) 1 else 0
        }
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(JetBlack)
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HorizontalTabSwitch(listOfTabs, selectedIndex) { idx ->
            selectedIndex = idx
        }
    }
}

@Preview(name = "Interactive", showBackground = true)
@Composable
private fun HorizontalTabSwitchPreview_Interactive() {
    val listOfTabs = listOf(
        Pair(R.drawable.ic_clock, "Hourly"),
        Pair(R.drawable.ic_calendar, "Weekly"),
    )
    var selectedIndex by remember { mutableIntStateOf(0) }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(JetBlack)
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HorizontalTabSwitch(listOfTabs, selectedIndex) { idx ->
            selectedIndex = idx
        }
    }
}

