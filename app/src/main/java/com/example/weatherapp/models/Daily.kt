package com.example.weatherapp.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Daily(
    @SerializedName("dt"         ) var dt        : Long?               = null,
    @SerializedName("sunrise"    ) var sunrise   : Int?               = null,
    @SerializedName("sunset"     ) var sunset    : Int?               = null,
    @SerializedName("moonrise"   ) var moonrise  : Int?               = null,
    @SerializedName("moonset"    ) var moonset   : Int?               = null,
    @SerializedName("moon_phase" ) var moonPhase : Double?            = null,
    @SerializedName("temp"       ) var temp      : Temp?              = Temp(),
    @SerializedName("feels_like" ) var feelsLike : FeelsLike?         = FeelsLike(),
    @SerializedName("pressure"   ) var pressure  : Double?               = null,
    @SerializedName("humidity"   ) var humidity  : Double?               = null,
    @SerializedName("dew_point"  ) var dewPoint  : Double?            = null,
    @SerializedName("wind_speed" ) var windSpeed : Double?               = null,
    @SerializedName("wind_deg"   ) var windDeg   : Double?               = null,
    @SerializedName("wind_gust"  ) var windGust  : Double?            = null,
    @SerializedName("weather"    ) var weather   : ArrayList<Weather> = arrayListOf(),
    @SerializedName("clouds"     ) var clouds    : Double?               = null,
    @SerializedName("pop"        ) var pop       : Double?               = null,
    @SerializedName("uvi"        ) var uvi       : Double?            = null
): Serializable
