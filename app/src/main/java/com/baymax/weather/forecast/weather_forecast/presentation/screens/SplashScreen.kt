package com.baymax.weather.forecast.weather_forecast.presentation.screens

import android.view.Gravity
import android.view.animation.Animation
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieDrawable
import com.baymax.weather.forecast.R
import com.baymax.weather.forecast.weather_forecast.presentation.screens.destinations.HomeScreenDestination
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.seconds

@RootNavGraph(start = true)
@Destination
@Composable
fun SplashScreen(navigator: DestinationsNavigator) {
    BaseScreenWrapper {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .wrapContentWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom
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
                                blurRadius = 2f,
                            ),
                            textDecoration = TextDecoration.None,
                        ),
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
                                blurRadius = 2f,
                            ),
                            textDecoration = TextDecoration.None,
                        ),
                    ) {
                        append("F")
                    }
                    append("orecast")
                },
                fontSize = 30.sp,
                color = Color.White,
                fontFamily = FontFamily(Font(R.font.handlee_regular)),
                fontStyle = FontStyle.Italic,
                textDecoration = TextDecoration.Underline,
            )
            Text(
                text = buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            color = Color.White,
                            fontSize = 8.sp,
                        ),
                    ) {
                        append("powered by  ")
                    }
                    append("MSSIDDIQUE")
                },
                fontSize = 10.sp,
                color = Color.White,
                fontFamily = FontFamily(Font(R.font.handlee_regular)),
            )
        }
        ShowSplashScreenAnimation(navigator)
    }
}

@Composable
fun ShowSplashScreenAnimation(navigator: DestinationsNavigator) {
    val context = LocalContext.current
    val customView = remember { LottieAnimationView(context) }
    Column(
        Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        AndroidView({ customView }, modifier = Modifier.fillMaxSize()) { view ->
            with(view) {
                setAnimation(R.raw.splash_animation)
                playAnimation()
                repeatMode = LottieDrawable.RESTART
                repeatCount = Animation.INFINITE
                foregroundGravity = Gravity.CENTER
            }
        }
    }
    LaunchedEffect(Unit) {
        delay(5.seconds)
        navigator.navigate(HomeScreenDestination)
    }
}
