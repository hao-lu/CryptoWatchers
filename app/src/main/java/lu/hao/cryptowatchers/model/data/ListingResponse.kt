package lu.hao.cryptowatchers.model.data

data class LatestListingResponse(
    val `data`: List<Data>,
    val status: Status
)

data class Data(
    val circulating_supply: Double,
    val cmc_rank: Double,
    val date_added: String,
    val id: String,
    val last_updated: String,
    val max_supply: Double,
    val name: String,
    val num_market_pairs: Double,
    val platform: Any,
    val quote: Quote,
    val slug: String,
    val symbol: String,
    val tags: List<String>,
    val total_supply: Double
)

data class Quote(
    val BTC: BTC,
    val USD: USD
)

data class BTC(
    val last_updated: String,
    val market_cap: Double,
    val percent_change_1h: Double,
    val percent_change_24h: Double,
    val percent_change_7d: Double,
    val price: Double,
    val volume_24h: Double
)

data class USD(
    val last_updated: String,
    val market_cap: Double,
    val percent_change_1h: Double,
    val percent_change_24h: Double,
    val percent_change_7d: Double,
    val price: Double,
    val volume_24h: Double
)

data class Status(
    val credit_count: Double,
    val elapsed: Double,
    val error_code: Double,
    val error_message: String,
    val timestamp: String
)