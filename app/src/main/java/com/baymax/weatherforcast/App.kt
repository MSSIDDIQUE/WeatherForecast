package com.baymax.weatherforcast

import android.app.Application
import android.content.Context
import com.baymax.weatherforcast.data.db.MyDatabase
import com.baymax.weatherforcast.utils.mappers.RecordMapper
import com.baymax.weatherforcast.ui.fragments.home_fragment.data.WeatherRemoteDataSource
import com.baymax.weatherforcast.ui.fragments.home_fragment.data.WeatherRepository
import com.baymax.weatherforcast.api.WeatherApiService
import com.baymax.weatherforcast.ui.fragments.home_fragment.data.LocationProviderUseCase
import com.baymax.weatherforcast.ui.fragments.home_fragment.ui.HomeFramentViewModelFactory
import com.baymax.weatherforcast.utils.PrefHelper
import com.google.android.gms.location.LocationServices
import com.jakewharton.threetenabp.AndroidThreeTen
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

class App:Application(), KodeinAware {
    override val kodein = Kodein.lazy {
        import(androidXModule(this@App))
        bind() from singleton { PrefHelper(applicationContext) }
        bind() from singleton { WeatherApiService(instance()) }
        bind() from singleton { MyDatabase(instance()) }
        bind() from singleton { instance<MyDatabase>().weatherDataDao() }
        bind() from singleton { WeatherRemoteDataSource(instance()) }
        bind() from provider { LocationServices.getFusedLocationProviderClient(instance<Context>()) }
        bind() from singleton { WeatherRepository(instance(),instance(),instance()) }
        bind() from singleton { LocationProviderUseCase(instance(),instance()) }
        bind() from provider { HomeFramentViewModelFactory(instance(),instance()) }
    }

    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)
    }

}