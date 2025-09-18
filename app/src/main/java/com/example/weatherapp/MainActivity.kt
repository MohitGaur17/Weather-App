package com.example.weatherapp

import GeoAPI
import GeoResponse
import WeatherAPI
import WeatherResponse
import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    private lateinit var geoApi: GeoAPI
    private lateinit var weatherApi: WeatherAPI

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val cityInput = findViewById<EditText>(R.id.cityInput)
        val getWeatherButton: Button = findViewById(R.id.getWeatherButton)
        val cityName = findViewById<TextView>(R.id.cityName)

        // Retrofit for Geo API
        val geoRetrofit = Retrofit.Builder()
            .baseUrl("https://geocoding-api.open-meteo.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        geoApi = geoRetrofit.create(GeoAPI::class.java)

        // Retrofit for Weather API
        val weatherRetrofit = Retrofit.Builder()
            .baseUrl("https://api.open-meteo.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        weatherApi = weatherRetrofit.create(WeatherAPI::class.java)

        getWeatherButton.setOnClickListener {
            val city = cityInput.text.toString().trim()
            cityName.text = city
            if (city.isNotEmpty()) {
                getCoordinateAndWeather(city)
            } else {
                Toast.makeText(this, "Please enter a city", Toast.LENGTH_SHORT).show()
            }
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun getCoordinateAndWeather(city: String) {
        val temperature = findViewById<TextView>(R.id.temperature)
        val humidity = findViewById<TextView>(R.id.humidityData)
        val wind = findViewById<TextView>(R.id.windData)
        val hourlyData = findViewById<RecyclerView>(R.id.hourlyData)

        hourlyData.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        geoApi.getCityCoordinates(city).enqueue(object : retrofit2.Callback<GeoResponse> {
            override fun onResponse(call: Call<GeoResponse>, response: Response<GeoResponse>) {
                if (response.isSuccessful) {
                    val geoResponse = response.body()
                    if (!geoResponse?.results.isNullOrEmpty()) {
                        val lat = geoResponse.results[0].latitude
                        val long = geoResponse.results[0].longitude

                        val weatherCall = weatherApi.getWeather(lat, long)
                        weatherCall.enqueue(object : retrofit2.Callback<WeatherResponse?> {
                            @SuppressLint("SetTextI18n")
                            override fun onResponse(
                                call: Call<WeatherResponse?>,
                                response: Response<WeatherResponse?>
                            ) {
                                if (response.isSuccessful) {
                                    val weatherResponse = response.body()
                                    if (weatherResponse != null) {
                                        val currentWeather = weatherResponse.current_weather
                                        temperature.text = "${currentWeather.temperature} °C"
                                        humidity.text =
                                            "${weatherResponse.hourly.relative_humidity_2m.firstOrNull() ?: "-"} %"
                                        wind.text = "${currentWeather.windspeed} km/h"

                                        // Change weather icon based on weathercode
                                        val weatherImage = findViewById<ImageView>(R.id.weatherImage)
                                        when (currentWeather.weathercode) {
                                            0 -> weatherImage.setImageResource(R.drawable.sunny_weather) // Clear sky
                                            1, 2 -> weatherImage.setImageResource(R.drawable.partly_cloudy) // Mainly clear / Partly cloudy
                                            3 -> weatherImage.setImageResource(R.drawable.cloudy) // Overcast
                                            61, 63, 65 -> weatherImage.setImageResource(R.drawable.rain) // Rain
                                            71, 73, 75 -> weatherImage.setImageResource(R.drawable.snow) // Snow
                                            95, 96, 99 -> weatherImage.setImageResource(R.drawable.thunderstorm) // Thunderstorms
                                        }

                                        // ✅ Fill RecyclerView with NEXT 24 hours
                                        val next24Hours = weatherResponse.hourly.time.take(24)
                                        val next24Temps = weatherResponse.hourly.temperature_2m.take(24)

                                        hourlyData.adapter = HourlyAdapter(next24Hours, next24Temps)
                                    }
                                }
                            }

                            override fun onFailure(call: Call<WeatherResponse?>, t: Throwable) {
                                Toast.makeText(
                                    this@MainActivity,
                                    "City Weather Response Error: ${t.message}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        })
                    }
                }
            }

            override fun onFailure(call: Call<GeoResponse>, t: Throwable) {
                Toast.makeText(
                    this@MainActivity,
                    "Error: City Not Found ${t.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
}
