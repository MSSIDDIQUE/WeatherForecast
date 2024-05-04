package com.baymax.weather.forecast.weather_forecast.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.runtime.Composable
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

@Composable
fun SearchLocationView(
    city: String,
    onSearchClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .width(IntrinsicSize.Max)
            .wrapContentHeight()
            .background(
                color = DarkestBlue,
                shape = RoundedCornerShape(25.dp),
            )
            .clickable {
                onSearchClick()
            },
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(
            modifier = Modifier
                .wrapContentWidth()
                .wrapContentHeight()
                .background(
                    color = DarkestBlue,
                    shape = RoundedCornerShape(25.dp),
                )
                .padding(horizontal = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_location_pin),
                contentDescription = stringResource(R.string.location),
                modifier = Modifier.padding(5.dp),
                tint = FluorescentPink,
            )
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
        }
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