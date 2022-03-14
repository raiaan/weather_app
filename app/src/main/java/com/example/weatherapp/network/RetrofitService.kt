package com.example.weatherapp.network

import com.example.weatherapp.models.WeatherResponse
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface RetrofitService {
    @GET("onecall")
    suspend fun allWeatherData(@Query("lat", encoded=true) lat:String ,
                               @Query("lon", encoded=true) lon:String ,
                               @Query("exclud", encoded=true) exclude:String ="daily",
                               @Query("appid", encoded=true) appid:String = "adc11d699014fce526e5c765fdf3539d"): Response<WeatherResponse>

    companion object {
        var retrofitService: RetrofitService? = null
        fun getInstance() : RetrofitService {
            if (retrofitService == null) {
                val retrofit = Retrofit.Builder()
                    .baseUrl("https://api.openweathermap.org/data/2.5/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                retrofitService = retrofit.create(RetrofitService::class.java)
            }
            return retrofitService!!
        }

    }
}