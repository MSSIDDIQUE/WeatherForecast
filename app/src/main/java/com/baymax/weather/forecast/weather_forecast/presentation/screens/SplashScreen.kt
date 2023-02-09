package com.baymax.weather.forecast.weather_forecast.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.baymax.weather.forecast.R

@Composable
fun SplashScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF2E335A),
                        Color(0xFF1C1B33)
                    )
                )
            ),
        Alignment.BottomCenter
    ) {
        Column(
            modifier = Modifier
                .wrapContentWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            color = Color(0xFFD80073),
                            fontSize = 50.sp,
                            shadow = Shadow(
                                color = Color(0xFF4C0034),
                                offset = Offset(2.0f, 5.0f),
                                blurRadius = 2f
                            ),
                            textDecoration = TextDecoration.None
                        )
                    ) {
                        append("W")
                    }
                    append("eather ")
                    withStyle(
                        style = SpanStyle(
                            color = Color(0xFFD80073),
                            fontSize = 50.sp,
                            shadow = Shadow(
                                color = Color(0xFF4C0034),
                                offset = Offset(2.0f, 5.0f),
                                blurRadius = 2f
                            ),
                            textDecoration = TextDecoration.None
                        )
                    ) {
                        append("F")
                    }
                    append("orecast")
                },
                fontSize = 30.sp,
                color = Color.White,
                fontFamily = FontFamily(Font(R.font.handlee_regular)),
                fontStyle = FontStyle.Italic,
                textDecoration = TextDecoration.Underline
            )
            Text(
                text = buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            color = Color.White,
                            fontSize = 8.sp
                        )
                    ) {
                        append("powered by  ")
                    }
                    append("MSSIDDIQUE")
                },
                fontSize = 10.sp,
                color = Color.White,
                fontFamily = FontFamily(Font(R.font.handlee_regular))
            )
        }
        ShowSplashScreenAnimation()
    }
}

@Composable
fun ShowSplashScreenAnimation() {
    val speed by remember { mutableStateOf(1f) }
    val isPlaying by remember { mutableStateOf(true) }
    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.splash_animation)
    )
    val progress by animateLottieCompositionAsState(
        composition,
        iterations = LottieConstants.IterateForever,
        isPlaying = isPlaying,
        speed = speed,
        restartOnPlay = false
    )
    LottieAnimation(
        composition = composition,
        progress = progress,
        modifier = Modifier.fillMaxSize()
    )
}
