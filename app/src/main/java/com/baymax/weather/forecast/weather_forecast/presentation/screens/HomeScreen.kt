package com.baymax.weather.forecast.weather_forecast.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.TabRowDefaults
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.baymax.weather.forecast.R
import com.baymax.weather.forecast.weather_forecast.presentation.model.WeatherReportsDAO
import com.baymax.weather.forecast.weather_forecast.presentation.view_model.HomeFragmentViewModel
import com.baymax.weather.forecast.weather_forecast.presentation.view_state.WeatherReportsUiState
import com.ramcosta.composedestinations.annotation.Destination
import kotlinx.coroutines.launch

@Destination
@Composable
fun HomeScreen(viewModel: HomeFragmentViewModel) {
    when (val weatherState = viewModel.weatherState.collectAsStateWithLifecycle().value) {
        WeatherReportsUiState.Idle -> Column(Modifier.fillMaxSize()) {}
        is WeatherReportsUiState.Error -> TODO()
        is WeatherReportsUiState.Loading -> ProgressBarScreen(weatherState.message)
        is WeatherReportsUiState.Success -> SearchLocationBottomSheet(
            weatherState.weatherReports,
            viewModel,
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SearchLocationBottomSheet(
    weatherReports: WeatherReportsDAO,
    viewModel: HomeFragmentViewModel,
) {
    val coroutineScope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
    ModalBottomSheetLayout(
        sheetState = sheetState,
        sheetShape = RoundedCornerShape(25.dp),
        sheetContent = {
            SearchLocationBottomSheetView(viewModel) { placeId ->
                coroutineScope.launch {
                    viewModel.updateLocationFromPlaceId(placeId)
                    sheetState.hide()
                }
            }
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
    viewModel: HomeFragmentViewModel,
    onPredictionClick: (String) -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()
    val predictions = viewModel.predictions.collectAsStateWithLifecycle().value
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color.Black,
                        Color(0xFF2E335A),
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
                    .height(25.dp),
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
                    value = viewModel.searchQuery.collectAsStateWithLifecycle().value,
                    onValueChange = {
                        viewModel.setSearchQuery(it)
                    },
                    leadingIcon = {
                        Icon(
                            Icons.Rounded.Search,
                            tint = Color(0xFFD80073),
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
                    modifier = Modifier.fillMaxWidth()
                        .background(
                            color = Color(0xFF1C1B33),
                            shape = RoundedCornerShape(
                                topStart = 25.dp,
                                topEnd = 25.dp,
                                bottomStart = 25.dp,
                                bottomEnd = 25.dp,
                            ),
                        ),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = Color(0xFFD80073),
                        textColor = Color(0xFFD80073),
                        placeholderColor = Color.Black,
                        cursorColor = Color(0xFFD80073),
                    ),
                    textStyle = TextStyle.Default.copy(
                        fontFamily = FontFamily(Font(R.font.handlee_regular)),
                        fontSize = 20.sp,
                    ),
                    trailingIcon = {
                        Icon(
                            Icons.Rounded.Clear,
                            tint = Color(0xFFD80073),
                            contentDescription = "Back button",
                            modifier = Modifier.size(20.dp)
                                .clickable {
                                    coroutineScope.launch {
                                        viewModel.setSearchQuery("")
                                    }
                                },
                        )
                    },
                )
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
                itemsIndexed(predictions) { idx, item ->
                    Text(
                        text = item.first,
                        color = Color.White,
                        fontFamily = FontFamily(Font(R.font.handlee_regular)),
                        fontSize = 20.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 5.dp)
                            .clickable {
                                onPredictionClick(item.second)
                            },
                    )
                    if (idx != predictions.lastIndex) {
                        TabRowDefaults.Divider(
                            color = Color(0xFF1C1B33),
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
