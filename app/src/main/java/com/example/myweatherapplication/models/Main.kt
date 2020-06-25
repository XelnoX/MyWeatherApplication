package com.example.myweatherapplication.models

import com.google.gson.annotations.SerializedName

class Main {
    @SerializedName("temp")
    val temp: String = ""

    @SerializedName("feels_like")
    val feelsLike: String = ""

    @SerializedName("temp_min")
    val min: String = ""

    @SerializedName("temp_max")
    val max: String = ""

    @SerializedName("pressure")
    val pressure: String = ""

    @SerializedName("humidity")
    val humidity: String = ""
}