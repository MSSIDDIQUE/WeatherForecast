package com.baymax.weather.forecast

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.widget.ImageView
import java.io.IOException
import java.net.URL

class ImageWrapper {

    private var hashMap: HashMap<String, Bitmap> = HashMap()

    fun ImageView.loadImage(url: String) {
        if (hashMap.containsKey(url)) {
            this.setImageBitmap(hashMap[url])
        } else {
            try {
                val imgUrl = URL(url)
                val imageBitmap = BitmapFactory.decodeStream(imgUrl.openConnection().getInputStream())
                this.setImageBitmap(imageBitmap)
                hashMap[url] = imageBitmap
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
}