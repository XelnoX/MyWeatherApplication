package com.example.myweatherapplication

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface RetroWeather {

    @GET("weather?")
    fun tryGetWeather(@Query("lat") lat: String,
                      @Query("lon") lon: String,
                      @Query("appid") appid: String): Call<WeatherResponse>
}