package com.example.myweatherapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class NavigationActivity : AppCompatActivity() {

    companion object{
        private const val TAG = "NavigationActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navigation)

        replaceFragment(WeatherFromCoordinatesFragment())

        val navigation: BottomNavigationView = findViewById(R.id.bottomNavigationView)
        navigation.setOnNavigationItemSelectedListener(onNavClick)
    }

    private val onNavClick = BottomNavigationView.OnNavigationItemSelectedListener {
        when(it.itemId){
            R.id.weather_from_coordinates ->{
                Log.d(TAG, "Weather from coordinates fragment loaded")
                replaceFragment(WeatherFromCoordinatesFragment())
                return@OnNavigationItemSelectedListener true
            }
            R.id.weather_from_city_name ->{
                Log.d(TAG, "Weather from city name fragment loaded")
                replaceFragment(WeatherFromCityNameFragment())
                return@OnNavigationItemSelectedListener true
            }
        }
        return@OnNavigationItemSelectedListener true
    }

    fun replaceFragment(fragment: Fragment){
        val fragmentTransition = supportFragmentManager.beginTransaction()
        fragmentTransition.replace(R.id.fragment_holder, fragment)
        fragmentTransition.commit()
    }
}
