package com.baymax.weather.forecast.weather_forecast.presentation.screens

import android.view.Gravity
import android.view.animation.Animation
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieDrawable
import com.baymax.launcherapp.ui.theme.BrightWhite
import com.baymax.launcherapp.ui.theme.DarkBlue
import com.baymax.launcherapp.ui.theme.DarkestBlue
import com.baymax.launcherapp.ui.theme.FluorescentPink
import com.baymax.launcherapp.ui.theme.JetBlack
import com.baymax.weather.forecast.R
import com.baymax.weather.forecast.fetch_location.presentation.model.PredictionDAO
import com.baymax.weather.forecast.presentation.view_state.SnackBarViewState
import com.baymax.weather.forecast.weather_forecast.presentation.model.WeatherReportsDAO
import com.baymax.weather.forecast.weather_forecast.presentation.view_model.HomeScreenViewModel
import com.baymax.weather.forecast.weather_forecast.presentation.view_state.WeatherReportsState
import com.ramcosta.composedestinations.annotation.Destination
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

@Destination
@Composable
fun HomeScreen(
    viewModel: HomeScreenViewModel,
    onCurrentLocationClick: () -> Unit,
) = with(viewModel) {
    val coroutineScope = rememberCoroutineScope()
    val weatherState = weatherState.collectAsStateWithLifecycle().value
    val snackBarState = snackBarState.collectAsStateWithLifecycle().value
    BaseScreenWrapper { scaffoldState ->
        when (weatherState) {
            WeatherReportsState.Idle -> {}
            is WeatherReportsState.Loading -> ProgressBarView(weatherState.message)
            is WeatherReportsState.Success -> SearchLocationBottomSheet(
                weatherReports = weatherState.weatherReports,
                initSearchQuery = searchQuery,
                listOfPredictions = predictions.collectAsStateWithLifecycle(),
                onPredictionClick = ::updateLocationFromPlaceId,
                onSearchQueryUpdate = ::setSearchQuery,
                onCurrentLocationClick = onCurrentLocationClick,
            )

            is WeatherReportsState.Error -> LaunchedEffect(true) {
                scaffoldState.snackbarHostState.showSnackbar(
                    message = weatherState.message,
                    actionLabel = "",
                    duration = SnackbarDuration.Long,
                )
            }
        }

        when (snackBarState) {
            SnackBarViewState.Hidden -> {}
            is SnackBarViewState.Show -> with(snackBarState.snackBarData) {
                coroutineScope.launch {
                    scaffoldState.snackbarHostState.showSnackbar(
                        message,
                        actionLabel,
                        SnackbarDuration.Short,
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SearchLocationBottomSheet(
    weatherReports: WeatherReportsDAO,
    initSearchQuery: MutableStateFlow<String>,
    listOfPredictions: State<List<PredictionDAO>>,
    onPredictionClick: (String) -> Unit,
    onSearchQueryUpdate: (String) -> Unit,
    onCurrentLocationClick: () -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
    ModalBottomSheetLayout(
        sheetState = sheetState,
        sheetShape = RoundedCornerShape(25.dp),
        sheetContent = {
            SearchLocationBottomSheetView(
                initSearchQuery = initSearchQuery,
                listOfPredictions = listOfPredictions,
                onSearchQueryUpdate = onSearchQueryUpdate,
                onPredictionClick = onPredictionClick,
                onCurrentLocationClick = onCurrentLocationClick,
            )
        },
    ) {
        WeatherReportsScreen(weatherReports) {
            coroutineScope.launch {
                if (!sheetState.isVisible) sheetState.show()
            }
        }
    }
}

@Composable
fun SearchLocationBottomSheetView(
    initSearchQuery: MutableStateFlow<String>,
    listOfPredictions: State<List<PredictionDAO>>,
    onPredictionClick: (String) -> Unit,
    onSearchQueryUpdate: (String) -> Unit,
    onCurrentLocationClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        JetBlack,
                        DarkBlue,
                    ),
                ),
            ),
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(30.dp),
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                OutlinedTextField(
                    singleLine = true,
                    value = initSearchQuery.collectAsStateWithLifecycle().value,
                    onValueChange = { onSearchQueryUpdate(it) },
                    leadingIcon = {
                        Icon(
                            Icons.Rounded.Search,
                            tint = FluorescentPink,
                            contentDescription = "Back button",
                            modifier = Modifier.size(30.dp),
                        )
                    },
                    placeholder = {
                        Text(
                            text = "Search location",
                            fontSize = 20.sp,
                            fontFamily = FontFamily(Font(R.font.handlee_regular)),
                        )
                    },
                    shape = RoundedCornerShape(
                        topStart = 25.dp,
                        topEnd = 25.dp,
                        bottomStart = 25.dp,
                        bottomEnd = 25.dp,
                    ),
                    modifier = Modifier
                        .width(IntrinsicSize.Max)
                        .background(
                            color = DarkestBlue,
                            shape = RoundedCornerShape(
                                topStart = 25.dp,
                                topEnd = 25.dp,
                                bottomStart = 25.dp,
                                bottomEnd = 25.dp,
                            ),
                        ),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = FluorescentPink,
                        textColor = FluorescentPink,
                        placeholderColor = JetBlack,
                        cursorColor = FluorescentPink,
                    ),
                    textStyle = TextStyle.Default.copy(
                        fontFamily = FontFamily(Font(R.font.handlee_regular)),
                        fontSize = 16.sp,
                    ),
                    trailingIcon = {
                        Icon(
                            Icons.Rounded.Clear,
                            tint = FluorescentPink,
                            contentDescription = "Back button",
                            modifier = Modifier
                                .size(20.dp)
                                .clickable {
                                    onSearchQueryUpdate("")
                                },
                        )
                    },
                )
                Spacer(modifier = Modifier.width(20.dp))
                Row(
                    modifier = Modifier
                        .width(IntrinsicSize.Max)
                        .height(IntrinsicSize.Max)
                        .background(
                            color = DarkestBlue,
                            shape = RoundedCornerShape(25.dp),
                        )
                        .padding(10.dp)
                        .clickable { onCurrentLocationClick() },
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_my_location),
                        contentDescription = stringResource(R.string.current_location),
                        tint = FluorescentPink,
                        modifier = Modifier
                            .width(30.dp)
                            .height(30.dp),
                    )
                }
            }
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp),
            )
            LazyColumn(
                contentPadding = PaddingValues(5.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                itemsIndexed(listOfPredictions.value) { idx, item ->
                    Text(
                        text = item.description,
                        color = BrightWhite,
                        fontFamily = FontFamily(Font(R.font.handlee_regular)),
                        fontSize = 16.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 5.dp)
                            .clickable { onPredictionClick(item.placeId) },
                    )
                    if (idx != listOfPredictions.value.lastIndex) {
                        TabRowDefaults.Divider(
                            color = DarkestBlue,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(1.dp)
                                .padding(horizontal = 30.dp),
                        )
                    }
                }
            }
        }
    }
}

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
