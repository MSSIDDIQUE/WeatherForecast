package com.baymax.weatherforcast.utils.dataBinding

import android.graphics.drawable.Drawable
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
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
fun getTimeFromTimestamp(view:TextView,s: String) {
    try {
        val temp = s.split(" ")
        val sdf = SimpleDateFormat("hh:mm")
        val netDate = Date(temp.get(0).toLong()*1000)
        view.text = sdf.format(netDate)+" "+temp.get(1)
    } catch (e: Exception) {
        view.text = ""
    }
}