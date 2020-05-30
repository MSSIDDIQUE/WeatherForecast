package com.baymax.weatherforcast

import android.app.Application
import com.baymax.weatherforcast.Model.DB.MyDatabase
import com.baymax.weatherforcast.Model.Mappers.RecordMapperImpl
import com.baymax.weatherforcast.Model.Network.ConnectivityInterceptor
import com.baymax.weatherforcast.Model.Network.ConnectivityInterceptorImpl
import com.baymax.weatherforcast.Model.Network.WeatherNetworkAbstractions
import com.baymax.weatherforcast.Model.Network.WeatherNetworkAbstractionsImpl
import com.baymax.weatherforcast.Model.Repository.Repository
import com.baymax.weatherforcast.Model.Repository.RepositoryImpl
import com.baymax.weatherforcast.Model.WeatherApiService
import com.baymax.weatherforcast.ViewModel.HomeFramentViewModelFactory
import com.jakewharton.threetenabp.AndroidThreeTen
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

class WeatherForecastApplication:Application(), KodeinAware {
    override val kodein = Kodein.lazy {
        import(androidXModule(this@WeatherForecastApplication))
        bind() from singleton { MyDatabase(instance()) }
        bind() from singleton { instance<MyDatabase>().weatherDataDao() }
        bind<ConnectivityInterceptor>() with singleton { ConnectivityInterceptorImpl(instance()) }
        bind() from singleton { WeatherApiService(instance())}
        bind<WeatherNetworkAbstractions>() with singleton { WeatherNetworkAbstractionsImpl(instance()) }
        bind() from singleton { RecordMapperImpl() }
        bind<Repository>() with singleton { RepositoryImpl(instance(),instance(),instance()) }
        bind() from provider { HomeFramentViewModelFactory(instance()) }
    }

    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)
    }

}