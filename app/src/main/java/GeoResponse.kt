data class GeoResponse(
    val results: List<GeoResult>?
)

data class GeoResult(
    val latitude: Double,
    val longitude: Double
)