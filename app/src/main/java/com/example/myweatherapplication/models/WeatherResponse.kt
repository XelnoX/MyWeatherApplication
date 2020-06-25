package com.example.myweatherapplication.models

import com.google.gson.annotations.SerializedName
import java.util.ArrayList

class WeatherResponse{
    @SerializedName("coord")
    val coord: Coord =
        Coord()

    @SerializedName("weather")
    val weather: List<Weather> = ArrayList()

    @SerializedName("base")
    val base: String = ""

    @SerializedName("main")
    val main: Main =
        Main()

    @SerializedName("visibility")
    val visibility: String = ""

    @SerializedName("wind")
    val wind: Wind =
        Wind()

    @SerializedName("clouds")
    val clouds: Clouds =
        Clouds()

    @SerializedName("dt")
    val dt: String = ""

    @SerializedName("sys")
    val sys: Sys =
        Sys()

    @SerializedName("timezone")
    val timezone: String = ""

    @SerializedName("id")
    val id: String = ""

    @SerializedName("name")
    val name: String = ""

    @SerializedName("cod")
    val cod: String = ""
}