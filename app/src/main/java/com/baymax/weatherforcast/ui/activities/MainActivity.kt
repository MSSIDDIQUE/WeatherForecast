package com.baymax.weatherforcast.ui.activities

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance
import java.io.InputStream
import java.util.*
import kotlin.collections.ArrayList
import com.baymax.weatherforcast.R
import com.baymax.weatherforcast.ui.fragments.home_fragment.ui.HomeFramentViewModel
import com.baymax.weatherforcast.ui.fragments.home_fragment.ui.HomeFramentViewModelFactory


class MainActivity : AppCompatActivity(), KodeinAware {

    companion object{
        private const val MULTIPLE_LOCAITON_PERMISSION = 1
        private const val LOCATION_SETTINGS_REQUEST = 1
    }
    val permissions = arrayListOf(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION
    )
    private lateinit var alertDialog:AlertDialog
    private val viewModelFactory: HomeFramentViewModelFactory by instance()
    private lateinit var viewModel: HomeFramentViewModel
    override val kodein by kodein()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel = ViewModelProvider(this,viewModelFactory).get(HomeFramentViewModel::class.java)
//        location.setOnClickListener {
//            createDailog()
//            alertDialog.show()
//        }
    }


    fun turnOnGPS(){
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
                    } catch (e: IntentSender.SendIntentException) {
                    }
                    LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                    }
                }
            }
        }
    }


    fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            MULTIPLE_LOCAITON_PERMISSION
        )
    }

    fun hasLocationPermission(): Boolean {
        permissions.forEach { permisson ->
            if (ContextCompat.checkSelfPermission(
                    this,
                    permisson
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return false
            }
        }
        return true
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == MULTIPLE_LOCAITON_PERMISSION) {
            var all_permissions_granted = true;
            if(grantResults.isNotEmpty()){
                grantResults.forEach { permissionResult->
                    if(permissionResult != PackageManager.PERMISSION_GRANTED){
                        all_permissions_granted = false
                    }
                }
                if (all_permissions_granted) {
                    if(!isGPSActive()){
                        turnOnGPS()
                    }
                    else{
                        viewModel.setGpsStatus(true)
                    }
                }
                else{
                    Snackbar.make(
                        main_layout,
                        "Provide all the permissions",
                        Snackbar.LENGTH_LONG).show()
                    /*createDailog()
                    alertDialog.show()*/
                }
            }
        }
    }

    private fun getJsonData(): String? {
        var json: String? = null
        try {
            val  inputStream: InputStream = this.assets!!.open("cities.json")
            json = inputStream.bufferedReader().use{it.readText()}
        } catch (ex: Exception) {
            ex.printStackTrace()
            return null
        }
        return json
    }

//    private fun getCountries(array:List<locations>) : ArrayList<String>{
//        val countries = ArrayList<String>()
//        for(country in array){
//            countries.add(country.country)
//        }
//        return countries
//    }

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

/*    private fun createDailog(){
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
                    loading_text.visibility = View.VISIBLE
                    lifecycleScope.launch {
                        weatherNetworkAbstractions.fetchWeather(dialogView.city.text.toString().trim())
                        progressBar.visibility = View.GONE
                        loading_text.visibility = View.GONE
                    }
                    val preference = PreferenceManager.getDefaultSharedPreferences(this)
                    preference.edit().putString("CUSTOM_LOCATION",dialogView.city.text.toString().trim()).apply()
                }
            }
            .setNegativeButton("Cancel"){ dailogInterface, which->
            }
            .create()
    }*/

    fun isGPSActive():Boolean{
        val locationManager = this.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        var gps_enabled = false
        try {
            gps_enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        }catch(e:Exception){
            return false
        }
        return gps_enabled
    }

    fun isOnline(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivityManager != null) {
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                    return true
                }
            }
        }
        return false
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == LOCATION_SETTINGS_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                viewModel.setGpsStatus(true)
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                Snackbar.make(
                    main_layout,
                    "Please turn on you GPS!",
                    Snackbar.LENGTH_LONG).setAction(
                    "Retry",
                    View.OnClickListener {
                        turnOnGPS()
                    }
                ).show()
                viewModel.setGpsStatus(false)
                /*createDailog()
                alertDialog.show()*/
            }
        }
    }
    
}
