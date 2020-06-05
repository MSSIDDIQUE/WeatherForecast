package com.baymax.weatherforcast

import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance
import android.content.pm.PackageManager
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.baymax.weatherforcast.Model.Network.WeatherNetworkAbstractions
import com.baymax.weatherforcast.Model.Network.WeatherNetworkAbstractionsImpl
import com.baymax.weatherforcast.Utils.LifecycleBoundLocationManager
import com.baymax.weatherforcast.Utils.locations
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.custom_dialogue.view.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.launch
import java.io.InputStream
import java.util.HashMap


private const val MY_PERMISSION_ACCESS_COARSE_LOCATION = 1
class MainActivity : AppCompatActivity(), KodeinAware {

    private val fusedLocationProviderClient :FusedLocationProviderClient by instance()
    private val weatherNetworkAbstractions :WeatherNetworkAbstractions by instance()
    override val kodein by kodein()
    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(p0: LocationResult?) {
            super.onLocationResult(p0)
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
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
        val alertDialog = AlertDialog.Builder(this)
            .setTitle("Enter City")
            .setIcon(R.drawable.icon_launcher_100)
            .setView(dialogView)
            .setPositiveButton("Ok"){dailogInterface, which->
                val city = cities.filter { it -> it.equals(dialogView.city.text.toString()) }
                if(city.size==0){
                    Snackbar.make(main_layout,"Please enter a valid city name",Snackbar.LENGTH_LONG).show()
                    return@setPositiveButton
                }
                else{
                    progressBar.visibility = View.VISIBLE
                    lifecycleScope.launch {
                        weatherNetworkAbstractions.fetchWeather(dialogView.city.text.toString())
                        progressBar.visibility = View.GONE
                    }
                }
            }
            .setNegativeButton("Cancel"){ dailogInterface, which->
            }
            .create()
        location.setOnClickListener {
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
        LifecycleBoundLocationManager(
            this,
            fusedLocationProviderClient, locationCallback
        )
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
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                bindLocationManager()
            else
                Snackbar.make(main_layout,"Please Turn on your GPS",Snackbar.LENGTH_LONG).show()
                //Toast.makeText(this, "Please, set location manually in settings", Toast.LENGTH_LONG).show()
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
}
