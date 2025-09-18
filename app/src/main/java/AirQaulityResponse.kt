data class AirQualityResponse(
    val hourly: AirQualityHourly
)

data class AirQualityHourly(
    val time: List<String>,
    val uv_index: List<Double>?,
    val us_aqi: List<Int>?,
)