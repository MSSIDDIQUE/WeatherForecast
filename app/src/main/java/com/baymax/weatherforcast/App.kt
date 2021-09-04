package com.baymax.weatherforcast

import com.baymax.weatherforcast.di.DaggerAppComponent
import com.jakewharton.threetenabp.AndroidThreeTen
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication

class App : DaggerApplication(){
    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        AndroidThreeTen.init(this)
        return DaggerAppComponent.builder()
            .application(this)
            .build()
    }
}