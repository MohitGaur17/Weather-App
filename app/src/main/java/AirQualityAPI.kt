import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface AirQualityAPI {
    @GET("v1/air-quality")
    fun getAirQuality(
        @Query("latitude") lat: Double,
        @Query("longitude") lon: Double,
        @Query("hourly") hourly: String = "uv_index,us_aqi",
        @Query("timezone") timezone: String = "auto"
    ): Call<AirQualityResponse>
}