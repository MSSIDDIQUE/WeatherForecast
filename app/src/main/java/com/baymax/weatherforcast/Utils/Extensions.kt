package com.baymax.weatherforcast.Utils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.baymax.weatherforcast.Model.Mappers.RecordMapperImpl
import com.baymax.weatherforcast.Model.Response.City
import com.baymax.weatherforcast.Model.Response.Record

fun RecordMapperImpl.toRecordDb(record:Record,location:City) = mapResponseRecord(record,location)

fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)
}