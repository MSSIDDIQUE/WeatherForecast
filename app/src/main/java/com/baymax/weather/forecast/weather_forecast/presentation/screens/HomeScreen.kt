package com.baymax.weather.forecast.weather_forecast.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.baymax.launcherapp.ui.theme.BrightWhite
import com.baymax.launcherapp.ui.theme.DarkBlue
import com.baymax.launcherapp.ui.theme.DarkestBlue
import com.baymax.launcherapp.ui.theme.DullBlue
import com.baymax.launcherapp.ui.theme.FluorescentPink
import com.baymax.launcherapp.ui.theme.JetBlack
import com.baymax.weather.forecast.R
import com.baymax.weather.forecast.fetch_location.presentation.model.PredictionDAO
import com.baymax.weather.forecast.presentation.view_state.SnackBarViewState
import com.baymax.weather.forecast.weather_forecast.presentation.components.ProgressBarView
import com.baymax.weather.forecast.weather_forecast.presentation.utils.BaseScreenWrapper
import com.baymax.weather.forecast.weather_forecast.presentation.view_state.WeatherReportsState
import com.baymax.weather.forecast.weather_forecast.presentation.viewmodel.HomeScreenViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(viewModel: HomeScreenViewModel) {
    val weatherState = viewModel.weatherState.collectAsStateWithLifecycle().value
    val snackBarState = viewModel.snackBarState.collectAsStateWithLifecycle().value
    val searchQueryState = viewModel.searchQuery.collectAsStateWithLifecycle().value
    val predictionsState = viewModel.predictions.collectAsStateWithLifecycle().value
    val coroutineScope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false)
    var isSheetOpen by remember { mutableStateOf(false) }
    when (val state = weatherState) {
        WeatherReportsState.Idle -> BaseScreenWrapper {}
        is WeatherReportsState.Loading -> BaseScreenWrapper { ProgressBarView(state.message) }
        is WeatherReportsState.Success -> BaseScreenWrapper(
            city = state.weatherReports.city,
            onSearchClick = {
                coroutineScope.launch {
                    if (!sheetState.isVisible) {
                        sheetState.show()
                        isSheetOpen = true
                    }
                }
            }
        ) {
            WeatherReportsScreen(state.weatherReports)
            if (isSheetOpen) ModalBottomSheet(
                sheetState = sheetState,
                content = {
                    SearchLocationBottomSheetView(
                        initSearchQuery = searchQueryState,
                        listOfPredictions = predictionsState,
                        onSearchQueryUpdate = { query -> viewModel.setSearchQuery(query) },
                        onPredictionClick = { placeId -> viewModel.updateLocationFromPlaceId(placeId) },
                        onCurrentLocationClick = { /* Call ViewModel */ },
                    )
                },
                shape = RoundedCornerShape(25.dp),
                containerColor = JetBlack,
                onDismissRequest = { isSheetOpen = false }
            )
        }

        is WeatherReportsState.Error -> BaseScreenWrapper {
            LaunchedEffect(true) {
                SnackbarHostState().showSnackbar(
                    message = weatherState.message,
                    actionLabel = "",
                    duration = SnackbarDuration.Long,
                )
            }
        }
    }

    when (snackBarState) {
        SnackBarViewState.Hidden -> {}
        is SnackBarViewState.Show -> with(snackBarState.snackBarData) {
            coroutineScope.launch {
                SnackbarHostState().showSnackbar(
                    message,
                    actionLabel,
                    duration = SnackbarDuration.Short,
                )
            }
        }
    }
}

@Composable
fun SearchLocationBottomSheetView(
    initSearchQuery: String,
    listOfPredictions: List<PredictionDAO>,
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
                    value = initSearchQuery,
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
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = FluorescentPink,
                        unfocusedIndicatorColor = DullBlue,
                        focusedContainerColor = DarkestBlue,
                        unfocusedContainerColor = JetBlack,
                        focusedTextColor = FluorescentPink,
                        cursorColor = FluorescentPink,
                        disabledPlaceholderColor = JetBlack
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
                itemsIndexed(listOfPredictions) { idx, item ->
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
                    if (idx != listOfPredictions.lastIndex) {
                        HorizontalDivider(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(1.dp)
                                .padding(horizontal = 30.dp),
                            thickness = DividerDefaults.Thickness, color = DarkestBlue
                        )
                    }
                }
            }
        }
    }
}

