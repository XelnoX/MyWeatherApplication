package com.example.myweatherapplication

import com.google.gson.annotations.SerializedName
import java.util.ArrayList

class WeatherResponse{
    @SerializedName("name")
    val name: String = ""

    @SerializedName("weather")
    val weather: List<Weather> = ArrayList()

    @SerializedName("wind")
    val wind: Wind = Wind()
}