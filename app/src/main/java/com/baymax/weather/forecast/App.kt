package com.baymax.weather.forecast

import com.baymax.weather.forecast.di.DaggerAppComponent
import com.facebook.stetho.Stetho
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication

class App : DaggerApplication() {
    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        Stetho.initializeWithDefaults(this)
        return DaggerAppComponent.builder()
            .application(this)
            .build()
    }
}
