package com.example.weatherapp.util

import java.text.SimpleDateFormat
import java.util.*

fun dateFromLongToStr(time:Long ,pattern:String):String{
    val date = Date(time)
    val formatter = SimpleDateFormat(pattern)
    return formatter.format(date)
}
fun dateFromLongToStr(time:Date ,pattern:String):String{
    val formatter = SimpleDateFormat(pattern)
    return formatter.format(time)
}
