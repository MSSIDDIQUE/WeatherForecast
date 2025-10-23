package com.baymax.weather.forecast.weather_forecast.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.baymax.launcherapp.ui.theme.DarkestBlue
import com.baymax.launcherapp.ui.theme.FluorescentPink
import com.baymax.weather.forecast.R
import kotlinx.coroutines.delay

@Composable
fun HorizontalTabSwitch(
    listOfTabs: List<Pair<Int, String>>,
    tabIndexState: Int,
    onTabClick: (Int) -> Unit
) {
    var isExpanded by remember { mutableStateOf(false) }

    // Auto-collapse after 3 seconds when expanded
    LaunchedEffect(isExpanded) {
        if (isExpanded) {
            delay(3000)
            isExpanded = false
        }
    }

    Row(
        modifier = Modifier
            .wrapContentWidth()
            .wrapContentHeight()
            .background(
                color = DarkestBlue,
                shape = RoundedCornerShape(25.dp),
            )
            .clickable {
                isExpanded = !isExpanded
            }
            .padding(horizontal = 10.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // Show the selected tab's icon only when collapsed
        AnimatedVisibility(
            visible = !isExpanded,
            enter = fadeIn(animationSpec = tween(300)),
            exit = fadeOut(animationSpec = tween(300))
        ) {
            listOfTabs.forEachIndexed { idx, value ->
                if (idx == tabIndexState) {
                    Icon(
                        painter = painterResource(value.first),
                        contentDescription = value.second,
                        tint = FluorescentPink,
                        modifier = Modifier.padding(5.dp)
                    )
                }
            }
        }

        // Show all tabs when expanded
        AnimatedVisibility(
            visible = isExpanded,
            enter = expandHorizontally(
                animationSpec = tween(300),
                expandFrom = Alignment.Start
            ) + fadeIn(animationSpec = tween(300)),
            exit = shrinkHorizontally(
                animationSpec = tween(300),
                shrinkTowards = Alignment.Start
            ) + fadeOut(animationSpec = tween(300))
        ) {
            Row(
                modifier = Modifier.wrapContentWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                listOfTabs.forEachIndexed { idx, value ->
                    val isSelected = tabIndexState == idx
                    
                    Row(
                        modifier = Modifier
                            .wrapContentSize()
                            .background(
                                color = if (isSelected) FluorescentPink else DarkestBlue,
                                shape = RoundedCornerShape(20.dp),
                            )
                            .clickable {
                                if (tabIndexState != idx) {
                                    onTabClick(idx)
                                    isExpanded = false
                                }
                            }
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            painter = painterResource(value.first),
                            contentDescription = value.second,
                            tint = if (isSelected) Color.White else Color.White.copy(alpha = 0.6f),
                            modifier = Modifier.wrapContentSize()
                        )
                        Text(
                            text = value.second,
                            color = if (isSelected) Color.White else Color.White.copy(alpha = 0.6f),
                            fontFamily = FontFamily(Font(R.font.handlee_regular)),
                            fontSize = 16.sp,
                        )
                    }
                }
            }
        }
    }
}

