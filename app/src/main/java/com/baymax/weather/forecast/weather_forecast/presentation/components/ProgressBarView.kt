package com.baymax.weather.forecast.weather_forecast.presentation.components

import android.view.Gravity
import android.view.animation.Animation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieDrawable
import com.baymax.weather.forecast.R

@Composable
fun ProgressBarView(loadingText: String = stringResource(id = R.string.loading)) {
    val context = LocalContext.current
    val customView = remember { LottieAnimationView(context) }
    Column(
        Modifier.wrapContentSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        AndroidView({ customView }) { view ->
            with(view) {
                setAnimation(R.raw.loading_animation)
                playAnimation()
                repeatMode = LottieDrawable.RESTART
                repeatCount = Animation.INFINITE
                foregroundGravity = Gravity.CENTER
            }
        }
        Text(
            text = loadingText,
            color = Color(0xFFD80073),
            fontFamily = FontFamily(Font(R.font.handlee_regular)),
            fontSize = 24.sp,
            modifier = Modifier.wrapContentSize(align = Alignment.Center),
        )
    }
}

