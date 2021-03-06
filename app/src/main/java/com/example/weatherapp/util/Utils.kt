package com.example.weatherapp.util

import android.annotation.SuppressLint
import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.net.ConnectivityManager
import android.util.Log
import com.example.weatherapp.models.WeatherResponse
import com.google.firebase.firestore.GeoPoint
import com.google.gson.Gson
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.atomic.AtomicInteger


fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        val activeNetworkInfo = connectivityManager!!.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }
@SuppressLint("SimpleDateFormat")
fun dateFromLongToStr(time:Long, pattern:String):String{
    val date = Date(time*1000)
    val formatter = SimpleDateFormat(pattern)
    return formatter.format(date)
}
fun getDate(milliSeconds: Long, dateFormat: String?): String? {
    val formatter = SimpleDateFormat(dateFormat)
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = milliSeconds
    return formatter.format(calendar.time)
}
fun getDateTime(s: String , pattern:String): String {
    try {
        val sdf = SimpleDateFormat(pattern)
        val netDate = Date(s.toLong() * 1000)
        return sdf.format(netDate)
    } catch (e: Exception) {
        return e.toString()
    }
}
fun dateFromLongToStr(time: Date, pattern:String):String{
    val formatter = SimpleDateFormat(pattern)
    return formatter.format(time)
}
fun convertWeatherResponseToGson(w:WeatherResponse):String{
    val gson = Gson()
    return gson.toJson(w)
}
fun convertGsonToWeatherResponse(obj:String):WeatherResponse{
    val gson = Gson()
    return gson.fromJson(obj, WeatherResponse::class.java)
}
fun getCity(lat:String,lon:String,context:Context):String{
    val myLocation = Geocoder(context, Locale.getDefault())
    val myList: List<Address> =
        myLocation.getFromLocation(lat.toDouble(), lon.toDouble(), 1)
    val address: Address = myList[0]
    return address.subAdminArea
}
fun getRandomInt() = AtomicInteger().getAndIncrement() + System.currentTimeMillis().toInt()
fun getLocationFromAddress(context: Context,strAddress: String?): GeoPoint? {
    val coder = Geocoder(context)
    val address: List<Address>?
    var p1: GeoPoint? = null
    try {
        address = coder.getFromLocationName(strAddress, 5)
        if (address == null) {
            return null
        }
        val location = address[0]
        location.latitude
        location.longitude
        p1 = GeoPoint(
            (location.latitude),
            (location.longitude)
        )
        return p1
    } catch (e: IOException) {
        e.printStackTrace()
    }
    return null
}