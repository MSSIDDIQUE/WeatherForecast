package com.baymax.weatherforcast.utils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.baymax.weatherforcast.utils.mappers.RecordMapper
import com.baymax.weatherforcast.api.response.City
import com.baymax.weatherforcast.api.response.Record

fun RecordMapper.toRecordDb(record: Record, location: City) = mapResponseRecord(record,location)

fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)
}