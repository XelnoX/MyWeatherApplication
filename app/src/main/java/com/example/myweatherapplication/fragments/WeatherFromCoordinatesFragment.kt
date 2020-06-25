package com.example.myweatherapplication.fragments

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
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
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.getSystemService
import com.example.myweatherapplication.R
import com.example.myweatherapplication.models.WeatherResponse
import com.example.myweatherapplication.retrofit.RetroWeather
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

class WeatherFromCoordinatesFragment : Fragment(), LocationListener {

    companion object{
        private const val BASE_URL = "https://api.openweathermap.org/data/2.5/"
        private const val API_KEY = "12337d40700c05ecf4d3d80e83ece554"
        private const val TAG = "WeatherFromCoordinatesFragment"
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }

    private val gson: Gson = GsonBuilder().setLenient().create()

    var builder: Retrofit.Builder = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create(gson))

    private var retrofit: Retrofit = builder.build()

    private lateinit var etLatitude: EditText
    private lateinit var etLongitude: EditText
    private lateinit var btnGetWeather: Button
    private lateinit var btnUseCurrentLocation: Button
    private lateinit var tvCityName: TextView
    private lateinit var tvDescription: TextView
    private lateinit var tvPlusInfo: TextView
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_weather_from_coordinates, container, false)

        etLatitude = view.findViewById(R.id.et_latitude)
        etLongitude = view.findViewById(R.id.et_longitude)
        btnGetWeather = view.findViewById(R.id.btn_get_weather)
        btnUseCurrentLocation = view.findViewById(R.id.btn_use_current_loc)
        tvCityName = view.findViewById(R.id.tv_city_name)
        tvDescription = view.findViewById(R.id.tv_description)
        tvPlusInfo = view.findViewById(R.id.tv_plus_info)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity as Activity)

        btnGetWeather.setOnClickListener {
            hideKeyboard()

            val latitude = etLatitude.text.toString()
            val longitude = etLongitude.text.toString()

            val retroWeather = retrofit.create(RetroWeather::class.java)

            val locale: Locale = resources.configuration.locales.get(0)
            Log.d(TAG, locale.toString())

            val call = retroWeather.tryGetWeatherFromCoordinates(latitude, longitude, locale.toString(), API_KEY)

            call.enqueue(object : Callback<WeatherResponse> {
                override fun onFailure(call: Call<WeatherResponse>?, t: Throwable?) {
                    Log.d(TAG, "Failure during getting weather information", t)
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
                            Log.d(TAG, response.code().toString())
                        }
                    }
                }
            })
        }

        btnUseCurrentLocation.setOnClickListener {
            hideKeyboard()
            if (ActivityCompat.checkSelfPermission(context!!, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(context!!, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity as Activity, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
                return@setOnClickListener
            }

            val locationManager = activity!!.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0f, this)
            locationManager.removeUpdates(this)
            val location: Location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)!!
            etLatitude.setText(location.latitude.toString())
            Log.d(TAG, location.latitude.toString())
            etLongitude.setText(location.longitude.toString())
            Log.d(TAG, location.longitude.toString())

                    /*fusedLocationClient.lastLocation.addOnSuccessListener(activity as Activity) { location ->
                    if(location != null){
                        etLatitude.setText(location.latitude.toString())
                        Log.d(TAG, location.latitude.toString())
                        etLongitude.setText(location.longitude.toString())
                        Log.d(TAG, location.longitude.toString())
                    }else{
                        Log.d(TAG, "Location is null")
                    }
                }*/

        }
        return view
    }

    override fun onLocationChanged(location: Location?) {
        etLatitude.setText(location!!.latitude.toString())
        Log.d(TAG, location.latitude.toString())
        etLongitude.setText(location.longitude.toString())
        Log.d(TAG, location.longitude.toString())
    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
    }

    override fun onProviderEnabled(provider: String?) {
    }

    override fun onProviderDisabled(provider: String?) {
    }

    private fun hideKeyboard(){
        val inputMethodManager = context!!.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        inputMethodManager?.hideSoftInputFromWindow(activity!!.currentFocus?.windowToken, 0)
    }

}
