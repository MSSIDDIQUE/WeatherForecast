package com.baymax.weatherforcast

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.IntentSender.SendIntentException
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.preference.PreferenceManager
import com.baymax.weatherforcast.Model.Network.WeatherNetworkAbstractions
import com.baymax.weatherforcast.Utils.LifecycleBoundLocationManager
import com.baymax.weatherforcast.Utils.Providers.LocationProvider
import com.baymax.weatherforcast.Utils.Providers.PreferenceProvider
import com.baymax.weatherforcast.Utils.locations
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.custom_dialogue.view.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance
import java.io.InputStream
import java.util.*
import kotlin.collections.ArrayList


private const val MY_PERMISSION_ACCESS_COARSE_LOCATION = 1
private const val LOCATION_SETTINGS_REQUEST = 1
class MainActivity : AppCompatActivity(), KodeinAware {

    private lateinit var alertDialog:AlertDialog
    private val fusedLocationProviderClient :FusedLocationProviderClient by instance()
    private val weatherNetworkAbstractions :WeatherNetworkAbstractions by instance()
    private val locationProvider: LocationProvider by instance()
    private val preferenceProvider:PreferenceProvider by instance()
    override val kodein by kodein()
    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(p0: LocationResult?) {
            super.onLocationResult(p0)
            if(p0!=null){
                val preference = PreferenceManager.getDefaultSharedPreferences(this@MainActivity)
                preference.edit().putString("CUSTOM_LOCATION",getDeviceCityName(p0.lastLocation.latitude,p0.lastLocation.latitude)).apply()
                progressBar.visibility = View.VISIBLE
                lifecycleScope.launch {
                    delay(3000.toLong())
                    weatherNetworkAbstractions.fetchWeather(locationProvider.getPreferredLocationString())
                    progressBar.visibility = View.GONE
                }
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        location.setOnClickListener {
            createDailog()
            alertDialog.show()
        }
        requestLocationPermission()

        if (hasLocationPermission()) {
            bindLocationManager()
        }
        else
            requestLocationPermission()
    }

    private fun bindLocationManager() {
        Log.d("(Saquib)","bindLocationManager is called")
        LifecycleBoundLocationManager(
            this,
            fusedLocationProviderClient, locationCallback
        ).startLocationUpdates()
    }

    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
            MY_PERMISSION_ACCESS_COARSE_LOCATION
        )
    }

    private fun hasLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(this,
            Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == MY_PERMISSION_ACCESS_COARSE_LOCATION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                bindLocationManager()
                turnOnGPS()
            }
            else if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_DENIED) {
                Snackbar.make(main_layout, "Please Turn on your GPS", Snackbar.LENGTH_LONG).show()
                createDailog()
                alertDialog.show()
            }
        }
    }

    private fun getJsonData(): String? {
        var json: String? = null
        try {
            val  inputStream: InputStream = this?.assets!!.open("cities.json")
            json = inputStream.bufferedReader().use{it.readText()}
        } catch (ex: Exception) {
            ex.printStackTrace()
            return null
        }
        return json
    }

    private fun getCountries(array:List<locations>) : ArrayList<String>{
        val countries = ArrayList<String>()
        for(country in array){
            countries.add(country.country)
        }
        return countries
    }

    private fun getCities(map:HashMap<String,ArrayList<String>>) : ArrayList<String>{
        val countries = ArrayList<String>()
        map.forEach{
                k,v->
            v!!.forEach { it ->
                countries.add(it)
            }
        }
        return countries
    }

    private fun getDeviceCityName(lat:Double,lon:Double):String{
        val geocoder = Geocoder(this, Locale.getDefault())
        val addresses: List<Address> = geocoder.getFromLocation(lat, lon, 5)
        val cityName: String = addresses[0].locality
        return cityName
    }

    private fun createDailog(){
        val dialogView = layoutInflater.inflate(R.layout.custom_dialogue,null,false)
        val locations_array: HashMap<String, ArrayList<String>> = Gson().fromJson(
            getJsonData(),
            object :
                TypeToken<HashMap<String?, ArrayList<String?>?>?>() {}.type
        )
        val countries = ArrayList<String>(locations_array.keys)
        val cities = getCities(locations_array)
        val country_adapter = ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,countries)
        val citi_adapter = ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,cities)
        dialogView.city.setAdapter(citi_adapter)
        alertDialog = AlertDialog.Builder(this)
            .setTitle("Enter City")
            .setIcon(R.drawable.icon_launcher_100)
            .setView(dialogView)
            .setPositiveButton("Ok"){dailogInterface, which->
                val city = cities.filter { it -> it.toLowerCase().equals(dialogView.city.text.trim().toString().toLowerCase()) }
                if(city.size==0){
                    Snackbar.make(main_layout,"Please enter a valid city name",Snackbar.LENGTH_LONG).show()
                    return@setPositiveButton
                }
                else{
                    progressBar.visibility = View.VISIBLE
                    lifecycleScope.launch {
                        weatherNetworkAbstractions.fetchWeather(dialogView.city.text.toString().trim())
                        progressBar.visibility = View.GONE
                    }
                    val preference = PreferenceManager.getDefaultSharedPreferences(this)
                    preference.edit().putString("CUSTOM_LOCATION",dialogView.city.text.toString().trim()).apply()
                }
            }
            .setNegativeButton("Cancel"){ dailogInterface, which->
            }
            .create()
    }

    private fun turnOnGPS(){
        val mLocationRequest = LocationRequest.create()
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
            .setInterval(10 * 1000.toLong())
            .setFastestInterval(1 * 1000.toLong())
        val settingsBuilder = LocationSettingsRequest.Builder()
            .addLocationRequest(mLocationRequest)
        settingsBuilder.setAlwaysShow(true)
        val result =
            LocationServices.getSettingsClient(this)
                .checkLocationSettings(settingsBuilder.build())

        result.addOnCompleteListener { task ->
            try {
                val response =
                    task.getResult(ApiException::class.java)
            } catch (ex: ApiException) {
                when (ex.statusCode) {
                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> try {
                        val resolvableApiException =
                            ex as ResolvableApiException
                        resolvableApiException
                            .startResolutionForResult(
                                this,
                                LOCATION_SETTINGS_REQUEST
                            )
                    } catch (e: SendIntentException) {
                    }
                    LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                    }
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == LOCATION_SETTINGS_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                progressBar.visibility = View.VISIBLE
                lifecycleScope.launch {
                    delay(3000.toLong())
                    weatherNetworkAbstractions.fetchWeather(locationProvider.getPreferredLocationString())
                    val preference = PreferenceManager.getDefaultSharedPreferences(this@MainActivity)
                    preference.edit().putString("CUSTOM_LOCATION",locationProvider.getPreferredLocationString()).apply()
                    progressBar.visibility = View.GONE
                }
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                createDailog()
                alertDialog.show()
            }
        }
    }
}
