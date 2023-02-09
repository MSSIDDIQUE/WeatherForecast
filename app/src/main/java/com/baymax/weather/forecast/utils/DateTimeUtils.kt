package com.baymax.weather.forecast.utils

import java.text.SimpleDateFormat
import java.util.*

object DateTimeUtils {
    fun Long.timeFromEpoch(): String? = try {
        SimpleDateFormat(
            "hh:mm a",
            Locale.getDefault(),
        ).format(Date(this.times(1000))).uppercase(Locale.getDefault())
    } catch (e: Exception) {
        null
    }

    fun Long.dayFromEpoch(): String? = try {
        SimpleDateFormat(
            "EEEE",
            Locale.getDefault(),
        ).format(Date(this.times(1000))).uppercase(Locale.getDefault())
    } catch (e: Exception) {
        null
    }

    fun Long.dateTimeFromEpoch(): String {
        val dateFormat = SimpleDateFormat("EEE, MMM", Locale.getDefault())
        val timeFormat = SimpleDateFormat("h:mm a", Locale.getDefault())
        val date = Date(this * 1000)
        val dateString = dateFormat.format(date)
        val timeString = timeFormat.format(date)
        val dayOfMonth = date.date
        val ordinal = when {
            dayOfMonth in 11..13 -> "${dayOfMonth}th"
            dayOfMonth % 10 == 1 -> "${dayOfMonth}st"
            dayOfMonth % 10 == 2 -> "${dayOfMonth}nd"
            dayOfMonth % 10 == 3 -> "${dayOfMonth}rd"
            else -> "${dayOfMonth}th"
        }
        return "$dateString $ordinal\n$timeString"
    }
}
