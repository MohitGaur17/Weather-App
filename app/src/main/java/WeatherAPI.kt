import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherAPI {
    @GET("v1/forecast")
    fun getWeather(
        @Query("latitude") lat: Double,
        @Query("longitude") long: Double,
        @Query("current_weather") currentWeather: Boolean = true,
        @Query("hourly") hourly: String = "temperature_2m,relative_humidity_2m,windspeed_10m",
        @Query("forecast_days") forecastDays: Int = 2   // 👈 at least 2 days to get next 24h
    ): Call<WeatherResponse>
}
