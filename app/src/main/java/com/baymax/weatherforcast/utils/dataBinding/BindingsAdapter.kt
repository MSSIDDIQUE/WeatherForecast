package com.baymax.weatherforcast.utils.dataBinding

import android.graphics.drawable.Drawable
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter
import java.text.SimpleDateFormat
import java.util.*

@BindingAdapter("imageResource")
fun bindImageFromUrl(view: ImageView, imageUrl: Drawable) {
    Glide.with(view.context)
        .load(imageUrl)
        .fitCenter()
        .centerCrop()
        .transition(DrawableTransitionOptions.withCrossFade())
        .into(view)
}

@BindingAdapter("timeFromTimeStamp")
fun getTimeFromTimestamp(view: TextView, s: String) {
    try {
        val temp = s.split(" ")
        val sdf = SimpleDateFormat("hh:mm")
        val netDate = Date(temp.get(0).toLong() * 1000)
        view.text = sdf.format(netDate) + " " + temp.get(1)
    } catch (e: Exception) {
        view.text = ""
    }
}

@BindingAdapter("kelvinToCelcius")
fun getKelvinToCelcius(view: TextView, d: String) {
    try {
        val temp = Math.round(d.split(" ").get(0).toDouble() - 273.15).toString()
        view.text = temp + d.split(" ").get(1)
    } catch (e: Exception) {
        view.text = ""
    }
}

@BindingAdapter("dayFromDate")
fun getDayFromDate(view: TextView, d: String?) {
    if (d != null) {
        if(!d.isEmpty()){
            try {
                Log.d("(Saquib)","the date string is $d")
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
    try {
        val cap = s.lowercase(Locale.getDefault())
            .substring(0, 1)
            .uppercase(
                Locale.getDefault()
            ) + s.lowercase(
            Locale.getDefault()
        )
            .substring(1)
        return cap
    } catch (e: Exception) {
        return s
    }
}