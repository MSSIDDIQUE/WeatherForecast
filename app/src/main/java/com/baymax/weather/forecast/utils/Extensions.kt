package com.baymax.weather.forecast.utils

import java.util.Calendar
import kotlin.math.roundToInt

fun String.toStandardString(): String {
    return try {
        "${this.first().uppercase()}${this.subSequence(1, this.length)}"
    } catch (e: IndexOutOfBoundsException) {
        this
    }
}

fun String.buildIconUrl(
    sizeWithFormat: String,
) = Constants.WEATHER_API_ICONS_BASE_URL + this + sizeWithFormat

fun Double.toDegreeCelsius() = "${
    minus(273.15).roundToInt()
}°"

fun Double.toDegreeFahrenheit() = "${
    minus(273.15).times(9).div(5).plus(32).roundToInt()
}°"

fun Long.isTodayWeather(): Boolean {
    val date = Calendar.getInstance().apply {
        timeInMillis = this@isTodayWeather
    }
    Calendar.getInstance().run {
        return get(Calendar.YEAR) == date.get(Calendar.YEAR) &&
            get(Calendar.DAY_OF_YEAR) == date.get(Calendar.DAY_OF_YEAR)
    }
}

fun Long.isRecentWeather(): Boolean {
    val date = Calendar.getInstance().apply {
        timeInMillis = this@isRecentWeather
    }
    val now = Calendar.getInstance()
    val timeDifference = now.timeInMillis - date.timeInMillis
    val timeDifferenceInHours = timeDifference.div(60.times(60))
    return timeDifferenceInHours <= 3
}
