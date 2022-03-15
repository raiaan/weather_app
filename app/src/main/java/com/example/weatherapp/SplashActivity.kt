package com.example.weatherapp

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.core.content.ContextCompat
import com.example.weatherapp.util.PermissionUtils.requestPermission
import com.google.android.gms.location.*
import com.google.android.gms.tasks.OnSuccessListener

class SplashActivity : AppCompatActivity() {
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var sharedPref:SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        sharedPref = getSharedPreferences(
            getString(R.string.preference_setting_key), Context.MODE_PRIVATE)?: return

    }

    override fun onStart() {
        super.onStart()
        startLocationUpdates()
        if (sharedPref.getBoolean(getString(R.string.preference_first_time),true)){
            with (sharedPref.edit()) {
                putBoolean(getString(R.string.preference_first_time),false)
                apply()
            }
            goHome()
        }
        else goHome()
    }

    private fun goHome(){
        Handler().postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, 3000)
    }
    private val writeLoactionToPrefrence:(location : Location?)->Unit ={location : Location?->

    }
    private fun startLocationUpdates() {
        Log.v("change","start startLocationUpdates")
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED) {
            val locationRequest = LocationRequest.create().apply {
                interval = 100
                fastestInterval = 50
                priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
                maxWaitTime= 100
            }
            fusedLocationClient.requestLocationUpdates(locationRequest, mLocationCallback, Looper.myLooper()!!)
            fusedLocationClient.lastLocation.addOnSuccessListener {
                with(sharedPref.edit()) {
                    putString(getString(R.string.pref_user_lat), "${it!!.latitude}")
                    putString(getString(R.string.pref_user_longitude), "${it!!.longitude}")
                    Log.v("change", "from splash ${it!!.latitude}")
                    Log.v("change", "from splash ${it!!.longitude}")
                    commit()
                }
                goHome()
            }
        }else{
            requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
                Manifest.permission.ACCESS_FINE_LOCATION, true
            )
        }
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            return
        }
        Log.v("permissionResult","request permission function")
        startLocationUpdates()
    }

    companion object {
        /**
         * Request code for location permission request.
         *
         * @see .onRequestPermissionsResult
         */
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }
    val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(p0: LocationResult) {
            super.onLocationResult(p0)
            Log.v("change","${p0.lastLocation.altitude}")
        }

        override fun onLocationAvailability(p0: LocationAvailability) {
            super.onLocationAvailability(p0)
            Log.v("change","${p0.isLocationAvailable}")
        }
    }

}