package com.baymax.weather.forecast.weather_forecast.presentation.components.previews

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.baymax.launcherapp.ui.theme.DarkestBlue
import com.baymax.launcherapp.ui.theme.FluorescentPink
import com.baymax.launcherapp.ui.theme.JetBlack
import com.baymax.weather.forecast.R
import com.baymax.weather.forecast.weather_forecast.presentation.components.SearchLocationView
import kotlinx.coroutines.delay

// Helper composable to show the SearchLocationView in a specific state for previews
@Composable
private fun SearchLocationViewStaticState(
    city: String,
    isExpanded: Boolean
) {
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
        
        if (isExpanded) {
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
                        .padding(6.dp),
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

// Animated preview that continuously cycles through states
@Composable
private fun SearchLocationViewAnimatedDemo(city: String) {
    var isExpanded by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(1000) // Wait 1 second in collapsed state
            isExpanded = true
            delay(2000) // Wait 2 seconds in expanded state
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
            .clickable { isExpanded = !isExpanded }
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
                        .padding(6.dp),
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

// ============ Collapsed State Previews ============

@Preview(
    name = "Collapsed - Short City",
    showBackground = true,
    backgroundColor = 0xFF001f3f
)
@Composable
fun SearchLocationViewCollapsedShortPreview() {
    Box(
        modifier = Modifier
            .background(Color(0xFF001f3f))
            .padding(16.dp)
    ) {
        SearchLocationViewStaticState(
            city = "Tokyo",
            isExpanded = false
        )
    }
}

@Preview(
    name = "Collapsed - Medium City",
    showBackground = true,
    backgroundColor = 0xFF001f3f
)
@Composable
fun SearchLocationViewCollapsedMediumPreview() {
    Box(
        modifier = Modifier
            .background(Color(0xFF001f3f))
            .padding(16.dp)
    ) {
        SearchLocationViewStaticState(
            city = "New York",
            isExpanded = false
        )
    }
}

@Preview(
    name = "Collapsed - Long City",
    showBackground = true,
    backgroundColor = 0xFF001f3f
)
@Composable
fun SearchLocationViewCollapsedLongPreview() {
    Box(
        modifier = Modifier
            .background(Color(0xFF001f3f))
            .padding(16.dp)
    ) {
        SearchLocationViewStaticState(
            city = "San Francisco",
            isExpanded = false
        )
    }
}

// ============ Expanded State Previews ============

@Preview(
    name = "Expanded - Short City",
    showBackground = true,
    backgroundColor = 0xFF001f3f
)
@Composable
fun SearchLocationViewExpandedShortPreview() {
    Box(
        modifier = Modifier
            .background(Color(0xFF001f3f))
            .padding(16.dp)
    ) {
        SearchLocationViewStaticState(
            city = "Tokyo",
            isExpanded = true
        )
    }
}

@Preview(
    name = "Expanded - Medium City",
    showBackground = true,
    backgroundColor = 0xFF001f3f
)
@Composable
fun SearchLocationViewExpandedMediumPreview() {
    Box(
        modifier = Modifier
            .background(Color(0xFF001f3f))
            .padding(16.dp)
    ) {
        SearchLocationViewStaticState(
            city = "New York",
            isExpanded = true
        )
    }
}

@Preview(
    name = "Expanded - Long City",
    showBackground = true,
    backgroundColor = 0xFF001f3f
)
@Composable
fun SearchLocationViewExpandedLongPreview() {
    Box(
        modifier = Modifier
            .background(Color(0xFF001f3f))
            .padding(16.dp)
    ) {
        SearchLocationViewStaticState(
            city = "San Francisco",
            isExpanded = true
        )
    }
}

@Preview(
    name = "Expanded - Very Long City",
    showBackground = true,
    backgroundColor = 0xFF001f3f
)
@Composable
fun SearchLocationViewExpandedVeryLongPreview() {
    Box(
        modifier = Modifier
            .background(Color(0xFF001f3f))
            .padding(16.dp)
    ) {
        SearchLocationViewStaticState(
            city = "São Paulo, Brazil",
            isExpanded = true
        )
    }
}

@Preview(
    name = "Expanded - International Characters",
    showBackground = true,
    backgroundColor = 0xFF001f3f
)
@Composable
fun SearchLocationViewExpandedInternationalPreview() {
    Box(
        modifier = Modifier
            .background(Color(0xFF001f3f))
            .padding(16.dp)
    ) {
        SearchLocationViewStaticState(
            city = "München",
            isExpanded = true
        )
    }
}

// ============ Animated Previews ============

@Preview(
    name = "Animation Demo - Short City",
    showBackground = true,
    backgroundColor = 0xFF001f3f
)
@Composable
fun SearchLocationViewAnimatedShortPreview() {
    Box(
        modifier = Modifier
            .background(Color(0xFF001f3f))
            .padding(16.dp)
    ) {
        SearchLocationViewAnimatedDemo(city = "Tokyo")
    }
}

@Preview(
    name = "Animation Demo - Medium City",
    showBackground = true,
    backgroundColor = 0xFF001f3f
)
@Composable
fun SearchLocationViewAnimatedMediumPreview() {
    Box(
        modifier = Modifier
            .background(Color(0xFF001f3f))
            .padding(16.dp)
    ) {
        SearchLocationViewAnimatedDemo(city = "New York")
    }
}

@Preview(
    name = "Animation Demo - Long City",
    showBackground = true,
    backgroundColor = 0xFF001f3f
)
@Composable
fun SearchLocationViewAnimatedLongPreview() {
    Box(
        modifier = Modifier
            .background(Color(0xFF001f3f))
            .padding(16.dp)
    ) {
        SearchLocationViewAnimatedDemo(city = "San Francisco")
    }
}

@Preview(
    name = "Animation Demo - Very Long City",
    showBackground = true,
    backgroundColor = 0xFF001f3f
)
@Composable
fun SearchLocationViewAnimatedVeryLongPreview() {
    Box(
        modifier = Modifier
            .background(Color(0xFF001f3f))
            .padding(16.dp)
    ) {
        SearchLocationViewAnimatedDemo(city = "São Paulo, Brazil")
    }
}

// ============ Interactive Preview (Standard Component) ============

@Preview(
    name = "Interactive - Click to Expand",
    showBackground = true,
    backgroundColor = 0xFF001f3f
)
@Composable
fun SearchLocationViewInteractivePreview() {
    Box(
        modifier = Modifier
            .background(Color(0xFF001f3f))
            .padding(16.dp)
    ) {
        SearchLocationView(
            city = "San Francisco",
            onSearchClick = {}
        )
    }
}
