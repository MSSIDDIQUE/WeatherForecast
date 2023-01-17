package com.baymax.weather.forecast.utils

fun String.toStandardString(): String {
    return try {
        "${this.first().uppercase()}${this.subSequence(1, this.length)}"
    } catch (e: IndexOutOfBoundsException) {
        this
    }
}
