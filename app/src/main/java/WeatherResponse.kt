data class WeatherResponse(
    val current_weather: CurrentWeather,
    val hourly: Hourly
)

data class CurrentWeather(
    val temperature: Double,
    val windspeed: Double,
    val winddirection: Double,
    val weathercode: Int,
    val time: String
)

data class Hourly(
    val time: List<String>,
    val temperature_2m: List<Double>,
    val relative_humidity_2m: List<Int>,
    val windspeed_10m: List<Double>
)
