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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.baymax.launcherapp.ui.theme.DarkestBlue
import com.baymax.launcherapp.ui.theme.FluorescentPink
import com.baymax.launcherapp.ui.theme.JetBlack
import com.baymax.weather.forecast.R
import kotlinx.coroutines.delay

@Composable
fun SearchLocationView(
    city: String,
    onSearchClick: () -> Unit,
) {
    var isExpanded by remember { mutableStateOf(false) }

    // Auto-collapse after 2 seconds when expanded
    LaunchedEffect(isExpanded) {
        if (isExpanded) {
            delay(2000)
            isExpanded = false
        }
    }

    Row(
        modifier = Modifier
            .wrapContentWidth()
            .wrapContentHeight()
            .background(
                color = DarkestBlue,
                shape = RoundedCornerShape(
                    topStart = 0.dp,
                    topEnd = 25.dp,
                    bottomStart = 0.dp,
                    bottomEnd = 25.dp
                ),
            )
            .clickable {
                if (isExpanded) {
                    onSearchClick()
                } else {
                    isExpanded = true
                }
            }
            .padding(horizontal = 10.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_location_pin),
            contentDescription = stringResource(R.string.location),
            modifier = Modifier.padding(5.dp),
            tint = FluorescentPink,
        )
        
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
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
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
                Row(
                    modifier = Modifier
                        .wrapContentWidth()
                        .wrapContentHeight()
                        .background(
                            color = JetBlack,
                            shape = RoundedCornerShape(25.dp),
                        )
                        .padding(3.dp),
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
    }
}
