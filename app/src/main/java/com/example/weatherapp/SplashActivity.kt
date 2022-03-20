package com.example.weatherapp

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    private lateinit var  sharedPref:SharedPreferences
    private var fusedLocationClient: FusedLocationProviderClient? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        sharedPref =  getSharedPreferences(getString(R.string.preference_setting_key), Context.MODE_PRIVATE)
    }

    override fun onStart() {
        super.onStart()
        val lat = sharedPref.getString(getString(R.string.pref_user_lat),"")
        if (lat.isNullOrBlank() || lat.isNullOrEmpty()){
            if (checkLocationPermissions()) getUpdatedLocation() else requestLocationPermissions()
        }
        else getUpdatedLocation()
        goHome()
    }

    private fun goHome(){
        Handler().postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, 1000)
    }
    private val writeLocationToPrefrence:(location : Location?)->Unit = { location: Location? ->
        with (sharedPref.edit()) {
            putString(getString(R.string.pref_user_lat),"${location!!.latitude}")
            putString(getString(R.string.pref_user_longitude),"${location!!.longitude}")
            apply()
        }
        goHome()
    }
    fun requestLocationPermissions() {
        ActivityCompat.requestPermissions(
            this, arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            LOCATION_PERMISSION_ID
        )
    }
    fun checkLocationPermissions() :Boolean{
        return (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_ID) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getUpdatedLocation()
            } else {
                Toast.makeText(this, "This action needs to use GPS", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private val locationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            writeLocationToPrefrence(locationResult.lastLocation)
        }
    }
    @SuppressLint("MissingPermission")
    fun getUpdatedLocation() {
        val locationRequest = LocationRequest.create()
        locationRequest.interval = 10000
        locationRequest.fastestInterval = 5000
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.numUpdates = 1
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        fusedLocationClient!!.requestLocationUpdates(
            locationRequest, locationCallback,
            Looper.myLooper()!!
        )
    }
    companion object{
        const val LOCATION_PERMISSION_ID = 1
    }
}