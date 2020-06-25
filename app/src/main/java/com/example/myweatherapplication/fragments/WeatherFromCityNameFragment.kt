package com.example.myweatherapplication.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.myweatherapplication.R
import com.example.myweatherapplication.models.WeatherResponse
import com.example.myweatherapplication.retrofit.RetroWeather
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

class WeatherFromCityNameFragment : Fragment() {

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

    private lateinit var etCityName: EditText
    private lateinit var btnGetWeather: Button
    private lateinit var tvCityName: TextView
    private lateinit var tvDescription: TextView
    private lateinit var tvPlusInfo: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_weather_from_city_name, container, false)

        etCityName = view.findViewById(R.id.et_city_name)
        btnGetWeather = view.findViewById(R.id.btn_get_weather_by_city_name)
        tvCityName = view.findViewById(R.id.tv_city_name_by_city_name)
        tvDescription = view.findViewById(R.id.tv_description_by_city_name)
        tvPlusInfo = view.findViewById(R.id.tv_plus_info_by_city_name)

        btnGetWeather.setOnClickListener {
            hideKeyboard()

            val cityName = etCityName.text.toString()

            val retroWeather = retrofit.create(RetroWeather::class.java)

            val locale: Locale = resources.configuration.locales.get(0)
            Log.d(TAG, locale.toString())

            val call: Call<WeatherResponse> = retroWeather.tryGetWeatherFromCityName(cityName, locale.toString(), API_KEY)

            call.enqueue(object : Callback<WeatherResponse> {
                override fun onFailure(call: Call<WeatherResponse>?, t: Throwable?) {
                    Log.d(TAG, "Failure during getting weather information", t)
                    Toast.makeText(context, resources.getString(R.string.try_again), Toast.LENGTH_SHORT).show()
                }


                override fun onResponse(call: Call<WeatherResponse>?, response: Response<WeatherResponse>?) {
                    when(response!!.code()){
                        200 -> {
                            val weatherInfo = response.body()
                            tvCityName.text = weatherInfo.name
                            val tempDescription = weatherInfo.weather[0].description
                            tvDescription.text = tempDescription
                            val tempPlusInfo = "${resources.getString(R.string.the_speed_of_the_wind)} ${weatherInfo.wind.speed} km/h"
                            tvPlusInfo.text = tempPlusInfo
                            Log.d(TAG, "Successfully got the information!")
                        }
                        else ->{
                            tvCityName.text = ""
                            tvDescription.text = ""
                            tvPlusInfo.text = ""
                            Toast.makeText(context, resources.getString(R.string.name_problem), Toast.LENGTH_SHORT).show()
                            Log.d(TAG, response.code().toString())
                        }
                    }
                }
            })
        }
        return view
    }

    private fun hideKeyboard(){
        val inputMethodManager = context!!.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        inputMethodManager?.hideSoftInputFromWindow(activity!!.currentFocus?.windowToken, 0)
    }
}
