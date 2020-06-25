package com.example.myweatherapplication

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface RetroWeather {

    @GET("weather?")
    fun tryGetWeatherFromCoordinates(@Query("lat") lat: String,
                                     @Query("lon") lon: String,
                                     @Query("appid") appid: String): Call<WeatherResponse>

    @GET("weather?")
    fun tryGetWeatherFromCityName(@Query("q") q: String,
                                  @Query("appid") appid: String): Call<WeatherResponse>
}