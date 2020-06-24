package com.example.myweatherapplication

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class WeatherFromCoordinatesFragment : Fragment() {

    companion object{
        private const val BASE_URL = "https://api.openweathermap.org/data/2.5/"
        private const val API_KEY = "12337d40700c05ecf4d3d80e83ece554"
        private const val TAG = "WeatherFromCoordinatesFragment"
    }

    private val gson: Gson = GsonBuilder().setLenient().create()

    var builder: Retrofit.Builder = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create(gson))

    private var retrofit: Retrofit = builder.build()

    private lateinit var etLatitude: EditText
    private lateinit var etLongitude: EditText
    private lateinit var btnGetWeather: Button
    private lateinit var tvCityName: TextView
    private lateinit var tvDescription: TextView
    private lateinit var tvPlusInfo: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_weather_from_coordinates, container, false)

        etLatitude = view.findViewById(R.id.et_latitude)
        etLongitude = view.findViewById(R.id.et_longitude)
        btnGetWeather = view.findViewById(R.id.btn_get_weather)
        tvCityName = view.findViewById(R.id.tv_city_name)
        tvDescription = view.findViewById(R.id.tv_description)
        tvPlusInfo = view.findViewById(R.id.tv_plus_info)

        btnGetWeather.setOnClickListener {
            val latitude = etLatitude.text.toString()
            val longitude = etLongitude.text.toString()

            val retroWeather = retrofit.create(RetroWeather::class.java)

            val call = retroWeather.tryGetWeather(latitude, longitude, API_KEY)

            call.enqueue(object : Callback<WeatherResponse> {
                override fun onFailure(call: Call<WeatherResponse>?, t: Throwable?) {
                    Log.d(TAG, "Failure during getting weather information", t)
                }

                override fun onResponse(call: Call<WeatherResponse>?, response: Response<WeatherResponse>?) {
                    when(response!!.code()){
                        200 -> {
                            val weatherInfo = response.body()
                            tvCityName.text = weatherInfo.name
                            val tempDescription = "The weather is: ${weatherInfo.weather[0].main}"
                            tvDescription.text = tempDescription
                            val tempPlusInfo = "The speed of the wind is: ${weatherInfo.wind.speed}"
                            tvPlusInfo.text = tempPlusInfo
                            Log.d(TAG, "Successfully got the information!")
                        }
                        else ->{
                            Log.d(TAG, response.code().toString())
                        }
                    }
                }
            })
        }
        return view
    }
}
