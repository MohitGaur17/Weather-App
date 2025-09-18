import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface GeoAPI {
    @GET("v1/search")
    fun getCityCoordinates(
        @Query("name") city: String,
        @Query("count") count: Int = 1
    ): Call<GeoResponse>
}