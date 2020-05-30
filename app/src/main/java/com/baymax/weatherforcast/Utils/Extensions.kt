package com.baymax.weatherforcast.Utils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.baymax.weatherforcast.Model.Mappers.RecordMapperImpl
import com.baymax.weatherforcast.Model.Network.Response.Record
import com.baymax.weatherforcast.Model.Network.Response.Weather

fun RecordMapperImpl.toRecordDb(record:Record) = mapResponseRecord(record)

fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)
}