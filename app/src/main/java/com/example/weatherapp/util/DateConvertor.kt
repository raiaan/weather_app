package com.example.weatherapp.util

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("SimpleDateFormat")
fun dateFromLongToStr(time:Long, pattern:String):String{
    val date = Date(time*1000)
    val formatter = SimpleDateFormat(pattern)
    return formatter.format(date)
}
fun dateFromLongToStr(time:Date ,pattern:String):String{
    val formatter = SimpleDateFormat(pattern)
    return formatter.format(time)
}
