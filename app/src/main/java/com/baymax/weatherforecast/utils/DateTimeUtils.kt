package com.baymax.weatherforecast.utils

import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter
import java.text.DecimalFormat

object DateTimeUtils {
    fun getCurrentDateTime(): HashMap<String, String> {
        val dateTime = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val formattedDateTime = dateTime.format(formatter).split(" ")
        var hourInTime = formattedDateTime[1].subSequence(0, 2).toString().toInt()
        hourInTime -= hourInTime % 3
        return hashMapOf<String, String>().apply {
            this["date"] = formattedDateTime[0]
            this["time"] = "${DecimalFormat("00").format(hourInTime)}:00:00"
        }
    }
}