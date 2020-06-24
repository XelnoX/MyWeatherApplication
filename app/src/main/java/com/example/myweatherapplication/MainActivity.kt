package com.example.myweatherapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    companion object{
        private const val BASE_URL = "https://api.openweathermap.org/data/2.5/"
        private const val API_KEY = "12337d40700c05ecf4d3d80e83ece554"
        private const val TAG = "MainActivity"
    }

    private val gson: Gson = GsonBuilder().setLenient().create()

    var builder: Retrofit.Builder = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create(gson))

    private var retrofit: Retrofit = builder.build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_get_weather.setOnClickListener {
            val retroWeather = retrofit.create(RetroWeather::class.java)

            val call = retroWeather.tryGetWeather("47.89902", "20.3747", API_KEY)

            call.enqueue(object : Callback<WeatherResponse>{
                override fun onFailure(call: Call<WeatherResponse>?, t: Throwable?) {
                    Log.d(TAG, t!!.message.toString())
                }

                override fun onResponse(call: Call<WeatherResponse>?, response: Response<WeatherResponse>?) {
                    when(response!!.code()){
                        200 -> {
                            val weatherInfo = response.body()
                            tv_city_name.text = weatherInfo.name
                            Log.d(TAG, "Successfully got the information!")
                        }
                        else ->{
                            Log.d(TAG, response.code().toString())
                        }
                    }
                }

            })
        }
    }
}
