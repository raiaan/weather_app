package com.example.weatherapp.repositories

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.preference.PreferenceManager
import com.example.weatherapp.R
import com.example.weatherapp.models.WeatherResponse
import com.example.weatherapp.network.RetrofitService
import retrofit2.Response

class OnlineRepository (private val retrofitService:RetrofitService, private val context:Context) {
    lateinit var Units : String
    lateinit var language : String
    private lateinit var lat:String
    private lateinit var long:String
    private val sharedPreferences:SharedPreferences
    init {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        getSettingDetails()
    }
    suspend fun getWeatherData():Response<WeatherResponse> {
        getSettingDetails()
        return retrofitService.allWeatherData(lat = lat,lon=long,Units = Units,language = language)
    }
    private fun getSettingDetails(){
        val languageSys = sharedPreferences.getString("language", "English").toString()
        if (languageSys.equals("English")) {
            language = "en"
        } else if (languageSys.equals("Arabic")) {
            language = "ar"
        }
        Log.v("change",languageSys)
        val units = sharedPreferences.getString("Temp" , "temp_c").toString()
        if(units.equals("temp_c")){
            Units = "metric"
        }else if(units.equals("temp_f")){
            Units = "imperial"
        }
        Log.v("change",units)
        lat = sharedPreferences.getString( context.resources.getString(R.string.pref_user_lat),"30.044420")!!
        long = sharedPreferences.getString( context.resources.getString(R.string.pref_user_longitude),"31.235712")!!
    }
}