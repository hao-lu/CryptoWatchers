package lu.hao.cryptowatchers.model.data

data class MapResponse(
    val data: List<MapData>,
    val status: MapStatus
)

data class MapData(
    val first_historical_data: String,
    val id: String,
    val is_active: Int,
    val last_historical_data: String,
    val name: String,
    var platform: Platform?,
    val slug: String,
    val symbol: String
)

data class Platform(
    val id: Int,
    val name: String,
    val slug: String,
    val symbol: String,
    val token_address: String
)

data class MapStatus(
    val credit_count: Int,
    val elapsed: Int,
    val error_code: Int,
    val error_message: String,
    val timestamp: String
)