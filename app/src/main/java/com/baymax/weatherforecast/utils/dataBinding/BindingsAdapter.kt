package com.baymax.weatherforecast.utils.dataBinding

import android.util.Log
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter
import java.text.SimpleDateFormat
import java.util.*

@BindingAdapter("imageResource")
fun bindImageFromUrl(view: AppCompatImageView, imageUrl: String) {
    Glide.with(view.context)
        .load(imageUrl)
        .fitCenter()
        .centerCrop()
        .transition(DrawableTransitionOptions.withCrossFade())
        .into(view)
}

@BindingAdapter("timeFromTimeStamp")
fun getTimeFromTimestamp(view: TextView, s: String?) {
    try {
        s?.let {
            val sdf = SimpleDateFormat("hh:mm a")
            val netDate = Date(s.toLong() * 1000)
            view.text = sdf.format(netDate).uppercase(Locale.getDefault())
        } ?: run { view.text = "" }
    } catch (e: Exception) {
        view.text = ""
    }
}

@BindingAdapter("dateFromTimeStamp")
fun getDateFromTimestamp(view: TextView, s: String?) {
    try {
        s?.let {
            val date = LocalDate.parse(s.split(" ")[0])
            val formatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy")
            val formattedDate = date.format(formatter)
            view.text = formattedDate
        } ?: run { view.text = "" }
    } catch (e: Exception) {
        e.printStackTrace()
        view.text = ""
    }
}

@BindingAdapter("kelvinToCelcius")
fun getKelvinToCelsius(view: TextView, d: String) {
    try {
        val temp = Math.round(d.split(" ").get(0).toDouble() - 273.15).toString()
        view.text = temp + d.split(" ").get(1)
    } catch (e: Exception) {
        view.text = ""
    }
}

@BindingAdapter("dayFromDate")
fun getDayFromDate(view: AppCompatTextView, d: String?) {
    if (d != null) {
        if (d.isNotEmpty()) {
            try {
                view.text = getStandardString(
                    LocalDateTime.parse(
                        d.replace(
                            " ",
                            "T"
                        )
                    ).dayOfWeek.toString()
                )
            } catch (e: Exception) {
                view.text = ""
            }
        }
    }
}

fun getStandardString(s: String): String {
    return try {
        val cap = s.lowercase(Locale.getDefault())
            .substring(0, 1)
            .uppercase(
                Locale.getDefault()
            ) + s.lowercase(
            Locale.getDefault()
        )
            .substring(1)
        cap
    } catch (e: Exception) {
        s
    }
}